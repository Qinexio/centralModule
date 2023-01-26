package indep.vafl.service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import indep.vafl.entity.Sentence;

@Service
public class PredictionServiceImpl implements PredictionService {

	private final RestTemplate restTemplate;

	@Value("${pycomp.domain}")
	private String domain;

	@Value("${pycomp.port}")
	private String port;

	public PredictionServiceImpl(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	public String getPostsPlainJSON() {

		return this.restTemplate.getForObject(this.domain + ":" + this.port, String.class);
	}

	@Override
	public Set<Sentence> predictText(String toPredict) throws JsonParseException, JsonMappingException, IOException {

		UriComponents uriComponents = UriComponentsBuilder.newInstance().scheme("http").host(this.domain)
				.port(this.port).path("/predict").query("txt={value}").buildAndExpand(toPredict);

		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		
		Map<String, Object> map = new HashMap<>();
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
		
		ResponseEntity<String> response = this.restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity,
				String.class);
		System.out.println(response.getHeaders());
		if (response.getStatusCode() == HttpStatus.OK) {
			
			Set<Sentence> toReturn = new HashSet<Sentence>();
			ObjectMapper mapper = new ObjectMapper();
			JsonNode result = mapper.readTree(response.getBody()).get("result");
			System.out.println(result.toString());
			for (JsonNode toParse : result) {	
				String textToAdd = toParse.get("sentenceText").asText();
				String sentimentToAdd = toParse.get("sentencePrediction").asText();
				String sentimentPercentages = toParse.get("sentencePercentage").asText();
				textToAdd = textToAdd.replaceAll("[{}]", " ");
				sentimentToAdd = sentimentToAdd.replaceAll("[{}]", " ");
				sentimentPercentages = sentimentPercentages.replaceAll("[{}]", " ");
				
				Sentence toAdd = new Sentence();
				toAdd.setSentenceIsVerified(false);
				toAdd.setSentenceUserInput(false);
				toAdd.setSentenceText(textToAdd);
				toAdd.setSentencePrediction(sentimentToAdd);
				toAdd.setSentencePercentages(sentimentPercentages);
				toReturn.add(toAdd);
			}

			return toReturn;

		} else {
			return null;
		}
	}

	public Set<Sentence> predictTextMock(String toPredict) {

		Set<Sentence> mockData = new HashSet<Sentence>();
		Sentence mockSentence1 = new Sentence();
		Sentence mockSentence2 = new Sentence();

		mockSentence1.setSentenceText("Something1");
		mockSentence1.setSentencePrediction("Love");
		mockSentence2.setSentenceText("Something2");
		mockSentence2.setSentencePrediction("Hate");

		mockData.add(mockSentence1);
		mockData.add(mockSentence2);
		return mockData;
	}

}
