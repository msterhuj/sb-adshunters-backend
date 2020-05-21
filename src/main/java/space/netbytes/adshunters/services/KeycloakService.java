package space.netbytes.adshunters.services;

import com.sun.istack.Nullable;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Service
public class KeycloakService {

    @Autowired
    private HttpServletRequest request;

    public KeycloakAuthenticationToken getToken() {
        return (KeycloakAuthenticationToken) request.getUserPrincipal();
    }

    public KeycloakPrincipal getPrincipal() {
        return (KeycloakPrincipal) getToken().getPrincipal();
    }

    public KeycloakSecurityContext getSession() {
        return getPrincipal().getKeycloakSecurityContext();
    }

    public AccessToken getAccessToken() {
        return getSession().getToken();
    }

    public UUID getUUID() {
        return UUID.fromString(getAccessToken().getSubject());
    }

    public boolean hasPermission(@Nullable UUID uuid) { // todo add permission check for admin
        return uuid == getUUID();// || getSession().getAuthorizationContext().hasScopePermission("ADMIN");
    }
}
