package com.insilicosoft.api.person.exception;

/**
 * Exception thrown when a {@linkplain com.insilicosoft.api.person.entity.Person}
 * is not found.
 *
 * @author geoff
 */
public class PersonNotFoundException extends Exception {

  private static final long serialVersionUID = 1L;

  private static final String defaultMessage = "Person with id '%s' not found";

  /**
   * Default constructor (assigning default message).
   */
  public PersonNotFoundException() {
    super(String.format(defaultMessage, "<unspecified!>"));
  }

  /**
   * Initialising constructor.
   * 
   * @param personId System ID of {@linkplain com.insilicosoft.api.person.entity.Person}
   *                 being sought.
   */
  public PersonNotFoundException(final String personId) {
    super(String.format(defaultMessage, personId));
  }
}