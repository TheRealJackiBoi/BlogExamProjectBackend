package dat3.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import dat3.dto.UserDTO;
import dat3.exception.AuthorizationException;

import java.text.ParseException;
import java.util.Date;


public class SignVerifyToken {

    private final String ISSUER, TOKEN_EXPIRE_TIME, SECRET_KEY;

    public SignVerifyToken(String ISSUER, String TOKEN_EXPIRE_TIME, String SECRET_KEY) {
        this.ISSUER = ISSUER;
        this.TOKEN_EXPIRE_TIME = TOKEN_EXPIRE_TIME;
        this.SECRET_KEY = SECRET_KEY;
    }

    public String signToken(String userName, String rolesAsString, Date date) throws JOSEException {
        JWTClaimsSet claims = createClaims(userName, rolesAsString, date);
        JWSObject jwsObject = createHeaderAndPayload(claims);
        return signTokenWithSecretKey(jwsObject);
    }

    private JWTClaimsSet createClaims(String username, String rolesAsString, Date date) {
        return new JWTClaimsSet.Builder()
                .subject(username)
                .issuer(ISSUER)
                .claim("username", username)
                .claim("roles", rolesAsString)
                .expirationTime(new Date(date.getTime() + Integer.parseInt(TOKEN_EXPIRE_TIME)))
                .build();
    }

    private JWSObject createHeaderAndPayload(JWTClaimsSet claimsSet) {
        return new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload(claimsSet.toJSONObject()));
    }

    private String signTokenWithSecretKey(JWSObject jwsObject) {
        try {
            JWSSigner signer = new MACSigner(SECRET_KEY.getBytes());
            jwsObject.sign(signer);
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Signing failed", e);
        }
    }

    public SignedJWT parseTokenAndVerify(String token) throws ParseException, JOSEException, AuthorizationException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());

        if (!signedJWT.verify(verifier)) {
            throw new AuthorizationException(401, "Invalid token signature");
        }
        return signedJWT;
    }

    public UserDTO getJWTClaimsSet(JWTClaimsSet claimsSet) throws AuthorizationException {

        if (new Date().after(claimsSet.getExpirationTime()))
            throw new AuthorizationException(401, "Token is expired");

        String username = claimsSet.getClaim("username").toString();
        String roles = claimsSet.getClaim("roles").toString();
        String[] rolesArray = roles.split(",");

        return new UserDTO(username, rolesArray);
    }

}