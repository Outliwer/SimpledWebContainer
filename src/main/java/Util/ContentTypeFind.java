package Util;

import java.util.HashMap;

public class ContentTypeFind {

    private static String TEXT_HTML = "text/html";

    private static HashMap<String, String> map = new HashMap<String, String>();

    public static void initMap(){
        map.put("html", TEXT_HTML);
        map.put("htm", TEXT_HTML);
    }

    public static String findTheType(String fileName){
        if (fileName == null){
            return TEXT_HTML;
        }
        String[] helper = fileName.split("\\.");
        if (map.containsKey(helper[helper.length - 1])){
            return map.get(helper[helper.length - 1]);
        }
        return TEXT_HTML;
    }
}
