package Container.core;

import Connector.HttpRequest;
import Connector.HttpResponse;

import java.net.URLClassLoader;

public class ServletProcessor {
    public void process(HttpRequest httpRequest, HttpResponse httpResponse) {
        String uri = httpRequest.getRequestURI();
        String servletName = uri.substring(uri.lastIndexOf("/") + 1);
        URLClassLoader loader = null;
    }
}
