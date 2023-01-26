package indep.vafl.datarepo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import indep.vafl.entity.Batch;

public interface BatchRepository extends JpaRepository<Batch,Integer>{
	
	Page<Batch> findByBatchComplete(Boolean truthValue, Pageable pageable);
	Iterable<Batch> findByBatchComplete(Boolean truthValue);
	
}
