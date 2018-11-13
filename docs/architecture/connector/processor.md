### 处理器
***

![这里写图片描述](https://img-blog.csdn.net/20180412153025530?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM0ODYxMTAy/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
***
 - 在此前的描述中：`Connector`是一个`IO`多路复用来处理`socket`的架构，在对于具体的`socket`会存在着具体的**处理器**来进行**处理**。

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



