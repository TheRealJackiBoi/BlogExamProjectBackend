package dat3.controller.impl;

import dat3.exception.ApiException;
import dat3.exception.AuthorizationException;
import dat3.exception.Message;
import dat3.exception.ValidationMessage;
import dat3.routes.Routes;
import io.javalin.http.Context;
import io.javalin.validation.ValidationError;
import io.javalin.validation.ValidationException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ExceptionController {
    private final Logger LOGGER = LoggerFactory.getLogger(Routes.class);
    public void exceptionHandlerNotAuthorized(AuthorizationException e, Context ctx) {
        LOGGER.error(ctx.attribute("requestInfo") + " " + ctx.res().getStatus() + " " + e.getMessage());
        ctx.status(e.getStatusCode());
        ctx.json(new Message(e.getStatusCode(), e.getMessage()));
    }

    public void validationExceptionHandler(ValidationException e, Context ctx) {
        LOGGER.error(ctx.attribute("requestInfo") + " " + ctx.res().getStatus() + " " + ctx.body());

        Map<String, List<ValidationError<Object>>> errors = e.getErrors();
        List<ValidationError<Object>> errorList = new ArrayList<>();
        int statusCode = 0;

        if (errors.containsKey("id")) {
            errorList = errors.get("id");
            statusCode = 404;
        }

        if (errors.containsKey("REQUEST_BODY")) {
            errorList = errors.get("REQUEST_BODY");
            statusCode = 400;
        }

        String message = errorList.get(0).getMessage();
        Map<String, Object> args = errorList.get(0).getArgs();
        Object value = errorList.get(0).getValue();

        ctx.status(statusCode);
        ctx.json(new ValidationMessage(message, args, value));
    }

    public void constraintViolationExceptionHandler(ConstraintViolationException e, Context ctx) {
        LOGGER.error(ctx.attribute("requestInfo") + " " + ctx.res().getStatus() + " " + ctx.body());
        ctx.status(500);
        ctx.json(new Message(e.getErrorCode(), e.getSQLException().getMessage()));
    }

    public void apiExceptionHandler(ApiException e, Context ctx) {
        LOGGER.error(ctx.attribute("requestInfo") + " " + ctx.res().getStatus() + " " + e.getMessage());
        ctx.status(e.getStatusCode());
        ctx.json(new Message(e.getStatusCode(), e.getMessage()));
    }

    public void exceptionHandler(Exception e, Context ctx) {
        LOGGER.error(ctx.attribute("requestInfo") + " " + ctx.res().getStatus() + " " + e.getMessage());

        System.out.println("======================================");
        System.out.println(Arrays.toString(e.getStackTrace()));
        System.out.println("======================================");

        ctx.status(500);
        ctx.json(new Message(500, e.getMessage()));
    }
}