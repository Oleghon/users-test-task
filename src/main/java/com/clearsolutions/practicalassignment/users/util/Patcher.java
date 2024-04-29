package com.clearsolutions.practicalassignment.users.util;

import com.clearsolutions.practicalassignment.users.domain.User;

import java.lang.reflect.Field;

//@Component
public class Patcher {
    public static void userPatcher(User existingUser, User incompleteUser) throws IllegalAccessException {
        Class<?> userClass = User.class;
        Field[] userFields = userClass.getDeclaredFields();

        for (Field field : userFields) {
            field.setAccessible(true);

            Object value = field.get(incompleteUser);
            if (value != null) {
                if (value instanceof String) {
                    if (((String) value).isBlank()) {
                        continue;
                    }
                }
                field.set(existingUser, value);
            }
            field.setAccessible(false);
        }
    }
}
