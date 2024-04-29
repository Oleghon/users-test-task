package com.clearsolutions.practicalassignment.users.mocks;

import com.clearsolutions.practicalassignment.users.domain.User;

import java.time.LocalDate;
import java.util.UUID;

public class MockHelper {

    public static UUID mockId = UUID.fromString("34c3f2f1-e864-4db6-aa4e-e0b4afe0c17e");
    public static User buildUser() {
        User user = new User();
        user.setUuid(mockId);
        user.setEmail("test@test.com");
        user.setFirstName("Test");
        user.setLastName("test");
        user.setBirthDate(LocalDate.of(2000, 1, 1));
        user.setAddress("Test address");
        user.setPhoneNumber("7804542323");
        return user;
    }
}
