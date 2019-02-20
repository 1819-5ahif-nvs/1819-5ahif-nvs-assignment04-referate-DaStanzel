package at.htl.nvs.oauth.bookbox;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
import sun.font.AttributeValues;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@RequestScoped
public class AccessTokenProducer {

    @Inject
    private HttpServletRequest request;

    @Produces
    public AccessToken getAccessToken() {

        AccessToken p = ((KeycloakPrincipal) request.getUserPrincipal()).getKeycloakSecurityContext().getToken();
        return p;
    }
}
