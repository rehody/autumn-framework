package org.autumnframework.infra;

public class Application {
    public static ApplicationContext run() {
        ApplicationContext context = ApplicationContext.getInstance();
        ObjectFactory factory = new ObjectFactory(context);
        context.setFactory(factory);
        return context;
    }

    private Application() {
    }
}
