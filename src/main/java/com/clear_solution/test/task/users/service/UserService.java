package com.clear_solution.test.task.users.service;

import com.clear_solution.test.task.users.domain.Range;
import com.clear_solution.test.task.users.domain.User;
import com.clear_solution.test.task.users.exception.UserNotEnoughAdultException;
import com.clear_solution.test.task.users.exception.UserNotFoundException;
import com.clear_solution.test.task.users.util.Patcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@PropertySource("application.yml")
public class UserService {

    @Value("${adult.age}")
    private int age;

    private static final HashMap<UUID, User> users = new HashMap<>() {{
        User user = new User();
        user.setEmail("vitok@vvv.com");
        user.setFirstName("Viktor");
        user.setLastName("Melnik");
        user.setBirthDate(LocalDate.of(2000, 6, 5));
        put(user.getUuid(), user);

        user = new User();
        user.setEmail("caron@vvv.com");
        user.setFirstName("Caron");
        user.setLastName("Jones");
        user.setBirthDate(LocalDate.of(2005, 3, 5));
        put(user.getUuid(), user);

        user = user = new User();
        user.setEmail("smola@vvv.com");
        user.setFirstName("Simon");
        user.setLastName("Smolan");
        user.setBirthDate(LocalDate.of(2003, 4, 1));
        put(user.getUuid(), user);

        user = user = new User();
        user.setEmail("zhorik@vvv.com");
        user.setFirstName("Egor");
        user.setLastName("Klaus");
        user.setBirthDate(LocalDate.of(2003, 5, 1));
        put(user.getUuid(), user);
    }};

    public User createUser(User user) {
        checkUserBirthDate(user);
        users.put(user.getUuid(), user);
        return users.get(user.getUuid());
    }

    private void checkUserBirthDate(User user) {
        if (LocalDate.now().minusYears(age).isBefore(user.getBirthDate())) {
            throw new UserNotEnoughAdultException("User must be older than " + age + " years");
        }
    }

    public List<User> getAllUsers() {
        return users.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .toList();
    }

    public User findUser(UUID id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else throw new UserNotFoundException("User doesn't exist");
    }

    public User updateUser(UUID id, User user) {
        user.setUuid(id);
        if (users.containsKey(id)) {
            users.put(id, user);
            return users.get(id);
        } else throw new UserNotFoundException("User doesn't exist");
    }

    public User patchUpdateUser(UUID id, User incomingUser) {
        if (users.containsKey(id)) {
            try {
                User existingUser = users.get(id);
                Patcher.userPatcher(existingUser, incomingUser);
                return existingUser;
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        throw new UserNotFoundException("User doesn't exist");
    }

    public boolean deleteUser(UUID id) {
        if (users.containsKey(id)) {
            users.remove(id);
            return true;
        }
        return false;
    }

    public List<User> findUsersByRange(Range range) {
        return users.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .filter(u -> u.getBirthDate() != null)
                .filter(u -> u.getBirthDate().isAfter(range.from())
                        && u.getBirthDate().isBefore(range.to()))
                .collect(Collectors.toList());
    }
}
