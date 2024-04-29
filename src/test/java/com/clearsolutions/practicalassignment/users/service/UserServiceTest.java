package com.clearsolutions.practicalassignment.users.service;

import com.clearsolutions.practicalassignment.users.domain.Range;
import com.clearsolutions.practicalassignment.users.domain.User;
import com.clearsolutions.practicalassignment.users.exception.UserNotEnoughAdultException;
import com.clearsolutions.practicalassignment.users.exception.UserNotFoundException;
import com.clearsolutions.practicalassignment.users.mocks.MockHelper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService service;


    @Test
    @Order(1)
    public void createUserTest() {
        User user = MockHelper.buildUser();
        User actual = service.createUser(user);
        assertEquals(user, actual);
    }

    @Test
    public void updateUserTest() {
        User expected = MockHelper.buildUser();
        User updatedUser = new User();

        updatedUser.setEmail("updatedEmail@test.com");
        updatedUser.setFirstName("Updated");
        updatedUser.setLastName(" ");
        updatedUser.setBirthDate(LocalDate.of(1999, 12, 12));

        User actual = service.updateUser(MockHelper.mockId, updatedUser);

        assertAll(
                () -> assertNotEquals(expected, actual),
                () -> assertEquals(expected.getUuid(), actual.getUuid()));
    }

    @Test
    public void updatePatchUserTest() {
        User patchUser = new User();

        patchUser.setEmail("updatedEmail@test.com");
        patchUser.setFirstName("Updated");
        patchUser.setLastName(" ");

        User actual = service.patchUpdateUser(MockHelper.mockId, patchUser);

        assertAll(
                () -> assertFalse(actual.getLastName().isBlank()),
                () -> assertNotNull(actual.getBirthDate()),
                () -> assertEquals(patchUser.getFirstName(), actual.getFirstName()),
                () -> assertNotEquals(patchUser.getLastName(), actual.getLastName()));
    }

    @Test
    public void findUserTest() {
        User expected = MockHelper.buildUser();
        service.createUser(expected);

        User actual = service.findUser(MockHelper.mockId);
        assertEquals(expected, actual);
    }

    @Test
    public void allUserTest() {
        List<User> actual = service.getAllUsers();

        assertAll(
                () -> assertNotNull(actual),
                () -> assertTrue(actual.size() > 0));
    }

    @Test
    public void findUserFailTest() {
        Exception exception = assertThrows(UserNotFoundException.class,
                () -> service.findUser(UUID.randomUUID()));

        assertTrue(exception.getMessage().contains("User doesn't exist"));
    }

    @Test
    public void createUserFailTest() {
        ReflectionTestUtils.setField(service, "age", 18);

        User user = MockHelper.buildUser();
        user.setBirthDate(LocalDate.now());

        Exception exception = assertThrows(UserNotEnoughAdultException.class,
                () -> service.createUser(user));

        assertTrue(exception.getMessage().contains("User must be older than "));
    }


    @Test
    public void findUserByRangeTest() {
        List<User> actual = service.findUsersByRange(new Range(
                LocalDate.of(2000, 1, 1),
                LocalDate.of(2010, 1, 1)));

        assertNotNull(actual);
        assertFalse(actual.isEmpty());
    }

    @Test
    public void deleteUserTest() {
        assertTrue(service.deleteUser(MockHelper.mockId));
    }
}
