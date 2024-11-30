package com.validation.ValidationAPI.exception;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionalHandler{


    //This exception is used for validation request body parameters for entity object
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMethodArgumentException(MethodArgumentNotValidException exception) {
        Map<String, String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });

        return errorMap;
    }

    //This method is used  for validation RequestQuery Param missing
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>> handleMissingParams(MissingServletRequestParameterException ex) {

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Missing required parameter");
        errorResponse.put("parameter", ex.getParameterName());
        errorResponse.put("status", String.valueOf(HttpStatus.BAD_REQUEST.value()));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    //This method is used to validate request parameters for a specific HTTP method during message validation.
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,String> handleConstraintViolation(ConstraintViolationException ex) {
        // Extract the violation message
        String message = ex.getConstraintViolations().iterator().next().getMessage();

        //Map
        Map<String,String> errorMessage = new HashMap<>();
        errorMessage.put("Message", message);

        // Return the message in the response body with BAD_REQUEST status
        return errorMessage;
    }

    /*
    This exception is thrown when a handler method expects a specific path variable,
    but the path variable is not present in the request mapping, or it is not correctly defined.

    If you define a method with a @PathVariable, and the path variable is not properly supplied,
    Spring will throw this exception.
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<String> handleMissingPathVariable(MissingPathVariableException ex) {
        String variableName = ex.getVariableName();
        return new ResponseEntity<>(variableName + " path variable is missing", HttpStatus.BAD_REQUEST);
    }

    /*
    * This exception is thrown when a @PathVariable or @RequestParam value cannot be converted to the expected type.
    * For instance, if you expect a Long but the provided path variable is a non-numeric string.
    * If you define a @PathVariable to be a Long, but the client passes a string, Spring will throw a TypeMismatchException.
    * */
    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(TypeMismatchException ex) {
        return new ResponseEntity<>("Path variable type mismatch: " + ex.getValue(), HttpStatus.BAD_REQUEST);
    }

    //In your controller or service layer, throw the custom exception when validation fails.
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> handleNoResourceFound(NoResourceFoundException ex) {
        return new ResponseEntity<>("Resource Path Not Found " + ex.getResourcePath(), HttpStatus.BAD_REQUEST);
    }

    //Custom defined Exception to return user respective message
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,String> handleCustomValidationException(ValidationException ex) {
         Map<String,String> validationErrorMap = new HashMap<>();
         validationErrorMap.put("Message", ex.getMessage());
        return validationErrorMap;
    }

    //Run time Exception handling
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>("Internal Server Error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Authentication required: Please provide a valid token.");
    }

    //If the user does not specific permission to access the endpoint then this error will be returning
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("Access denied: You do not have permission to access this resource.");
    }

    //if we provide invalid credentials for login/authenatication to get jwt token then this will be returning
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleAccessDeniedException(BadCredentialsException ex , HttpServletResponse response) {
        return ResponseEntity
                .status( HttpServletResponse.SC_UNAUTHORIZED)
                .body("Invalid username or password");
    }


}
