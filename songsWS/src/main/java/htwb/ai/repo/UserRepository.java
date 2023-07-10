package htwb.ai.repo;

import htwb.ai.modell.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUserId(String userId);
    User findByToken(String token);

}
