package com.insilicosoft.api.person.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit test the invalid request exception.
 *
 * @author geoff
 */
public class InvalidRequestExceptionTest {

  private InvalidRequestException invReqException;

  @Test
  public void testConstructors() {
    invReqException = new InvalidRequestException();

    assertEquals("Invalid request received", invReqException.getMessage());

    final String dummyMessage = "dummyMessage";

    invReqException = new InvalidRequestException(dummyMessage);

    assertEquals(dummyMessage, invReqException.getMessage());
  }
}