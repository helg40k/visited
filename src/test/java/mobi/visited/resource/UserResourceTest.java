package mobi.visited.resource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import mobi.visited.resource.dto.UserDTO;
import mobi.visited.service.UserService;

import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserResourceTest extends BaseMockMvcResourceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserResource userResource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userResource).build();
    }

    @Test
    public void save_success() throws Exception {
        String content = loadTemplate(
            "templates/user.json",
            "aaa@bbb.ccc",
            "123456"
        );
        mockMvc.perform(post("/user")
                            .contentType(contentType)
                            .content(content)
        ).andExpect(status().isOk());

        verify(userService).registerNewUser(isA(UserDTO.class));
    }

    @Test
    public void update_success() throws Exception {
        long userId = 100L;
        when(userService.editUser(same(userId), isA(UserDTO.class))).thenReturn(Optional.of(new UserDTO(userId, "test", "test")));

        String content = loadTemplate(
            "templates/user.json",
            "aaa@bbb.ccc",
            "123456"
        );
        mockMvc.perform(put("/user/{userId}", userId)
                            .contentType(contentType)
                            .content(content)
        ).andExpect(status().isOk());

        verify(userService).editUser(same(userId), isA(UserDTO.class));
    }

    @Test
    public void getAll_success() throws Exception {
        mockMvc.perform(get("/user")
                            .contentType(contentType)
        ).andExpect(status().isOk());

        verify(userService).getUsers();
    }

    @Test
    public void get_success() throws Exception {
        long userId = 100L;
        when(userService.getUserForResource(same(userId))).thenReturn(Optional.of(new UserDTO(userId, "test", "test")));

        mockMvc.perform(get("/user/{userId}", userId)
                            .contentType(contentType)
        ).andExpect(status().isOk());

        verify(userService).getUserForResource(same(userId));
    }

    @Test
    public void delete_success() throws Exception {
        long userId = 100L;
        mockMvc.perform(delete("/user/{userId}", userId)
                            .contentType(contentType)
        ).andExpect(status().isOk());

        verify(userService).deleteUser(same(userId));
    }

}
