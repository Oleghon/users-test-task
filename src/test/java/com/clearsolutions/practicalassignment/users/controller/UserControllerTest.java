package com.clearsolutions.practicalassignment.users.controller;

import com.clearsolutions.practicalassignment.users.domain.Range;
import com.clearsolutions.practicalassignment.users.domain.User;
import com.clearsolutions.practicalassignment.users.handler.ErrorResponse;
import com.clearsolutions.practicalassignment.users.mocks.MockHelper;
import com.clearsolutions.practicalassignment.users.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;

import static com.clearsolutions.practicalassignment.users.mocks.MockHelper.mockId;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    protected MockMvc mvc;

    @MockBean
    private UserService service;

    private ObjectMapper mapper = new ObjectMapper();

    private static String URL = "/users";

    private User user;

    @Test
    public void createSuccessTest() throws Exception {
        user = MockHelper.buildUser();
        String content = mapToJson(user);

        when(service.createUser(user)).thenReturn(user);

        MvcResult result = mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated())
                .andReturn();

        User actual = mapFromJson(result);

        assertAll(
                () -> assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus()),
                () -> assertNotNull(actual.getUuid()),
                () -> assertEquals(user.getFirstName(), actual.getFirstName()),
                () -> assertEquals(user.getBirthDate(), actual.getBirthDate()),
                () -> assertEquals(user.getLastName(), actual.getLastName())
        );
    }

    @Test
    public void createFailValidationTest() throws Exception {
        user = new User();
        user.setUuid(mockId);
        user.setEmail("updatedEmailtest.com");
        user.setFirstName(" ");
        user.setLastName("");
        user.setBirthDate(LocalDate.now());
        String content = mapToJson(user);

        when(service.createUser(user)).thenReturn(user);

        MvcResult result = mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andReturn();

        ErrorResponse actual = mapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals("User not valid", actual.getMessage()),
                () -> assertFalse(actual.getDetails().isEmpty())
        );
    }

    @Test
    public void updateSuccessTest() throws Exception {
        user = MockHelper.buildUser();

        User updatedUser = new User();
        updatedUser.setUuid(mockId);
        updatedUser.setEmail("updatedEmail@test.com");
        updatedUser.setFirstName("Updated");
        updatedUser.setLastName("Updated");
        updatedUser.setBirthDate(LocalDate.of(1999, 12, 12));

        when(service.updateUser(user.getUuid(), updatedUser)).thenReturn(updatedUser);

        String content = mapToJson(updatedUser);
        MvcResult result = mvc.perform(put(URL + "/{id}", user.getUuid())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andReturn();

        User actual = mapFromJson(result);

        assertAll(
                () -> assertNotEquals(user, actual),
                () -> assertEquals(user.getUuid(), actual.getUuid()));
    }

    @Test
    public void updateFailValidationTest() throws Exception {
        user = MockHelper.buildUser();

        User updatedUser = new User();
        updatedUser.setUuid(mockId);
        updatedUser.setEmail("updatedEmailtest.com");
        updatedUser.setFirstName(" ");
        updatedUser.setLastName("");
        updatedUser.setBirthDate(LocalDate.now());

        when(service.updateUser(user.getUuid(), updatedUser)).thenReturn(updatedUser);

        String content = mapToJson(updatedUser);
        MvcResult result = mvc.perform(put(URL + "/{id}", user.getUuid())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andReturn();

        ErrorResponse actual = mapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals("User not valid", actual.getMessage()),
                () -> assertFalse(actual.getDetails().isEmpty())
        );
    }

    @Test
    public void readByIdSuccessTest() throws Exception {
        user = MockHelper.buildUser();
        when(service.findUser(mockId)).thenReturn(user);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", mockId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        User actual = mapFromJson(result);

        assertEquals(user, actual);
    }

    @Test
    public void successReadAllTest() throws Exception {
        MvcResult result = mvc.perform(get(URL + "/all"))
                .andExpect(status().isOk())
                .andReturn();

        assertFalse(result.getResponse().getContentAsString().isBlank());
    }

    @Test
    public void successFindByRangeTest() throws Exception {
        Range range = new Range(LocalDate.of(2000, 1, 1),
                LocalDate.of(2010, 1, 1));


        String content = mapToJson(range);
        MvcResult result = mvc.perform(post(URL + "/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andReturn();

        assertFalse(result.getResponse().getContentAsString().isBlank());
    }

    @Test
    public void findByRangeFailValidationTest() throws Exception {
        Range range = new Range(
                LocalDate.of(2010, 1, 1),
                LocalDate.of(2000, 1, 1));

        String content = mapToJson(range);
        MvcResult result = mvc.perform(post(URL + "/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andReturn();

        ErrorResponse actual = mapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals("Range not valid", actual.getMessage()),
                () -> assertFalse(actual.getDetails().isEmpty())
        );
    }

    @Test
    public void deleteSuccessTest() throws Exception {
        user = MockHelper.buildUser();

        when(service.deleteUser(mockId)).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", mockId))
                .andExpect(status().isOk());
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }

    protected User mapFromJson(MvcResult result) throws JsonProcessingException, UnsupportedEncodingException {
        return this.mapper.readValue(result.getResponse().getContentAsString(), User.class);
    }
}
