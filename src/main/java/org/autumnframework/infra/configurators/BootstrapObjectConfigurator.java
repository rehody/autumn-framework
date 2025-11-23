package org.autumnframework.infra.configurators;

import org.autumnframework.infra.ApplicationContext;

public interface BootstrapObjectConfigurator {
    void configure(Object o, ApplicationContext context);
}
