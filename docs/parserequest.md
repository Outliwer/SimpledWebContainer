### `HttpRequest` 的解析
***
对于每一个`HttpRequest` 要使用一个`HttpProcessor`用来进行解析，而对于`HttpProcessor`首先要完成对于传入参数的解析

这里实现一个**简易的解析**
***

 - 首先自己写一个打印流的函数，观察输入流中的内容：


```
    public static void print (InputStream is) throws UnsupportedEncodingException{
        InputStreamReader isr =new InputStreamReader(is,"utf-8");
        BufferedReader br = new BufferedReader(isr);
        try {
            int index = 0;
            while (index < 100) {
                System.out.println(br.readLine());
                index ++;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    } 
```

 - 得到内容如下：

```
GET /index.html?user=outlier HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Cache-Control: max-age=0
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
Accept-Encoding: gzip, deflate, br
Accept-Language: zh-CN,zh;q=0.9,en;q=0.8
Cookie: Idea-c03939ae=62b06f69-204a-4fa1-8f13-0c23db6987dd; UM_distinctid=162ec462706246-04cad68b914a44-336e7b05-fa000-162ec46270921b; CNZZDATA1264021086=2075159346-1524378005-%7C1524583190


```


***
## 开始对内容进行解析 ##

 - 新建一个`parseRequest`方法 （这里暂定使用类似于**解释器中的`Map`**进行存储得到解析的值）
	 

```
//常规参数Map
private Map<String,String> paramMap = new HashMap<String, String>();
//在URI中使用的参数列表
private Map<String,String> uriParamMap = new HashMap<String, String>();
```

 - 进行分析：这里使用**递归子程序的方法**进行解析
	 - 仿照之前的方法，先转化成`char`类型的数组
		

	```
	public char[] convertStreamToCharArray(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + " ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString().toCharArray();
    }
	```
	 - 然后进行**递归子程序的方法** ，这里先实现的**第一行的解析**：
	 
	
	```
	package Connector;
	
	import java.io.BufferedReader;
	import java.io.IOException;
	import java.io.InputStream;
	import java.io.InputStreamReader;
	import java.util.HashMap;
	import java.util.Map;
	
	public class HttpRequest {
	
	    //the param
	    private Map<String,String> paramMap = new HashMap<String, String>();
	    //the param in the URI
	    private Map<String,String> uriParamMap = new HashMap<String, String>();
	    //the tokens of the inputStream
	    char[] tokens;
	    //the offset of the tokens
	    int currentToken = -1;
	    //the value of the token
	    char current;
	
	    /*
	     * get the tokens
	     */
	    public HttpRequest(InputStream inputStream) {
	        tokens = convertStreamToCharArray(inputStream);
	    }
	
	    /*
	     * get the URI
	     */
	    public String getRequestURI() {
	        System.out.println("paramMap.size() " + paramMap.size());
	        return paramMap.get("requestURI");
	    }
	
	    /*
	     * the next token
	     */
	    private void nextToken(){
	        if (currentToken < tokens.length){
	            currentToken ++;
	            current = tokens[currentToken];
	        } else {
	            // TODO: error message need
	            System.out.println("the end of the tokens");
	        }
	    }
	    /*
	     * parse
	     */
	    public void parseRequest(){
	        if (tokens == null || tokens.length == 0){
	            // TODO: error message need
	            System.out.println("the input is null");
	            return;
	        }
	        // start
	        nextToken();
	        if (current == 'G'){
	            parseGetMethod();
	        } else if (current == 'P'){
	
	        } else {
	            // TODO: error message need
	            System.out.println("the input is wrong");
	            return;
	        }
	    }
	
	    /*
	     * parse Get
	     */
	    private void parseGetMethod(){
	        nextToken();
	        if (current == 'E'){
	            nextToken();
	            if (current == 'T'){
	                paramMap.put("METHOD","GET");
	                // TODO: current message need
	                System.out.println("the method : get");
	                parseURI();
	            } else {
	                // TODO: error message need
	                System.out.println("the method is wrong");
	            }
	        } else {
	            // TODO: error message need
	            System.out.println("the method is wrong");
	        }
	    }
	
	
	    /*
	     * parse URI
	     */
	    private void parseURI(){
	        nextToken();
	        while (current == ' ' || current == '\t'){
	            nextToken();
	        }
	        if (current != '/'){
	            // TODO: error message need
	            System.out.println("there need a '/' , The URI is wrong");
	        } else {
	            StringBuilder sb = new StringBuilder();
	            nextToken();
	            while (current != '?' && current != ' '){
	                sb.append(current);
	                nextToken();
	            }
	            paramMap.put("requestURI", String.valueOf(sb));
	            // TODO: current message need
	            System.out.println("the requestURI :" + String.valueOf(sb));
	            if (current == ' '){
	                // There is no param in the request Param
	                // TODO: current message need
	                System.out.println("There is no param in the request Param");
	                parseHttpVersion();
	            } else {
	                // TODO: current message need
	                System.out.println("There is some params in the request Param");
	                parseParamUriRequest();
	                System.out.println("params have been recorded");
	                parseHttpVersion();
	            }
	        }
	    }
	
	    /*
	     * parse the param of the uriRequest
	     */
	    private void parseParamUriRequest(){
	        StringBuilder sb_key = new StringBuilder();
	        StringBuilder sb_value = new StringBuilder();
	        nextToken();
	        while (current != '='){
	            sb_key.append(current);
	            nextToken();
	        }
	        nextToken();
	        while (current != '&' && current != ' '){
	            sb_value.append(current);
	            nextToken();
	        }
	        uriParamMap.put(String.valueOf(sb_key),String.valueOf(sb_value));
	        // TODO: current message need
	        System.out.println(" params in the URI :  " +String.valueOf(sb_key)+ " : " +String.valueOf(sb_value));
	        if (current == '&'){
	            parseParamUriRequest();
	        }
	    }
	
	    /*
	     * parse the version of the Http
	     */
	    private void parseHttpVersion(){
	        nextToken();
	        while (current == ' ' || current == '\t'){
	            nextToken();
	        }
	        if (current == 'H'){
	            StringBuilder sb_key = new StringBuilder();
	            StringBuilder sb_value = new StringBuilder();
	            while (current != '/'){
	                sb_key.append(current);
	                nextToken();
	            }
	            nextToken();
	            while (current != '\n' && current != '\t' && current != ' '){
	                sb_value.append(current);
	                nextToken();
	            }
	            paramMap.put(String.valueOf(sb_key),String.valueOf(sb_value));
	            // TODO: current message need
	            System.out.println(" HTTP Version : " +String.valueOf(sb_key)+ " : " +String.valueOf(sb_value));
	            nextToken();
	            // TODO: 下一次分析开始  H
	//            System.out.println(current);
	        }
	    }
	
	    /*
	     * change the inputStream to the CharArray
	     */
	    private char[] convertStreamToCharArray(InputStream is) {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	        StringBuilder sb = new StringBuilder();
	        String line = null;
	        try {
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + ' ');
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                is.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        return sb.toString().toCharArray();
	    }
	
	}
	
	```
****
**运行结果：**



    ![这里写图片描述](https://img-blog.csdn.net/20180624154139697?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM0ODYxMTAy/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)


