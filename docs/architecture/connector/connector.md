### 连接器
***

![这里写图片描述](https://img-blog.csdn.net/20180412153025530?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM0ODYxMTAy/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
***
 - 在此前的框架图中：`Connector`是一个`servlet`容器进行`request`和`response`的接口
 
 - 因此先从`Connector`的实现开始`servlet`容器的实现

***

`HttpConnector`类的实现：

 - 对一个`servlet`容器来说首先应该设置成多线程
 - 对于每一个线程要做的：
	 - 等待`HTTP`请求
		 - 创建一个服务器套接字用来等待`HTTP`请求
	 - 为每一个请求创建支撑类（`HttpProcessor`）
		 - 在服务器套接字`accept`的时候调用
	 - 调用支撑类中的方法（`process`）

按照以上要求的代码：


```
package Connector;

import Connector.Process.HttpProcessor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpConnector implements Runnable {

    volatile boolean stop = false;

    public void run() {
        ServerSocket serverSocket = null;
        int port = 8080;
        try {
            serverSocket = new ServerSocket(port,1, InetAddress.getByName("127.0.0.1"));
        }catch (IOException e){
            e.printStackTrace();
        }
        while (!stop){
            //waiting the request
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (Exception e){
                continue;
            }
            HttpProcessor httpRequestProcess = new HttpProcessor(this);
            httpRequestProcess.process(socket);
        }
    }
    public void start(){
        Thread thread = new Thread(this);
        thread.start();
    }
}
```


***
`HttpProcessor`类的实现：

 - `process`
	 - 传入了一个`Servlet`类的对象
	 - 通过其进行`HTTP`请求的解析
		 - 获得套接字的输入流（关于此处流可以参见`JavaIO`：）
		 - 创建一个`HttpRequest`实例
	 - 并且完成`HTTP`响应的创建
		 - 获得套接字的输出流
		 - 创建一个`HttpResponse`实例
		 - 创建**内容**：
			 - 方法–URI–协议/版本
			 - 响应的头部
             - 主体内容
	 - 对于请求又可以分成静态资源请求和动态`servlet`
		 - 对应两种不同的类又可以分别对应类`ServletProcessor` 、`StaticResourceProcessor`

按照以上要求的代码：


```
package Connector.Process;
import Connector.HttpConnector;
import Connector.HttpRequest;
import Connector.HttpResponse;
import Container.ServletProcessor;
import Container.StaticResourceProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpProcessor {

    private  HttpConnector httpConnector = null;
    private HttpRequest httpRequest = null;
    private HttpResponse httpResponse = null;

    public HttpProcessor(HttpConnector httpConnector) {
        this.httpConnector = httpConnector;
    }

    public void process(Socket socket) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            // create httprequest
            httpRequest = new HttpRequest(inputStream);

            // create httpresponse
            httpResponse = new HttpResponse(outputStream);
            // there need some functions to finish the httpResponse
            httpResponse.setRequest(httpRequest);

            //check whether the request is a static source
            if (httpRequest.getRequestURI().startsWith("/servlet/")){
                ServletProcessor processor = new ServletProcessor();
                processor.process(httpRequest, httpResponse);
            } else {
                StaticResourceProcessor processor = new StaticResourceProcessor();
                processor.process(httpRequest, httpResponse);
            }

            //close the sotket
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```



