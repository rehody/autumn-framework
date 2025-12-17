package org.autumnframework.infra;

public class Application {

    private Application() {
    }

    public static ApplicationContext run() {
        ApplicationContext context = new ApplicationContext();
        ComponentFactory factory = new ComponentFactory(context);
        context.setFactory(factory);
        return context;
    }
}
