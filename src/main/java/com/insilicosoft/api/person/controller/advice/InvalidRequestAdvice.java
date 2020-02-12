package com.insilicosoft.api.person.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.insilicosoft.api.person.exception.InvalidRequestException;
import com.insilicosoft.api.person.exception.PersonNotFoundException;

/**
 * Controller advice to capture invalid incoming requests.
 *
 * @author geoff
 */
@ControllerAdvice
public class InvalidRequestAdvice {

  /**
   * Advice handling of captured invalid request exceptions.
   * 
   * @param e Invalid request exception.
   * @return Exception message (in response body) for additional detail.
   */
  @ResponseBody
  @ExceptionHandler(InvalidRequestException.class)
  // https://stackoverflow.com/questions/6123425/rest-response-code-for-invalid-data
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String requestNotPermissibleHandler(final InvalidRequestException e) {
    return e.getMessage();
  }

  /**
   * 
   * @param e
   * @return
   */
  @ResponseBody
  @ExceptionHandler(PersonNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String personNotFoundHandler(final PersonNotFoundException e) {
    return e.getMessage();
  }
}