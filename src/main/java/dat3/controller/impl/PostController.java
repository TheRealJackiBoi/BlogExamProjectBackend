package dat3.controller.impl;

import dat3.config.HibernateConfig;
import dat3.dao.impl.PostDao;
import dat3.dao.impl.UserDao;
import dat3.dto.PostDTO;
import dat3.exception.ApiException;
import dat3.model.Post;
import dat3.security.TokenFactory;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class PostController {

    private PostDao postDao;

    public PostController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        postDao = PostDao.getInstance(emf);
    }

    public Handler getAllPublicPosts() {
        return ctx -> {
            List<Post> posts = postDao.getAllPublicPosts();

            if (posts.isEmpty()) {
                ctx.status(404);
                throw new ApiException(404, "No posts found");
            }

            List<PostDTO> postDtos = PostDTO.convertToDto(posts);

            ctx.status(200);
            ctx.json(postDtos);
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
