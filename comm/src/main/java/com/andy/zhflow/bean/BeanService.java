package com.andy.zhflow.bean;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class BeanService implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanService.applicationContext =applicationContext;
    }

    public <T> T getClassBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    public Object getBean(String name){
        return applicationContext.getBean(name);
    }
}
