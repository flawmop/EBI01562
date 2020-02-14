package com.insilicosoft.api.person.controller.advice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import com.insilicosoft.api.person.exception.InvalidRequestException;
import com.insilicosoft.api.person.exception.PersonNotFoundException;

/**
 * Controller advice to capture anticipated exceptions and respond as
 * {@link Problem}s with the {@code MediaType.APPLICATION_PROBLEM_JSON_UTF8}
 * content type.
 *
 * @author geoff
 * @see <a href="https://tools.ietf.org/html/rfc7807">RFC 7807: Problem Details for HTTP APIs</a>
 */
@ControllerAdvice
public class ExceptionCatchingAdvice {

  private final static HttpHeaders problemHeaders = new HttpHeaders();

  static {
    // https://github.com/spring-projects/spring-framework/issues/21927
    // https://github.com/spring-projects/spring-framework/issues/20865
    problemHeaders.setContentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8);
  }

  /**
   * Advice handling of captured invalid request exceptions.
   * 
   * @param e Invalid request exception.
   * @return Exception message (in response body) for additional detail.
   */
  @ExceptionHandler(InvalidRequestException.class)
  // https://stackoverflow.com/questions/6123425/rest-response-code-for-invalid-data
  public ResponseEntity<Problem> requestNotPermissibleHandler(final InvalidRequestException e) {
    return new ResponseEntity<Problem>(Problem.builder()
                                              .withTitle("Invalid request submitted")
                                              .withStatus(Status.BAD_REQUEST)
                                              .withDetail(e.getMessage())
                                              .build(),
                                        problemHeaders, HttpStatus.BAD_REQUEST);
  }

  /**
   * Advice handling of captured person not found exception.
   * 
   * @param e Person not found exception.
   * @return Exception message (in response body) for additional detail.
   */
  @ExceptionHandler(PersonNotFoundException.class)
  public ResponseEntity<Problem> personNotFoundHandler(final PersonNotFoundException e) {
    return new ResponseEntity<Problem>(Problem.builder()
                                              .withTitle("Person not found")
                                              .withStatus(Status.NOT_FOUND)
                                              .withDetail(e.getMessage())
                                              .build(),
                                        problemHeaders, HttpStatus.NOT_FOUND);
  }
}