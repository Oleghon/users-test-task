package com.clear_solution.test.task.users.handler;

import com.clear_solution.test.task.users.exception.UserNotEnoughAdultException;
import com.clear_solution.test.task.users.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler<User> {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List details = ex.getBindingResult().getAllErrors().stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.toList());
        ErrorResponse error = new ErrorResponse(ex.getTarget().getClass().getSimpleName() + " not valid", details);
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    protected ResponseEntity<Object> handleUserNotFound(RuntimeException ex, WebRequest request) {
        String responseBody = ex.getMessage();
        return new ResponseEntity(responseBody, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = {UserNotEnoughAdultException.class})
    protected ResponseEntity<Object> handleNotEnoughAdult(RuntimeException ex, WebRequest request) {
        String responseBody = ex.getMessage();
        return new ResponseEntity(responseBody, HttpStatus.BAD_REQUEST);
    }
}
