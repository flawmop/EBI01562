package com.insilicosoft.api.person.controller;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.insilicosoft.api.person.exception.InvalidRequestException;
import com.insilicosoft.api.person.service.PersonService;
import com.insilicosoft.api.person.value.PersonDto;

/**
 * Unit test the {@link PersonController}.
 *
 * @author geoff
 */
@RunWith(MockitoJUnitRunner.class)
public class PersonControllerTest {

  @Mock
  private PersonService mockPersonService;

  @InjectMocks
  private PersonController personController = new PersonController();

  //@Test
  public void testAll() {
    when(mockPersonService.all()).thenReturn(new ArrayList<PersonDto>());

    personController.all();

    verify(mockPersonService, times(1)).all();

    verifyNoMoreInteractions(mockPersonService);
  }

  @Test
  public void testNewPerson() throws InvalidRequestException {
    final PersonDto mockPersonDto = mock(PersonDto.class);

    when(mockPersonService.newPerson(mockPersonDto)).thenReturn(mockPersonDto);

    final PersonDto returnedNewPersonDto = personController.newPerson(mockPersonDto);

    verify(mockPersonService, times(1)).newPerson(any(PersonDto.class));

    verifyNoMoreInteractions(mockPersonService);

    assertSame(mockPersonDto, returnedNewPersonDto);
  }
}