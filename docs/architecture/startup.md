### 启动类
***

![这里写图片描述](https://img-blog.csdn.net/20180412153025530?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM0ODYxMTAy/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

* 对于`web`容器，使用一个用于监控`servlet`请求的**启动类**启动容器即可

    具体的方法：调用**连接器**方法即可：
    
    
    ```
    package startup;
    
    import Connector.HttpConnector;
    
    public class Bootstrap {
        public static void main(String[] args){
            HttpConnector connector = new HttpConnector();
            connector.start();
        }
    }
    
    ```


