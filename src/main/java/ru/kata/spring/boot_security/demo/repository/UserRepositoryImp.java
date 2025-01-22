package ru.kata.spring.boot_security.demo.repository;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImp implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findAll() {
        return entityManager.createQuery("select distinct u from User u join fetch u.roles", User.class)
                .getResultList();
    }

    @Override
    public void save(User user) {
        entityManager.persist(user);
    }

    @Override
    public void delete(long id) {
        getById(id).ifPresent(entityManager::remove);
    }

    @Override
    public void update(User user, long id) {
        User u = entityManager.find(User.class, id);
        u.setFirstName(user.getFirstName());
        u.setLastName(user.getLastName());
        u.setAge(user.getAge());
        u.setEmail(user.getEmail());
        u.setRoles(user.getRoles());
        if (!user.getPassword().isEmpty()) {
            u.setPassword(user.getPassword());
        }
        entityManager.flush();
    }

    @Override
    public Optional<User> getById(long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return entityManager.createQuery("select u from User u join fetch u.roles where u.email = :email", User.class)
                .setParameter("email", email)
                .getResultStream()
                .findAny();
    }
}
