package com.igor.autowiredmap.api;

import com.igor.autowiredmap.business.LifeArea;
import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Igor Dmitriev on 11/3/15
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.TYPE, ElementType.METHOD})
@Qualifier
public @interface MapKeyEnumerated {
    LifeArea key();
}
