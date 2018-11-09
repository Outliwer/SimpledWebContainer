### `servlet` **大体架构**
***

大致框架：
随着进一步的深入会不断进行修改，目前的意向的版本框架如下：

![这里写图片描述](https://img-blog.csdn.net/20180412153025530?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM0ODYxMTAy/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
***

**大致工作内容：**

 - 创建一个 `request` 对象
	 - 填充可能被所引用的 `servlet` 使用的信息，如参数、头部、`URI` 等
	 - `HTTP` 请求
		 - 方法--`URI`--协议/版本
		 - 请求的头部
			 - 每一个头部通过一个`CRLF`来分割 
		 - 主体内容
			 - 头部和主体内容之间有一个 `CRLF`
 
 - 创建一个 `response` 对象
	 - 所引用的 `servlet`  使用它来给客户端发送响应
	 -  `HTTP` 响应
		 - 方法--`URI`--协议/版本
		 - 响应的头部
		 - 主体内容
	 
 - 调用 `servlet` 的 `service` 方法
	 - 这个是 `servlet` 自带的五个主要方法中的一个，也是 `servlet` 生命周期的第二阶段：主要是从`request` 对象取值然后给`response`对象写值
	 - 生命周期方法：`init`  -> `service` -> `destroy`。也代表了`servlet` 的声明周期，从初始化到有请求的时候的`service`，最后释放`servlet`（一般发生在`servlet`容器关闭或者需要更多的空闲内存时候）
***
**大致的工作流程：**

 - 等待`HTTP`请求
 - 对每次请求，构造一个`request`和`response`的实例
 - 当调用`servlet`的时候，加载该`servlet`类并调用`servlet`的`init` 方法（**仅一次**），传递`request`和`response`
 - 当调用的是静态资源，则调用`StaticResourceProcessor`，也需要传递`request`和`response`
 - `servlet`的销毁

***
一个`Servlet`容器中的一个`Web`服务器：

![这里写图片描述](https://img-blog.csdn.net/20180412160506176?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM0ODYxMTAy/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)




