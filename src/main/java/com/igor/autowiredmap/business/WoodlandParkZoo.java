package com.igor.autowiredmap.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Igor Dmitriev on 8/2/16
 */
@Component
public class WoodlandParkZoo implements Zoo {

    private final Map<LifeArea, Animal> animals = new HashMap<>();

    @Autowired
    public WoodlandParkZoo(@Qualifier("lion") Animal lion, @Qualifier("seal") Animal seal) {
        animals.put(LifeArea.DESERT, lion);
        animals.put(LifeArea.SEA, seal);
    }

    @Override
    public String say(LifeArea lifeArea) {
        String say = animals.get(lifeArea).say();
        if (say == null) {
            throw new IllegalArgumentException("There is no animal in the zoo");
        }
        return say;
    }
}
