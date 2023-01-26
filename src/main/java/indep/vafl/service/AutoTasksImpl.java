package indep.vafl.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import indep.vafl.datarepo.BatchRepository;
import indep.vafl.entity.Batch;
import indep.vafl.entity.Sentence;

@Lazy(false)
@Component
public class AutoTasksImpl implements AutoTasks {

	@Autowired
	BatchRepository batchRepository;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	@Scheduled(cron = "0 0 0 * * *")//which is every new day
	public void validateFull() {
		logger.warn("Checking for fully validated entries.");

		Iterable<Batch> results = batchRepository.findByBatchComplete(false);

		for (Batch batch : results) {
			Boolean isFull = true;

			for (Sentence sentence : batch.getBatchSentences()) {
				if (!sentence.isSentenceIsVerified())
					if(!sentence.getSentenceUserInput())
						isFull = false;
			}

			if (isFull)
				batch.setBatchComplete(true);

			batchRepository.save(batch);
		}
	}
}
