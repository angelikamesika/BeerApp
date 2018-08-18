package com.example.angelika.beerapp.utils;

import android.util.Log;

import com.example.angelika.beerapp.model.Input;
import com.example.angelika.beerapp.model.Restaurant;
import com.example.angelika.beerapp.model.RestaurantInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angelika on 18.08.2018.
 */

public class Utils {

    public static List<RestaurantInfo> getFieldsWithValueForInstanceRestaurant(Class<? extends Annotation> ann, Restaurant inst) {
        List<RestaurantInfo> list = new ArrayList<>();
        Class c = Restaurant.class;
        for (Field field : c.getDeclaredFields()) {
            if (field.isAnnotationPresent(ann)) {
                Annotation annotation = field.getAnnotation(Input.class);
                Input inputAnnotation = (Input) annotation;
                String name = inputAnnotation.name();
                String methodName = "get" + field.getName().substring(1, field.getName().length());
                try {
                    Method method = c.getMethod(methodName);
                    try {
                        String value = (String) method.invoke(inst);
                        RestaurantInfo info = new RestaurantInfo(name + " : " + value);
                        list.add(info);
                    } catch (IllegalAccessException aE) {
                        aE.printStackTrace();
                    } catch (InvocationTargetException aE) {
                        aE.printStackTrace();
                    }

                } catch (NoSuchMethodException aE) {
                    aE.printStackTrace();
                }
            }
        }

        return list;
    }


}
