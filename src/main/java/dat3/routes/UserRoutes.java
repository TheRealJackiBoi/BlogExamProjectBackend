package dat3.routes;

import dat3.controller.impl.UserController;
import dat3.security.RouteRoles;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class UserRoutes {
    private final UserController userController = new UserController();

    protected EndpointGroup getRoutes() {

        return () -> {
            path("/auth", () -> {
                post("/login", userController::login, RouteRoles.ANYONE);
                post("/register", userController::registerUser, RouteRoles.ANYONE);
                path("/admin", () -> {
                    post("/register", userController::register, RouteRoles.ADMIN, RouteRoles.MANAGER);
                });
            });
            path("/users", () -> {
                get("/", userController::getAllUsernames, RouteRoles.USER, RouteRoles.ADMIN, RouteRoles.MANAGER);
                get("/search/{searchterm}", userController::searchForUserTop10, RouteRoles.USER, RouteRoles.ADMIN, RouteRoles.ADMIN);
            });
        };
    }
}