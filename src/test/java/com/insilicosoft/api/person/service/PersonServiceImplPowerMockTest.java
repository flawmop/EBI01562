package com.insilicosoft.api.person.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.insilicosoft.api.person.dao.jpa.PersonRepository;
import com.insilicosoft.api.person.entity.Person;
import com.insilicosoft.api.person.exception.InvalidRequestException;
import com.insilicosoft.api.person.value.PersonDto;

/**
 * Unit test the {@link Person} service interface implementation (with usual
 * performance degradation caveats of using PowerMock).
 * TODO: PowerMock testing not appearing in jacoco test coverage!
 * 
 * @author geoff
 */
// https://github.com/powermock/powermock/wiki/Mockito
@RunWith(PowerMockRunner.class)
@PrepareForTest({ PersonServiceImpl.class })
public class PersonServiceImplPowerMockTest {

  @Mock
  PersonDto mockPersonDto;

  @Mock
  PersonRepository mockPersonRepository;

  @InjectMocks
  private PersonService personService = new PersonServiceImpl();

  private static final String dummyFirstName = "dummyFirstName";
  private static final String dummyLastName = "dummyLastName";
  private static final Integer dummyAge = 10;
  private static final String dummyFavouriteColour = "dummyFavouriteColour";
  private static final String[] dummyHobbies = {};

  /*
   * https://stackoverflow.com/questions/5920153/test-class-with-a-new-call-in-it-with-mockito
   */
  @Test
  public void testAllWithSomePeople() throws Exception {
    final Person mockPerson1 = mock(Person.class);
    final Person mockPerson2 = mock(Person.class);
    final List<Person> dummyPeople = new ArrayList<Person>();
    dummyPeople.add(mockPerson1);
    dummyPeople.add(mockPerson2);

    final PersonDto mockPersonDto1 = mock(PersonDto.class);
    final PersonDto mockPersonDto2 = mock(PersonDto.class);

    when(mockPersonRepository.findAll()).thenReturn(dummyPeople);

    whenNew(PersonDto.class).withArguments(Person.class)
                            .thenReturn(mockPersonDto1);
    whenNew(PersonDto.class).withArguments(Person.class)
                            .thenReturn(mockPersonDto2);

    final List<PersonDto> returnedPeople = personService.all();
 
    verify(mockPersonRepository, times(1)).findAll();

    verifyNoMoreInteractions(mockPersonRepository, mockPerson1, mockPerson2,
                             mockPersonDto1, mockPersonDto2);

    assertSame(dummyPeople.size(), returnedPeople.size());
  }

  @Test
  public void testNewPersonExceptionThrowing() throws Exception {
    // It's a valid name (to ensure it's PowerMockito.whenNew(..) in effect!)
    when(mockPersonDto.getFirst_name()).thenReturn(dummyFirstName);
    when(mockPersonDto.getLast_name()).thenReturn(dummyLastName);
    when(mockPersonDto.getAge()).thenReturn(dummyAge);
    when(mockPersonDto.getFavourite_colour()).thenReturn(dummyFavouriteColour);
    when(mockPersonDto.getHobby()).thenReturn(dummyHobbies);

    final String dummyException = "dummyException";
    whenNew(Person.class).withArguments(dummyFirstName, dummyLastName,
                                        dummyAge, dummyFavouriteColour,
                                        dummyHobbies)
           .thenThrow(new IllegalArgumentException(dummyException));
    try {
      personService.newPerson(mockPersonDto);
      fail("Should have thrown an exception!");
    } catch (InvalidRequestException e) {
      assertEquals(dummyException, e.getMessage());
    }

    verify(mockPersonDto, times(1)).getFirst_name();
    verify(mockPersonDto, times(1)).getLast_name();
    verify(mockPersonDto, times(1)).getAge();
    verify(mockPersonDto, times(1)).getFavourite_colour();
    verify(mockPersonDto, times(1)).getHobby();

    verifyNoMoreInteractions(mockPersonRepository, mockPersonDto);
  }

  @Test
  public void testNewPerson() throws Exception {
    when(mockPersonDto.getFirst_name()).thenReturn(dummyFirstName);
    when(mockPersonDto.getLast_name()).thenReturn(dummyLastName);
    when(mockPersonDto.getAge()).thenReturn(dummyAge);
    when(mockPersonDto.getFavourite_colour()).thenReturn(dummyFavouriteColour);
    when(mockPersonDto.getHobby()).thenReturn(dummyHobbies);
    // Create new Person entity from PersonDto
    final Person mockPerson = mock(Person.class);
    whenNew(Person.class).withArguments(dummyFirstName, dummyLastName,
                                        dummyAge, dummyFavouriteColour,
                                        dummyHobbies)
           .thenReturn(mockPerson);
    final Person mockSavedPerson = mock(Person.class);
    when(mockPersonRepository.save(mockPerson))
        .thenReturn(mockSavedPerson);
    final PersonDto mockSavedPersonDto = mock(PersonDto.class);
    // Create new PersonDto derived from saved Person Entity
    whenNew(PersonDto.class).withArguments(mockSavedPerson)
           .thenReturn(mockSavedPersonDto);

    PersonDto returnedNewPersonDto = null;
    try {
      returnedNewPersonDto = personService.newPerson(mockPersonDto);
    } catch (InvalidRequestException e) {
      fail("Should not have thrown exception");
    }

    verify(mockPersonDto, times(1)).getFirst_name();
    verify(mockPersonDto, times(1)).getLast_name();
    verify(mockPersonDto, times(1)).getAge();
    verify(mockPersonDto, times(1)).getFavourite_colour();
    verify(mockPersonDto, times(1)).getHobby();
    verify(mockPersonRepository, times(1)).save(any(Person.class));

    verifyNoMoreInteractions(mockPersonRepository, mockPerson, mockPersonDto,
                             mockSavedPerson, mockSavedPersonDto);

    assertSame(mockSavedPersonDto, returnedNewPersonDto);
  }
}