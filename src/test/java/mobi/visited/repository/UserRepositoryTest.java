package mobi.visited.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import mobi.visited.model.User;

import static org.fest.assertions.Assertions.assertThat;

@TransactionConfiguration
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:JPA-repository-test-context.xml"})
public class UserRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private UserRepository userRepository;

    @Before
    public void before() {
        executeSqlScript("user-test-data.sql", true);
    }

    @Test
    public void getAll() {
        List<User> result = userRepository.getAll();
        assertThat(result).hasSize(2);
    }

    @Test
    public void save() {
        User user1 = new User("qwerty@uio.iop", "098765");
        User user2 = new User("asdfgh@jkl.lkj", "567890");

        assertThat(userRepository.save(user1, user2)).isEqualTo(2L);

        List<User> result = userRepository.getAll();
        assertThat(result).hasSize(4);
    }

    @Test
    public void update() {
        User user = userRepository.find(100L);
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(100L);
        assertThat(user.getUpdated()).isNull();

        user.setEmail("test@email.com");

        Optional<User> updated = userRepository.update(user);
        assertThat(updated.isPresent()).isTrue();

        User result = userRepository.find(100L);
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getUpdated()).isNotNull();
    }

    @Test
    public void delete() {
        userRepository.delete(200L);

        List<User> result = userRepository.getAll();
        assertThat(result).hasSize(1);
    }

    @Test
    public void find_failed() {
        User user = userRepository.find(300L);
        assertThat(user).isNull();
    }

    @Test
    public void getById_success() {
        Optional<User> user = userRepository.getById(100L);
        assertThat(user.isPresent()).isTrue();
    }

    @Test
    public void getById_failed() {
        Optional<User> user = userRepository.getById(300L);
        assertThat(user.isPresent()).isFalse();
    }

    @Test
    public void getByEmail_success() {
        String email = "aaa@bbb.ccc";
        Optional<User> user = userRepository.getByEmail(email);
        assertThat(user.isPresent()).isTrue();
        assertThat(user.get().getEmail()).isEqualTo(email);
    }

    @Test
    public void getByEmail_failed() {
        Optional<User> user = userRepository.getByEmail("wrong@email.yeh");
        assertThat(user.isPresent()).isFalse();
    }
}
