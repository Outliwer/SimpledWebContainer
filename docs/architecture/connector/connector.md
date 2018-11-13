### 连接器
***

![这里写图片描述](https://img-blog.csdn.net/20180412153025530?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM0ODYxMTAy/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
***
 - 在此前的框架图中：`Connector`是一个`IO`多路复用来处理`socket`的架构
 
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

