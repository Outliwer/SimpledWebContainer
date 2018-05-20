package startup;

import Connector.HttpConnector;

public class Bootstrap {
    public static void main(String[] args){
        HttpConnector connector = new HttpConnector();
        connector.start();
    }
}
