package com.zhouxiaoge.dag.annotation;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 周小哥
 * @date 2021年01月25日 23点10分
 */

@Component
public class BuiltNodeTypeList implements ApplicationContextAware {

    public static final List<Object> list = new ArrayList<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = applicationContext.getBean(beanDefinitionName);
            NodeType annotation = bean.getClass().getAnnotation(NodeType.class);
            if (null == annotation) {
                continue;
            }
            list.add(bean);
        }
    }
}
