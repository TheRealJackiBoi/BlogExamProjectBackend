package dat3.dao.impl;

import dat3.dao.CRUDDao;
import dat3.model.Post;
import dat3.model.Visibility;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class PostDao extends CRUDDao<Post, Integer> {

    private static PostDao instance;

    private PostDao() {}

    public static PostDao getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new PostDao();
            instance.setEntityManagerFactory(emf);
        }
        return instance;
    }

    public List<Post> getAllPublicPosts() {

        try (EntityManager em = getEmf().createEntityManager()) {
            em.getTransaction().begin();
            List<Post> posts = em.createQuery("SELECT p FROM Post p WHERE p.visibility = :visibility ORDER BY p.createdAt DESC ", Post.class)
                    .setParameter("visibility", Visibility.PUBLIC)
                    .getResultList();
            em.getTransaction().commit();
            return posts;
        }

    }

    // Create a method getllPostsByUsername that returns a list of all posts with the specified username (FK) user_user_name

    public List<Post> getAllPostsByUsername(String username) {

        try (EntityManager em = getEmf().createEntityManager()) {
            em.getTransaction().begin();
            List<Post> posts = em.createQuery("SELECT p FROM Post p WHERE p.user.username = :username ORDER BY p.createdAt DESC ", Post.class)
                    .setParameter("username", username)
                    .getResultList();
            em.getTransaction().commit();
            return posts;
        }

    }
}
