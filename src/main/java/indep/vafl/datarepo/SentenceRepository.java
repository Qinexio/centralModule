package indep.vafl.datarepo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import indep.vafl.entity.Sentence;

public interface SentenceRepository extends JpaRepository<Sentence, Long>{

	Page<Sentence> findBySentenceIsVerified(boolean truthVal, Pageable pageable);

}

