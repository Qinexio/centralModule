package indep.vafl.datarepo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import indep.vafl.entity.Credidential;

@Repository
public interface CredidentialRepository extends JpaRepository<Credidential, Integer> {
	Optional<Credidential> findByCredUserId(Integer userIdfk);
}
