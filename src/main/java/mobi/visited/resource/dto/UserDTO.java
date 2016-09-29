package mobi.visited.resource.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import static java.util.Objects.requireNonNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {

    private Long id;
    private String email;
    private String password;

    public UserDTO() {
    }

    public UserDTO(Long id, String email, String password) {
        this(email, password);
        this.id = id;
    }

    public UserDTO(String email, String password) {
        this.email = requireNonNull(email, "email != null");
        this.password = requireNonNull(password, "password != null");
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "UserDCO{" +
               "id=" + id +
               "email=" + email +
               ", password=***" +
               '}';
    }
}
