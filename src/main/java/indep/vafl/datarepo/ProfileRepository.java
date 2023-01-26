package indep.vafl.datarepo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import indep.vafl.entity.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile,Integer>{
	Optional<Profile> findByProfileUserId(Integer userIdfk);
}
