package com.insilicosoft.api.person.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit test the "person not found" exception.
 *
 * @author geoff
 */
public class PersonNotFoundExceptionTest {

  private PersonNotFoundException personNFException;

  @Test
  public void testConstructors() {
    personNFException = new PersonNotFoundException();

    assertEquals("Person with id '<unspecified!>' not found",
                 personNFException.getMessage());

    final String dummyPersonId = "dummyPersonId";

    personNFException = new PersonNotFoundException(dummyPersonId);

    assertEquals("Person with id '".concat(dummyPersonId).concat("' not found"),
                 personNFException.getMessage());
  }
}