package dat3.controller.impl;

import dat3.config.HibernateConfig;
import dat3.dao.impl.PostDao;
import dat3.dao.impl.UserDao;
import dat3.security.TokenFactory;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManagerFactory;

public class PostController {

    private PostDao postDao;

    public PostController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        postDao = PostDao.getInstance(emf);
    }

    public Handler getAllPublicPosts() {
        return ctx -> {

        };
    }

    public Handler createPost() {
        return ctx -> {


        };
    }

    public Handler getPostById() {
        return ctx -> {


        };
    }

    public Handler deletePostById() {
        return ctx -> {


        };
    }

    public Handler updatePostById() {
        return ctx -> {


        };
    }
}
