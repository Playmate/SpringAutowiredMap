package com.igor.autowiredmap.api;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by Igor Dmitriev on 11/22/15
 */

@Component
public class AutowiredMapBeanPostProcessor implements BeanPostProcessor, Ordered {

    @Autowired
    private ConfigurableListableBeanFactory factory;

    private Map<String, Object> beans = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        streamOfAutowiredMap(bean).forEach(field -> beans.put(beanName, bean));
        return bean;
    }

    private Stream<Field> streamOfAutowiredMap(Object bean) {
        return Arrays.stream(bean.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(AutowiredMap.class));
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Object object = beans.get(beanName);
        if (object != null) {
            streamOfAutowiredMap(object).forEach(field -> {
                Map<Object, Object> autowiredMap = createAutowiredMap(object.getClass().getName(), field);
                field.setAccessible(true);
                ReflectionUtils.setField(field, object, autowiredMap);
            });
        }
        return bean;
    }

    private Map<Object, Object> createAutowiredMap(String ownerClassName, Field field) {
        Map<Object, Object> autowiredMap = new HashMap<>();

        ParameterizedType type = (ParameterizedType) field.getGenericType();
        Type value = type.getActualTypeArguments()[1];
        try {
            Class<?> classType = Class.forName(value.getTypeName());
            for (Map.Entry<String, ?> entry : factory.getBeansOfType(classType).entrySet()) {
                ScannedGenericBeanDefinition beanDefinition = (ScannedGenericBeanDefinition) factory.getBeanDefinition(entry.getKey());
                Map<String, Object> annotationAttributes = beanDefinition.getMetadata().getAnnotationAttributes(MapKeyEnumerated.class.getTypeName());
                if (annotationAttributes == null) {
                    continue;
                }
                Object key = annotationAttributes.get("key");
                Object oldBean = autowiredMap.put(key, entry.getValue());
                checkIfBeanAlreadyExists(entry, key, oldBean, ownerClassName, field.getName());
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return autowiredMap;
    }

    private void checkIfBeanAlreadyExists(Map.Entry<String, ?> entry, Object key, Object oldBean, String ownerClassName, String fieldName) {
        boolean beanAlreadyExistInMap = oldBean != null;
        if (beanAlreadyExistInMap) {
            String msg = "Could not autowire field %s.%s, two different beans exist for the same key: %s, beans: %s, %s";
            throw new IllegalStateException(String.format(msg, ownerClassName, fieldName, key,
                    getBeanName(oldBean.getClass()), getBeanName(entry.getValue().getClass())));
        }
    }

    private String getBeanName(Class clazz) {
        return factory.getBeanNamesForType(clazz)[0];
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
