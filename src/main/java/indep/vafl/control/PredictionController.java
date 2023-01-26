package indep.vafl.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import indep.vafl.datarepo.BatchRepository;
import indep.vafl.datarepo.SentenceRepository;
import indep.vafl.entity.Batch;
import indep.vafl.entity.Sentence;
import indep.vafl.service.PredictionService;
import indep.vafl.service.TrainService;
import indep.vafl.utility.EntityNotFoundException;
import indep.vafl.utility.PredictionServiceOfflineException;

@RestController
public class PredictionController {

	@Autowired
	SentenceRepository sentenceRepository;

	@Autowired
	BatchRepository batchRepository;

	@Autowired
	PredictionService predictService;
	
	@Autowired
	TrainService trainService;

	@Component
	@ConfigurationProperties(prefix = "predict")
	public class SentimentConfig {
		private final List<String> sentiment = new ArrayList<String>();

		public Boolean isPresent(String toCheck) {
			return sentiment.contains(toCheck);
		}

		public List<String> getSentiment() {
			return sentiment;
		}
	}

	@Autowired
	SentimentConfig sentConfig;

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping("/batch/{truthVal}")
	public Page<Batch> getBatchByVerification(@PathVariable(value = "truthVal") Boolean truthVal, Pageable pageable) {
		return batchRepository.findByBatchComplete(truthVal, pageable);
	}

	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@GetMapping("/batch/incomplete/{batchID}")
	public Batch getBatchUnverified(@PathVariable(value = "batchID") Integer batchID) {
		Batch toSend = batchRepository.findById(batchID).get();
		Set<Sentence> toModify = toSend.getBatchSentences().stream()
				.filter(sentence -> sentence.isSentenceIsVerified() == false).collect(Collectors.toSet());

		toSend.setBatchSentences(toModify);
		return toSend;
	}
	

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping("/sentence/{sentenceID}")
	public Optional<Sentence> getSentenceByID(@PathVariable(value = "sentenceID") Long sentID) {
		return sentenceRepository.findById(sentID);
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/batchAll")
	public Page<Batch> getAll(Pageable pageable) {
		return batchRepository.findAll(pageable);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping("/sentiments")
	public Iterable<String> getSentiments() {
		return sentConfig.getSentiment();
	}
	
	@Secured({"ROLE_ADMIN" })
	@GetMapping("/trainModel/{useCSV}/{useGloVE}")
	public Boolean trainModel(@PathVariable(value = "useCSV") Boolean useCSV, @PathVariable(value = "useGloVE") Boolean useGloVE) throws JsonParseException, JsonMappingException, IOException {
		List<Batch> toSend = StreamSupport.stream(batchRepository.findByBatchComplete(true).spliterator(), false)
			    .collect(Collectors.toList());
		
		return trainService.trainModel(toSend, useCSV, useGloVE);
	
	}
	
	@Secured({"ROLE_ADMIN" })
	@GetMapping("/trainReload")
	public Boolean reloadModel() {	
		return trainService.reloadModel();
	}
	
	@PostMapping("/batch")
	public Batch createBatch(@Valid @RequestBody String text) {
		Batch toCreate = new Batch();
		Set<Sentence> toAdd = null;

		try {
			toAdd = predictService.predictText(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		if (toAdd == null) {
			throw new PredictionServiceOfflineException();
		}
		
		toCreate.setBatchSentences(toAdd);

		for (Sentence toProcess : toCreate.getBatchSentences()) {
			toProcess.setSentenceBatch(toCreate);
			toProcess.setSentenceIsVerified(false);
		}

		toCreate = batchRepository.save(toCreate);// hopefully it does the same with the sentences, otherwise I'll have
													// to do it before if missing id's show up

		return toCreate;
	}

	@PutMapping("/sentence/{sentenceID}/feedback/{truthVal}")
	public Sentence updateSentenceTruth(@PathVariable(value = "sentenceID") Long sentenceID,
			@PathVariable(value = "truthVal") Boolean feedbackVal) {
		return sentenceRepository.findById(sentenceID).map(sentence -> {
			sentence.setSentenceUserInput(feedbackVal);
			return sentenceRepository.save(sentence);
		}).orElseThrow(() -> new EntityNotFoundException("sentenceID " + sentenceID + "not found"));
	}
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/sentence/{sentenceID}/sentimentUpdate/{sentVal}")
	public Sentence updateSentenceSentiment(@PathVariable(value = "sentenceID") Long sentenceID,
			@PathVariable(value = "sentVal") String feedbackVal) {
		if (sentConfig.isPresent(feedbackVal))
			return sentenceRepository.findById(sentenceID).map(sentence -> {
				sentence.setSentencePrediction(feedbackVal);
				sentence.setSentenceIsVerified(true);
				sentence.setSentenceUserInput(true);
				return sentenceRepository.save(sentence);
			}).orElseThrow(() -> new EntityNotFoundException("sentenceID " + sentenceID + "not found"));
		else
			throw new EntityNotFoundException("invalid sentiment:" + feedbackVal);
	}
	
	//sanity check version, because these are semi-trusted users
	@Secured("ROLE_USER")
	@PutMapping("/sentence/{sentenceID}/sentimentUpdateMod/{sentVal}")
	public Sentence updateSentenceSentimentMod(@PathVariable(value = "sentenceID") Long sentenceID,
			@PathVariable(value = "sentVal") String feedbackVal) {
			 
		if (sentConfig.isPresent(feedbackVal))
			return sentenceRepository.findById(sentenceID).map(sentence -> {
				if(sentence.isSentenceIsVerified() == true)
					throw new EntityNotFoundException("moderator attempted to update verified sentence");
				sentence.setSentencePrediction(feedbackVal);
				sentence.setSentenceIsVerified(true);
				sentence.setSentenceUserInput(true);
				return sentenceRepository.save(sentence);
			}).orElseThrow(() -> new EntityNotFoundException("sentenceID " + sentenceID + "not found"));
		else
			throw new EntityNotFoundException("invalid sentiment:" + feedbackVal);
	}

	@Secured("ROLE_ADMIN")
	@PutMapping("/sentence/{sentenceID}")
	public Sentence updateSentence(@PathVariable(value = "sentenceID") Long sentenceID,
			@Valid @RequestBody Sentence sentenceRequest) {
		return sentenceRepository.findById(sentenceID).map(sentence -> {
			sentence.setSentenceText(sentenceRequest.getSentenceText());
			sentence.setSentenceIsVerified(sentenceRequest.isSentenceIsVerified());
			sentence.setSentencePrediction(sentenceRequest.getSentencePrediction());
			sentence.setSentenceUserInput(sentenceRequest.getSentenceUserInput());
			return sentenceRepository.save(sentence);
		}).orElseThrow(() -> new EntityNotFoundException("sentenceID " + sentenceID + "not found"));
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@PutMapping("/sentence/{sentenceID}/validate")
	public Sentence validateSentence(@PathVariable(value = "sentenceID") Long sentenceID) {
		return sentenceRepository.findById(sentenceID).map(sentence -> {
			sentence.setSentenceIsVerified(true);
			return sentenceRepository.save(sentence);
		}).orElseThrow(() -> new EntityNotFoundException("sentenceID " + sentenceID + "not found"));
	}

	@Secured("ROLE_ADMIN")
	@PutMapping("/sentence/{sentenceID}/invalidate")
	public Sentence invalidateSentence(@PathVariable(value = "sentenceID") Long sentenceID) {
		return sentenceRepository.findById(sentenceID).map(sentence -> {
			sentence.setSentenceIsVerified(true);
			return sentenceRepository.save(sentence);
		}).orElseThrow(() -> new EntityNotFoundException("sentenceID " + sentenceID + "not found"));
	}
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/batch/validate/{batchID}")
	public Batch validateBatch(@PathVariable(value = "batchID") Integer batchID) {
		return batchRepository.findById(batchID).map(batch -> {
			batch.setBatchComplete(!batch.isBatchComplete());
			return batchRepository.save(batch);
		}).orElseThrow(() -> new EntityNotFoundException("batchID " + batchID + "not found"));
	}
	
	@Secured("ROLE_ADMIN")
	@DeleteMapping("/sentence/{sentenceID}")
	public ResponseEntity<?> deleteSentence(@PathVariable(value = "sentenceID") Long sentenceID) {
		return sentenceRepository.findById(sentenceID).map(sentence -> {
			sentenceRepository.delete(sentence);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new EntityNotFoundException("sentenceID " + sentenceID + " not found"));
	}
	
	@Secured("ROLE_ADMIN")
	@DeleteMapping("/batch/{batchID}")
	public ResponseEntity<?> deleteBatch(@PathVariable(value = "batchID") Integer batchID) {
		return batchRepository.findById(batchID).map(batch -> {
			batchRepository.delete(batch);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new EntityNotFoundException("sentenceID " + batchID + " not found"));
	}

}
