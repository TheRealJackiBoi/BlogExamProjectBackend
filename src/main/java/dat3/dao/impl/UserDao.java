package dat3.dao.impl;

import dat3.dao.CRUDDao;
import dat3.exception.AuthorizationException;
import dat3.model.Role;
import dat3.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class UserDao extends CRUDDao<User, String> {

    private static UserDao instance;

    public static UserDao getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new UserDao();
            instance.setEntityManagerFactory(emf);
        }
        return instance;
    }

    public User getVerifiedUser(String username, String password) throws AuthorizationException {

        try (EntityManager em = getEmf().createEntityManager()) {
            em.getTransaction().begin();
            User user = em.find(User.class, username);

            if (user == null || !user.verifyPassword(password)) {
                throw new AuthorizationException(401, "Invalid user name or password");
            }
            em.getTransaction().commit();
            return user;
        }
    }

    public User registerUser(String username, String password, String user_role) throws AuthorizationException {

        try (EntityManager em = getEmf().createEntityManager()) {
            em.getTransaction().begin();

            User user = new User(username, password);
            Role role = em.find(Role.class, user_role);

            if (role == null) {
                role = createRole(user_role);
            }

            user.addRole(role);
            em.persist(user);
            em.getTransaction().commit();
            return user;
        } catch (Exception e) {
            throw new AuthorizationException(400, "Username already exists");
        }
    }

    public Role createRole(String role) {
        try (EntityManager em = getEmf().createEntityManager()) {
            em.getTransaction().begin();
            Role newRole = new Role(role);
            em.persist(newRole);
            em.getTransaction().commit();
            return newRole;
        }
    }

    public List<String> getAllUsernames() {
        try (EntityManager em = getEmf().createEntityManager()) {
            em.getTransaction().begin();
            List<String> usernames = em.createQuery("SELECT u.username FROM User u", String.class).getResultList();
            em.getTransaction().commit();
            return usernames;
        }
    }

    public List<String> searchForUsernamesTop10(String searchTerm) {
        try (EntityManager em = getEmf().createEntityManager()) {
            em.getTransaction().begin();
            List<String> usernames = em.createQuery("SELECT u.username FROM User u " +
                            "WHERE u.username LIKE :searchterm " +
                            "ORDER BY LENGTH(u.username) - LENGTH(:searchterm) ASC", String.class)
                    .setParameter("searchterm", "%" + searchTerm + "%")
                    .setMaxResults(10)
                    .getResultList();
            em.getTransaction().commit();
            return usernames;
        }
    }
}
