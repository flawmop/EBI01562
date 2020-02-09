package com.insilicosoft.api.person.entity;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Unit test the {@link Person} entity.
 *
 * @author geoff
 */
public class PersonTest {

  private Person person;

  @Test
  public void testDefaultConstructor() {
    person = new Person();
    assertNull(person.getId());
    assertNull(person.getName());
    assertNotNull(person.toString());
  }

  @Test
  public void testInitialisingConstructor() {
    // Shouldn't allow a null name
    String dummyName = null;
    try {
      new Person(dummyName);
      fail("Should not permit a null name value");
    } catch (IllegalArgumentException e) {}

    // Shouldn't allow an empty name
    dummyName = "     ";
    try {
      new Person(dummyName);
      fail("Should not permit a null name value");
    } catch (IllegalArgumentException e) {}

    dummyName = "dummyName";
    person = new Person(dummyName);
    assertEquals(dummyName, person.getName());
    assertNull(person.getId());
    assertNotNull(person.toString());
  }
}