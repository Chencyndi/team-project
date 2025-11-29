// AccountRepository.java
package movieapp.interface_adapter.login;

import movieapp.entity.User;

import java.util.Optional;

public interface AccountRepository {
    User findByUsername(String username);
    void save(User user);
    boolean existsByUsername(String username);
}