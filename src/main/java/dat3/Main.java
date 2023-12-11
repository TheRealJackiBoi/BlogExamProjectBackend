package dat3;

import dat3.config.ApplicationConfig;
import io.javalin.Javalin;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ApplicationConfig
                .startServer(
                        Javalin.create(),
                        Integer.parseInt(ApplicationConfig.getProperty("javalin.port")));
    }
}