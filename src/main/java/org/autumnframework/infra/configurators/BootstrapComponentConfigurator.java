package org.autumnframework.infra.configurators;

import org.autumnframework.infra.ApplicationContext;

public interface BootstrapComponentConfigurator {
    void configure(Object o, ApplicationContext context);
}
