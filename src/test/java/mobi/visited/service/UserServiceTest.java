package mobi.visited.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import mobi.visited.model.User;
import mobi.visited.repository.UserRepository;
import mobi.visited.resource.dto.UserDTO;
import mobi.visited.security.SecurityUser;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService eventService;

    @Test
    public void getUsers_success() {
        User user = new User("aaa@bbb.ccc", "123456");
        user.setId(100L);
        when(userRepository.getAll()).thenReturn(Collections.singletonList(user));
        List<UserDTO> list = eventService.getUsers();
        assertThat(list).hasSize(1);
        UserDTO result = list.get(0);
        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getPassword()).isEqualTo(user.getPassword());
        verify(userRepository).getAll();
    }

    @Test
    public void getUsers_empty() {
        when(userRepository.getAll()).thenReturn(Collections.emptyList());
        assertThat(eventService.getUsers()).hasSize(0);
        verify(userRepository).getAll();
    }

    @Test
    public void getUserForResource_success() {
        User user = new User("aaa@bbb.ccc", "123456");
        user.setId(100L);
        when(userRepository.getById(eq(user.getId()))).thenReturn(Optional.of(user));
        Optional<UserDTO> resultOptional = eventService.getUserForResource(user.getId());
        assertThat(resultOptional.isPresent()).isTrue();
        UserDTO result = resultOptional.get();
        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getPassword()).isEqualTo(user.getPassword());
        verify(userRepository).getById(eq(user.getId()));
    }

    @Test
    public void getUserForResource_failed() {
        Long userId = 100L;
        when(userRepository.getById(eq(userId))).thenReturn(Optional.empty());
        assertThat(eventService.getUserForResource(userId).isPresent()).isFalse();
        verify(userRepository).getById(eq(userId));
    }

    @Test
    public void getUserForSecurity_success() {
        User user = new User("aaa@bbb.ccc", "123456");
        when(userRepository.getByEmail(eq(user.getEmail()))).thenReturn(Optional.of(user));
        SecurityUser result = eventService.getUserForSecurity(user.getEmail());
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(user.getEmail());
        assertThat(result.getPassword()).isEqualTo(user.getPassword());
        verify(userRepository).getByEmail(eq(user.getEmail()));
    }

    @Test
    public void getUserForSecurity_failed() {
        String email = "aaa@bbb.ccc";
        when(userRepository.getByEmail(eq(email))).thenReturn(Optional.empty());
        SecurityUser result = eventService.getUserForSecurity(email);
        assertThat(result).isNull();
        verify(userRepository).getByEmail(eq(email));
    }

    @Test
    public void registerNewUser() {
        UserDTO userDTO = new UserDTO("aaa@bbb.ccc", "123456");
        when(userRepository.save(isA(User.class))).thenReturn(1L);
        assertThat(eventService.registerNewUser(userDTO)).isEqualTo(1L);
        verify(userRepository).save(isA(User.class));
    }

    @Test
    public void editUser() {
        Long id = 1L;
        UserDTO userDTO = new UserDTO("aaa@bbb.ccc", "123456");
        when(userRepository.getById(same(id))).thenReturn(Optional.of(new User("old", "old")));
        User user = new User(userDTO.getEmail(), userDTO.getPassword());
        user.setId(id);
        when(userRepository.update(isA(User.class)))
            .thenReturn(Optional.of(user));
        Optional<UserDTO> resultOptional = eventService.editUser(id, userDTO);
        assertThat(resultOptional.isPresent()).isTrue();
        UserDTO result = resultOptional.get();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getEmail()).isEqualTo(userDTO.getEmail());
        assertThat(result.getPassword()).isEqualTo(userDTO.getPassword());
        verify(userRepository).getById(same(id));
        verify(userRepository).update(isA(User.class));
    }

    @Test
    public void deleteUser() {
        Long id = 1L;
        eventService.deleteUser(id);
        verify(userRepository).delete(same(id));
    }
}
