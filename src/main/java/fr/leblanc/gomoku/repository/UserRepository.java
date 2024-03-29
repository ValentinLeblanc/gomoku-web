
package fr.leblanc.gomoku.repository;

import org.springframework.stereotype.Repository;
import fr.leblanc.gomoku.model.User;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface UserRepository extends CrudRepository<User, Long>
{
    User findByUsername(final String username);
}