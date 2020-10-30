package Rever.PillX.User;

import org.springframework.data.mongodb.repository.MongoRepository;

/*
 * Methods to search the user repository
 */
public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
}
