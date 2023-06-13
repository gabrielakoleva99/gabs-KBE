package htwb.ai.repo;

import java.util.List;

import htwb.ai.modell.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByUserId(String userId);
}
