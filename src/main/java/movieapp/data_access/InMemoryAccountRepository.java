// InMemoryAccountRepository.java
package movieapp.data_access;

import movieapp.entity.User;
import movieapp.interface_adapter.AccountRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryAccountRepository implements AccountRepository {
    private final Map<String, User> users = new HashMap<>();
    
    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.get(normalizeUsername(username)));
    }

    @Override
    public void save(User user) {
        users.put(normalizeUsername(user.getUsername()), user);
    }
    
    @Override
    public boolean existsByUsername(String username) {
        return users.containsKey(normalizeUsername(username));
    }
    
    private String normalizeUsername(String username) {
        return username.toLowerCase();
    }
}