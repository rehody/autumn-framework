package org.autumnframework.infra.configurators;


import org.autumnframework.annotations.Log;

import java.lang.reflect.Proxy;
import java.util.Arrays;

@SuppressWarnings("unchecked")
public class LogAnnotationBootstrapProxyConfigurator implements BootstrapProxyConfigurator {
    @Override
    public <T> T replaceWithProxy(T t, Class<T> impl) {
        if (impl.isAnnotationPresent(Log.class) && impl.getInterfaces().length > 0) {
            return getJdkProxy(t, impl);
        }
        return t;
    }

    private <T> T getJdkProxy(T t, Class<T> impl) {
        return (T) Proxy.newProxyInstance(
                impl.getClassLoader(), impl.getInterfaces(),
                (proxy, method, args) -> {
                    String message = "method '" + method.getName() +
                            "' invoked with args: " + Arrays.toString(args) +
                            ", by: " + impl.getName();

                    System.out.println(message);
                    return method.invoke(t, args);
                });
    }
}
