package com.insilicosoft.api.person.value;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.insilicosoft.api.person.entity.Person;

/**
 * Unit test {@link Person} DTO.
 *
 * @author geoff
 */
public class PersonDtoTest {

  private Long dummyId;
  private Person mockPerson;
  private PersonDto personDto;
  private String dummyFirstName;
  private String dummyLastName;
  private Integer dummyAge;
  private String dummyFavouriteColour;
  private String[] dummyHobbies;

  @Before
  public void setUp() {
    dummyId = null;
    dummyFirstName = null;
    dummyLastName = null;
    dummyAge = null;
    dummyFavouriteColour = null;
    dummyHobbies = null;
  }

  @Test
  public void testEntityConstructorWithNullValues() {
    mockPerson = mock(Person.class);

    dummyId = 1L;

    when(mockPerson.getId()).thenReturn(dummyId);
    when(mockPerson.getFirstName()).thenReturn(dummyFirstName);
    when(mockPerson.getLastName()).thenReturn(dummyLastName);
    when(mockPerson.getAge()).thenReturn(dummyAge);
    when(mockPerson.getFavouriteColour()).thenReturn(dummyFavouriteColour);
    when(mockPerson.getHobbies()).thenReturn(dummyHobbies);

    personDto = new PersonDto(mockPerson);

    assertEquals(dummyId, personDto.getId());
    assertNull(personDto.getFirst_name());
    assertNull(personDto.getLast_name());
    assertNull(personDto.getAge());
    assertNull(personDto.getFavourite_colour());
    assertNull(personDto.getHobby());

    verify(mockPerson, times(1)).getId();
    verify(mockPerson, times(1)).getFirstName();
    verify(mockPerson, times(1)).getLastName();
    verify(mockPerson, times(1)).getAge();
    verify(mockPerson, times(1)).getFavouriteColour();
    verify(mockPerson, times(1)).getHobbies();
    assertNotNull(personDto.toString());

    verifyNoMoreInteractions(mockPerson);
  }

  @Test
  public void testEntityConstructor() {
    mockPerson = mock(Person.class);

    dummyId = 1L;
    dummyFirstName = "dummyFirstName";
    dummyLastName = "dummyLastName";
    dummyAge = 20;
    dummyFavouriteColour = "dummyFavouriteColour";
    dummyHobbies = new String[2];
    dummyHobbies[0] = "dummyHobby1";
    dummyHobbies[1] = "dummyHobby2"; 

    when(mockPerson.getId()).thenReturn(dummyId);
    when(mockPerson.getFirstName()).thenReturn(dummyFirstName);
    when(mockPerson.getLastName()).thenReturn(dummyLastName);
    when(mockPerson.getAge()).thenReturn(dummyAge);
    when(mockPerson.getFavouriteColour()).thenReturn(dummyFavouriteColour);
    when(mockPerson.getHobbies()).thenReturn(dummyHobbies);

    personDto = new PersonDto(mockPerson);

    assertEquals(dummyId, personDto.getId());
    assertEquals(dummyFirstName, personDto.getFirst_name());
    assertEquals(dummyLastName, personDto.getLast_name());
    assertEquals(dummyAge, personDto.getAge());
    assertEquals(dummyFavouriteColour, personDto.getFavourite_colour());
    assertArrayEquals(dummyHobbies, personDto.getHobby());

    verify(mockPerson, times(1)).getId();
    verify(mockPerson, times(1)).getFirstName();
    verify(mockPerson, times(1)).getLastName();
    verify(mockPerson, times(1)).getAge();
    verify(mockPerson, times(1)).getFavouriteColour();
    verify(mockPerson, times(1)).getHobbies();
    assertNotNull(personDto.toString());

    verifyNoMoreInteractions(mockPerson);
  }

  @Test
  public void testNonEntityConstructor() {
    dummyId = 1L;
    dummyFirstName = "dummyFirstName";
    dummyLastName = "dummyLastName";
    dummyAge = 20;
    dummyFavouriteColour = "dummyFavouriteColour";
    dummyHobbies = new String[2];
    dummyHobbies[0] = "dummyHobby1";
    dummyHobbies[1] = "dummyHobby2"; 

    personDto = new PersonDto(dummyId, dummyFirstName, dummyLastName,
                              dummyAge, dummyFavouriteColour, dummyHobbies);

    assertEquals(dummyId, personDto.getId());
    assertEquals(dummyFirstName, personDto.getFirst_name());
    assertEquals(dummyLastName, personDto.getLast_name());
    assertEquals(dummyAge, personDto.getAge());
    assertEquals(dummyFavouriteColour, personDto.getFavourite_colour());
    assertArrayEquals(dummyHobbies, personDto.getHobby());
  }
}