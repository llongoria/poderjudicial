package mx.com.wcontact.poderjudicial.filter;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Context;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import jakarta.ws.rs.container.ResourceInfo;



@Provider
public class CrossOriginResourceSharingFilter implements ContainerResponseFilter, Feature {

    private static final Logger log = Logger.getLogger(CrossOriginResourceSharingFilter.class.getName());

    @Context
    private ResourceInfo resinfo;


    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext response) throws IOException {
        response.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
        response.getHeaders().putSingle("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, OPTIONS");
        response.getHeaders().putSingle("Access-Control-Allow-Headers", "*");
        response.getHeaders().putSingle("Access-Control-Allow-Credentials", "true");
        String requestUri = requestContext.getUriInfo().getRequestUri().toString();
        log.logp(Level.INFO, CrossOriginResourceSharingFilter.class.getName(),"filter", "*** filter| Request[{0}] ***", requestUri );
        log.logp(Level.INFO, CrossOriginResourceSharingFilter.class.getName(),"filter", "*** filter| Status[{0},{1}], Headers[{2}] ***",new Object[] {response.getStatus() ,response.getStatusInfo().toString(), convertWithStream(response.getStringHeaders())}  );
    }

    @Override
    public boolean configure(FeatureContext context) {
        log.logp(Level.INFO, CrossOriginResourceSharingFilter.class.getName(),"configure", "*** configure| {0} ***", convertWithStream(context.getConfiguration().getProperties()) );
        return true;
    }

    public String convertWithStream(Map<String, ?> map) {
        return map.keySet().stream()
                .map(key -> key + "=" + map.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
    }
}



