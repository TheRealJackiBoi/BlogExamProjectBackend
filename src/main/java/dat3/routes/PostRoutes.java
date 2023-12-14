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
                 get("/", ctx -> postController.getAllPublicPosts(), RouteRoles.USER, RouteRoles.ADMIN, RouteRoles.MANAGER);
                 post("/", ctx -> postController.createPost(), RouteRoles.USER, RouteRoles.ADMIN, RouteRoles.MANAGER);
                 path("/:id", () -> {
                     get("/", ctx -> postController.getPostById(), RouteRoles.USER, RouteRoles.ADMIN, RouteRoles.MANAGER);
                     delete("/", ctx -> postController.deletePostById(), RouteRoles.USER, RouteRoles.ADMIN, RouteRoles.MANAGER);
                     put("/", ctx -> postController.updatePostById(), RouteRoles.USER, RouteRoles.ADMIN, RouteRoles.MANAGER);
                 });
             });
        };
    }
}
