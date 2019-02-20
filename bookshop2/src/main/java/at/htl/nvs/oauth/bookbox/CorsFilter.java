package at.htl.nvs.oauth.bookbox;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class CorsFilter implements ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        if (!containerRequestContext.getUriInfo().getPath().endsWith("-secured")){
            containerRequestContext.getHeaders().add("Access-Control-Allow-Origin", "*");
        }
    }
}
