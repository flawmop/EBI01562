package com.insilicosoft.api.person.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.insilicosoft.api.person.dao.jpa.PersonRepository;
import com.insilicosoft.api.person.entity.Person;

/**
 * Unit test the {@link Person} service interface implementation.
 *
 * @author geoff
 */
@RunWith(MockitoJUnitRunner.class)
public class PersonServiceImplTest {

  @Mock
  PersonRepository mockPersonRepository;

  @InjectMocks
  private PersonService personService = new PersonServiceImpl();

  @Test
  public void testAllOnNoPeople() {
    final List<Person> dummyPeople = new ArrayList<Person>();

    when(mockPersonRepository.findAll()).thenReturn(dummyPeople);

    personService.all();

    verify(mockPersonRepository, times(1)).findAll();

    verifyNoMoreInteractions(mockPersonRepository);
  }

}