package com.insilicosoft.api.person.entity;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * Unit test the {@link Person} entity.
 *
 * @author geoff
 */
public class PersonTest {

  private Person person;
  private Integer dummyAge;
  private String dummyFavouriteColour;
  private String dummyFirstName;
  private String dummyLastName;
  private String dummyHobby;
  private String[] dummyHobbies;

  @Test
  public void testDefaultConstructor() {
    person = new Person();
    assertNull(person.getId());
    assertNull(person.getFirstName());
    assertNotNull(person.toString());
  }

  private void assignValidArgs() {
    dummyFirstName = "dummyFirstName";
    dummyLastName = "dummyLastName";
    dummyAge = 1;
    dummyFavouriteColour = "dummyFavouriteColour";
    dummyHobbies = new String[1];
    dummyHobby = "dummyHobby";
    dummyHobbies[0] = dummyHobby;
  }

  @Test
  public void testInitialisingConstructor() {
    assignValidArgs();

    // Test assigning all values
    Person person = new Person(dummyFirstName, dummyLastName, dummyAge,
                               dummyFavouriteColour, dummyHobbies);
    assertSame(dummyFirstName, person.getFirstName());
    assertSame(dummyLastName, person.getLastName());
    assertSame(dummyAge, person.getAge());
    assertSame(dummyFavouriteColour, person.getFavouriteColour());
    assertSame(dummyHobby, person.getHobbies()[0]);
    assertNotNull(person.toString());

    // Test resetting hobbies.
    assignValidArgs();
    dummyHobbies = new String[0];
    person = new Person(dummyFirstName, dummyLastName, dummyAge,
                        dummyFavouriteColour, dummyHobbies);
    assertSame(dummyFirstName, person.getFirstName());
    assertSame(dummyLastName, person.getLastName());
    assertSame(dummyAge, person.getAge());
    assertSame(dummyFavouriteColour, person.getFavouriteColour());
    assertSame(0, person.getHobbies().length);

    // Test null hobbies.
    assignValidArgs();
    dummyHobbies = null;
    try {
      new Person(dummyFirstName, dummyLastName, dummyAge, dummyFavouriteColour,
                 dummyHobbies);
      fail("Should not permit null hobbies assignment");
    } catch (NullPointerException e) {}

    // Test null age and favourite colour
    assignValidArgs();
    dummyAge = null;
    dummyFavouriteColour = null;

    person = new Person(dummyFirstName, dummyLastName, dummyAge,
                        dummyFavouriteColour, dummyHobbies);
    assertNull(person.getAge());
    assertNull(person.getFavouriteColour());

    // Test null last name
    assignValidArgs();
    dummyLastName = null;

    try {
      new Person(dummyFirstName, dummyLastName, dummyAge, dummyFavouriteColour,
                 dummyHobbies);
      fail("Should not permit null last name assignment");
    } catch (IllegalArgumentException e) {}

    // Test null first name
    assignValidArgs();
    dummyFirstName = null;

    try {
      new Person(dummyFirstName, dummyLastName, dummyAge, dummyFavouriteColour,
                 dummyHobbies);
      fail("Should not permit null first name assignment");
    } catch (IllegalArgumentException e) {}
  }
}