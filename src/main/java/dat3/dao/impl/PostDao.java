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
            List<Post> posts = em.createQuery("SELECT p FROM Post p WHERE p.visibility = " + Visibility.PUBLIC, Post.class).getResultList();
            em.getTransaction().commit();
            return posts;
        }

    }
}
