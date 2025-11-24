package org.autumnframework.infra.configurators;

public interface BootstrapProxyConfigurator {
    <T> T replaceWithProxy(T t, Class<T> impl);
}
