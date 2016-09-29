package mobi.visited.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import mobi.visited.model.User;

@Repository
public class UserRepository {

    private static final Logger LOG = LoggerFactory.getLogger(UserRepository.class);

    @PersistenceContext(unitName = "mobi.visited")
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<User> getAll() {
        return entityManager.createQuery("FROM User", User.class).getResultList();
    }

    @Transactional
    public Long save(User... entities) {
        for (User user : entities) {
            LOG.info("Storing new user to database...");
            entityManager.persist(user);
            LOG.info("Stored new user, set entity's id to {}", user.getId());
        }
        return (long) entities.length;
    }

    @Transactional
    public Optional<User> update(User entity) {
        entity.setUpdated(LocalDateTime.now());
        return Optional.ofNullable(entityManager.merge(entity));
    }

    @Transactional
    public void delete(Long id) {
        LOG.info("Deleting user from database...");
        entityManager.remove(entityManager.find(User.class, id));
        LOG.info("Deleted user with id {} from database", id);
    }

    @Transactional(readOnly = true)
    public User find(Long id) {
        return entityManager.find(User.class, id);
    }

    @Transactional(readOnly = true)
    public Optional<User> getById(Long id) {
        try {
            return Optional.ofNullable(entityManager.find(User.class, id));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Transactional(readOnly = true)
    public Optional<User> getByEmail(String email) {
        List<User> values = entityManager.createQuery("FROM User WHERE email = :email", User.class)
            .setParameter("email", email)
            .getResultList();
        if (values.size() == 1) {
            return Optional.of(values.get(0));
        }
        return Optional.empty();
    }
}
