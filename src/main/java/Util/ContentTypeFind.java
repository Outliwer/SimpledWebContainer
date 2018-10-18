package Util;

import java.util.HashMap;

public class ContentTypeFind {

    private static HashMap<String, String> map = new HashMap<String, String>() {{
            map.put("html", "text/html");
            map.put("htm", "text/html");
        }
    };

    public static String findTheType(String fileName){
        if (fileName == null){
            return "text/html";
        }
        String[] helper = fileName.split(".");
        if (map.containsKey(helper[helper.length - 1])){
            return map.get(helper[helper.length - 1]);
        }
        return "text/html";
    }
}
