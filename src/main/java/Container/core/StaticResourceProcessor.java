package Container.core;

import Connector.request.HttpRequest;
import Connector.response.HttpResponse;

import java.io.IOException;

public class StaticResourceProcessor {
    public void process(HttpRequest httpRequest, HttpResponse httpResponse) {
        try {
            httpResponse.sendStaticResource();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
