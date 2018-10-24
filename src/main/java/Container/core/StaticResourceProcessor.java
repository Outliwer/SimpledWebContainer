package Container.core;

import Connector.HttpRequest;
import Connector.HttpResponse;

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
