package com.ilqjx.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class GloabalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public String defaultErrorHandler(HttpServletRequest request, Exception e) throws ClassNotFoundException {
        e.printStackTrace();
        // 约束违反异常
        Class constraintViolationException = Class.forName("org.hibernate.exception.ConstraintViolationException");
        if (e.getCause() != null && constraintViolationException == e.getCause().getClass()) {
            return "约束违反异常，可能是外键约束";
        }
        return e.getMessage();
    }

}
