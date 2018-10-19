package startup;

import Connector.HttpConnector;
import Util.ContentTypeFind;

public class Bootstrap {
    public static void main(String[] args){
        ContentTypeFind.initMap();
        HttpConnector connector = new HttpConnector();
        connector.start();
    }
}
