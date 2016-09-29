package mobi.visited.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import mobi.visited.model.User;
import mobi.visited.repository.UserRepository;
import mobi.visited.resource.dto.UserDTO;
import mobi.visited.security.SecurityUser;

@Service
public class UserService {

    private static final String DEFAULT_ROLE = "ROLE_USER";

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> getUsers() {
        return userRepository.getAll().stream()
            .map(user -> new UserDTO(user.getId(), user.getEmail(), user.getPassword()))
            .collect(Collectors.toList());
    }

    public Optional<UserDTO> getUserForResource(Long id) {
        Optional<User> userOptional = userRepository.getById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return Optional.of(new UserDTO(user.getId(), user.getEmail(), user.getPassword()));
        }
        return Optional.empty();
    }

    public SecurityUser getUserForSecurity(String email) {
        Optional<User> byEmail = userRepository.getByEmail(email);
        if (byEmail.isPresent()) {
            User user = byEmail.get();
            return new SecurityUser(user.getEmail(), user.getPassword(), DEFAULT_ROLE);
        }
        return null;
    }

    public Long registerNewUser(UserDTO dto) {
        User user = new User(dto.getEmail(), dto.getPassword());
        return userRepository.save(user);
    }

    public Optional<UserDTO> editUser(Long id, UserDTO dto) {
        Optional<User> userOptional = userRepository.getById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setEmail(dto.getEmail());
            user.setPassword(dto.getPassword());
            Optional<User> updatedOptional = userRepository.update(user);
            if (updatedOptional.isPresent()) {
                User updated = updatedOptional.get();
                return Optional.of(new UserDTO(updated.getId(), updated.getEmail(), updated.getPassword()));
            }
        }
        return Optional.empty();
    }

    public void deleteUser(Long id) {
        userRepository.delete(id);
    }

}
