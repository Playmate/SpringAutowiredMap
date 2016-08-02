package com.igor.autowiredmap.business;

import com.igor.autowiredmap.api.AutowiredMap;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Igor Dmitriev on 8/2/16
 */
@Component
public class WoodlandParkZoo implements Zoo {

    @AutowiredMap
    private Map<LifeArea, Animal> animals;

    @Override
    public String say(LifeArea lifeArea) {
        return animals.get(lifeArea).say();
    }
}
