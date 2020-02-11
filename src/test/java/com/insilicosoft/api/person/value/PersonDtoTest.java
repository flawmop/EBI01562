package com.insilicosoft.api.person.value;

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
  private String dummyName;

  @Before
  public void setUp() {
    dummyId = null;
    dummyName = null;
  }

  @Test
  public void testEntityConstructor() {
    mockPerson = mock(Person.class);

    dummyId = 1L;
    dummyName = "dummyName";

    when(mockPerson.getId()).thenReturn(dummyId);
    when(mockPerson.getName()).thenReturn(dummyName);

    personDto = new PersonDto(mockPerson);

    assertEquals(dummyId, personDto.getId());
    assertEquals(dummyName, personDto.getName());

    verify(mockPerson, times(1)).getId();
    verify(mockPerson, times(1)).getName();

    verifyNoMoreInteractions(mockPerson);
  }

  @Test
  public void testNonEntityConstructor() {
    personDto = new PersonDto(dummyId, dummyName);

    assertNull(personDto.getId());
    assertNull(personDto.getName());
    assertNotNull(personDto.toString());

    dummyId = 1L;
    dummyName = "dummyName";

    personDto = new PersonDto(dummyId, dummyName);

    assertEquals(dummyId, personDto.getId());
    assertEquals(dummyName, personDto.getName());
  }
  
}