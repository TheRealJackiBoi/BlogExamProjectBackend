package dat3.routes;

import dat3.controller.impl.PostController;
import dat3.security.RouteRoles;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class PostRoutes {

    private final PostController postController = new PostController();

    public EndpointGroup getRoutes() {
        return () -> {
             path("/posts", () -> {
                 get("/", postController.getAllPublicPosts(), RouteRoles.USER, RouteRoles.ADMIN, RouteRoles.MANAGER);
                 post("/", postController.createPost(), RouteRoles.USER, RouteRoles.ADMIN, RouteRoles.MANAGER);
                 path("/{id}", () -> {
                     get("/", postController.getPostById(), RouteRoles.USER, RouteRoles.ADMIN, RouteRoles.MANAGER);
                     delete("/", postController.deletePostById(), RouteRoles.USER, RouteRoles.ADMIN, RouteRoles.MANAGER);
                     put("/", postController.updatePostById(), RouteRoles.USER, RouteRoles.ADMIN, RouteRoles.MANAGER);
                     put("/likes", postController.updateLikesById(), RouteRoles.USER, RouteRoles.ADMIN, RouteRoles.MANAGER);
                 });
                 path("/user", () -> {
                     get("/{username}", postController.getAllPostsByUsername(), RouteRoles.USER, RouteRoles.ADMIN, RouteRoles.MANAGER);
                 });
             });
        };
    }
}
