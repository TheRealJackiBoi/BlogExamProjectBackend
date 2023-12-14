package dat3.controller.impl;

import dat3.config.HibernateConfig;
import dat3.dao.impl.PostDao;
import dat3.dao.impl.UserDao;
import dat3.dto.PostDTO;
import dat3.exception.ApiException;
import dat3.model.Post;
import dat3.model.User;
import dat3.model.Visibility;
import dat3.routes.Routes;
import dat3.security.TokenFactory;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PostController {

    private PostDao postDao;
    private UserDao userDao;

    private final Logger LOGGER = LoggerFactory.getLogger(Routes.class);

    public PostController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        postDao = PostDao.getInstance(emf);
        userDao = UserDao.getInstance(emf);
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
            PostDTO postDto = ctx.bodyAsClass(PostDTO.class);
            if (postDto == null) {
                ctx.status(400);
                throw new ApiException(400, "Invalid post");
            }
            Post post = new Post(postDto);
            User user = userDao.read(User.class, postDto.getUsername());
            post.setUser(user);

            post = postDao.create(post);

            if (post == null) {
                ctx.status(400);
                throw new ApiException(400, "Couldn't create post");
            }

            ctx.status(201);
            ctx.json(PostDTO.convertToDto(post));
        };
    }

    public Handler getPostById() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Post post = postDao.read(Post.class, id);

            if (post == null) {
                ctx.status(404);
                throw new ApiException(404, "Post not found");
            }

            ctx.status(200);
            ctx.json(PostDTO.convertToDto(post));
        };
    }

    public Handler deletePostById() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Post post = postDao.read(Post.class, id);

            if (post == null) {
                ctx.status(404);
                throw new ApiException(404, "Post not found");
            }

            post.removeUser();

            if (post.getUser() != null) {
                LOGGER.info("Couldn't remove user from post");
                ctx.status(500);
                throw new ApiException(500, "Couldn't delete post");
            }
            LOGGER.info("User removed from post");

            postDao.delete(Post.class, post.getId());
            ctx.status(204);
        };
    }

    public Handler updatePostById() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Post post = postDao.read(Post.class, id);

            if (post == null) {
                ctx.status(404);
                throw new ApiException(404, "Post not found");
            }

            PostDTO postDto = ctx.bodyAsClass(PostDTO.class);
            if (postDto == null) {
                ctx.status(400);
                throw new ApiException(400, "Invalid post for update");
            }

            post.setTitle(postDto.getTitle());
            post.setContent(postDto.getContent());
            post.setVisibility(Visibility.valueOf(postDto.getVisibility()));

            post = postDao.update(post);

            if (post == null) {
                ctx.status(400);
                throw new ApiException(400, "Couldn't update post");
            }

            ctx.status(200);
            ctx.json(PostDTO.convertToDto(post));
        };
    }
}
