package dat3.dao.impl;

import dat3.dao.CRUDDao;
import dat3.model.Post;
import jakarta.persistence.EntityManagerFactory;

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


}
