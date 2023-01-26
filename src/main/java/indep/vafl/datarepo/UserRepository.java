package indep.vafl.datarepo;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import indep.vafl.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>{

	boolean existsByUserConfirmation(String validationCode);
	Optional<User> findByUserConfirmation(String validationCode);
	Optional<User> findByUserName(String userName);
	boolean existsByUserName(String userName);
	boolean existsByUserMail(String userMail);

}
