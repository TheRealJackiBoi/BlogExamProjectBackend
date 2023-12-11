package dat3.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import dat3.config.ApplicationConfig;
import dat3.dto.UserDTO;
import dat3.exception.ApiException;
import dat3.exception.AuthorizationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenFactory {

    // Singleton
    private static TokenFactory instance;

    // Properties
    private final String ISSUER = Objects.requireNonNull(getProperties())[0];
    private final String TOKEN_EXPIRE_TIME = Objects.requireNonNull(getProperties())[1];
    private final String SECRET_KEY = Objects.requireNonNull(getProperties())[2];

    // Logger
    private final Logger LOGGER = LoggerFactory.getLogger(TokenFactory.class);

    // SignToken class
    private final SignVerifyToken signature = new SignVerifyToken(ISSUER, TOKEN_EXPIRE_TIME, SECRET_KEY);

    public static TokenFactory getInstance() {
        if (instance == null) {
            instance = new TokenFactory();
        }
        return instance;
    }

    // Get properties from pom file
    private String[] getProperties() {
        try {
            String[] properties = new String[3];
            properties[0] = ApplicationConfig.getProperty("issuer");
            properties[1] = ApplicationConfig.getProperty("token.expiration.time");
            properties[2] = ApplicationConfig.getProperty("secret.key");
            return properties;
        } catch (IOException e) {
            LOGGER.error("Could not get properties", e);
        }
        return null;
    }

    public String[] parseJsonObject(String jsonString, Boolean tryLogin) throws ApiException {
        try {
            List<String> roles = Arrays.asList("user", "admin", "manager");

            ObjectMapper mapper = new ObjectMapper();
            Map json = mapper.readValue(jsonString, Map.class);
            String username = json.get("username").toString();
            String password = json.get("password").toString();
            String role = "";

            if (!tryLogin) {
                role = json.get("role").toString();
                if (!roles.contains(role)) throw new ApiException(400, "Role not valid");
            }

            return new String[]{username, password, role};

        } catch (JsonProcessingException | NullPointerException e) {
            throw new ApiException(400, "Malformed JSON Supplied");
        }
    }

    public String createToken(String userName, Set<String> roles) throws ApiException {

        try {
            StringBuilder res = new StringBuilder();
            for (String string : roles) {
                res.append(string);
                res.append(",");
            }

            String rolesAsString = !res.isEmpty() ? res.substring(0, res.length() - 1) : "";

            Date date = new Date();
            return signature.signToken(userName, rolesAsString, date);
        } catch (JOSEException e) {
            throw new ApiException(500, "Could not create token");
        }
    }

    public UserDTO verifyToken(String token) throws ApiException, AuthorizationException {
        try {
            SignedJWT signedJWT = signature.parseTokenAndVerify(token);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            return signature.getJWTClaimsSet(claimsSet);
        } catch (ParseException | JOSEException e) {
            throw new ApiException(401, e.getMessage());
        }
    }
}