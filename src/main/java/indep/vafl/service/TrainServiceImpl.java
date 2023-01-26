package indep.vafl.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import indep.vafl.entity.Batch;
import indep.vafl.entity.Sentence;
import indep.vafl.utility.SentenceDTO;

@Service
public class TrainServiceImpl implements TrainService {

	private final RestTemplate restTemplate;

	@Value("${pycomp.domain}")
	private String domain;

	@Value("${pycomp.port}")
	private String port;

	public TrainServiceImpl(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	public String getPostsPlainJSON() {

		return this.restTemplate.getForObject(this.domain + ":" + this.port, String.class);
	}

	@Override
	public Boolean trainModel(List<Batch> toSend, Boolean useCSV, Boolean useGloVE) throws JsonParseException, JsonMappingException, IOException {
		
		System.out.println("CSV="+useCSV);
		System.out.println("GLOVE ="+ useGloVE);
		UriComponents uriComponents = UriComponentsBuilder.newInstance().scheme("http").host(this.domain)
				.port(this.port).path("/train").queryParam("useCSV", useCSV).queryParam("useGloVE", useGloVE).buildAndExpand(useCSV);

		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		
		Map<String, Object> map = new HashMap<>();
		
		List<SentenceDTO> toProcess = new ArrayList<SentenceDTO>();
		
		for (Batch toExtract : toSend)
			for (Sentence toAdd : toExtract.getBatchSentences())
				toProcess.add(new SentenceDTO(toAdd.getSentenceText(), toAdd.getSentencePrediction()));
		
		map.put("sentences", toProcess);
		
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
		ResponseEntity<String> response = this.restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity,
				String.class);
		
		if (response.getStatusCode() == HttpStatus.OK) {
			return true;

		} else {
			return false;
		}
	}


	@Override
	public Boolean reloadModel() {
		UriComponents uriComponents = UriComponentsBuilder.newInstance().scheme("http").host(this.domain)
				.port(this.port).path("/reload").buildAndExpand();

		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		Map<String, Object> map = new HashMap<>();
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
		ResponseEntity<String> response = this.restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity,
				String.class);

		if (response.getStatusCode() == HttpStatus.OK) {
			return true;

		} else {
			return false;
		}
	}

	

}