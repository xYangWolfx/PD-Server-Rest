package pt.isec.pd.server.rest.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

public class ClientConnection {
    public static boolean isAdmin(Authentication authentication) {
        Jwt userDetails = (Jwt) authentication.getPrincipal();
        String scope = userDetails.getClaim("scope");

        return scope.equals("ADMIN");
    }
}
