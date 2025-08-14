package com.adelahmadi.springit.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class BeanUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        BeanUtil.setContext(applicationContext);
    }

    // This method is static to allow access from anywhere in the application
    // without needing an instance of BeanUtil.
    // It sets the static context field to the provided ApplicationContext.
    private static void setContext(ApplicationContext applicationContext) {
        BeanUtil.context = applicationContext;
    }

    // This method retrieves a bean of the specified type from the Spring
    // ApplicationContext.
    // It throws an IllegalStateException if the context is not initialized yet.
    // This method is static to allow access from anywhere in the application.
    public static <T> T getBean(Class<T> type) {
        ApplicationContext ctx = BeanUtil.context;
        if (ctx == null) {
            throw new IllegalStateException("Spring ApplicationContext is not initialized yet");
        }
        return ctx.getBean(type);
    }
}
