package com.clear_solution.test.task.users.util;

import com.clear_solution.test.task.users.domain.User;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;

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
