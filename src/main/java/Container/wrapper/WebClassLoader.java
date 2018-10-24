package Container.wrapper;

public class WebClassLoader {
    /**
     * function:
     * create a classloader
     * set the repositories
     * set the classpath
     * set the permissions
     * set a new thread for auto-reload
     */

    /**
     * the loaderClass
     */
    private String loaderClass;
    /**
     * create a classloader
     * @return
     */
    private WebClassLoader createClassLoader() throws Exception{
        WebClassLoader classLoader = null;
        Class clazz = Class.forName(loaderClass);
        return classLoader;
    }


}
