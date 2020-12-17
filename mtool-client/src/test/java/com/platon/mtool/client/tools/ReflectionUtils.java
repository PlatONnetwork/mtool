package com.platon.mtool.client.tools;

import java.lang.reflect.Field;

public class ReflectionUtils {

    public static void setField(Object targetObject, String name, Object value, Class<?> type) throws NoSuchFieldException, IllegalAccessException {
        Field nameField = type.getDeclaredField(name);
        nameField.setAccessible(true);
        nameField.set(targetObject, value);
    }
}
