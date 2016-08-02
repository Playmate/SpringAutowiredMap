package com.igor.autowiredmap.business;

import com.igor.autowiredmap.api.MapKeyEnumerated;
import org.springframework.stereotype.Component;

/**
 * Created by Igor Dmitriev on 11/3/15
 */


@Component
@MapKeyEnumerated(key = LifeArea.SEA)
public class Seal implements Animal {
    @Override
    public String say() {
        return "ow ow ow";
    }
}
