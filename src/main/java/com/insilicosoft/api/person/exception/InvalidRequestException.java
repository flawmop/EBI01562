package com.insilicosoft.api.person.exception;

/**
 * Exception generated if invalid data submitted.
 *
 * @author geoff
 */
public class InvalidRequestException extends Exception {

  private static final long serialVersionUID = 1L;

  private static final String defaultMessage = "Invalid request received";

  public InvalidRequestException() {
    super(defaultMessage);
  }

  public InvalidRequestException(final String message) {
    super(message);
  }
}