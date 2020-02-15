package com.insilicosoft.api.person.controller;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.ArrayList;

import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.insilicosoft.api.person.service.PersonService;
import com.insilicosoft.api.person.value.PersonDto;

/**
 * Integration test the Person API.
 *
 * @author geoff
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonControllerIT {

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockBean
  private PersonService mockPersonService;

  private static final String urlTemplatePersons = "/persons";
  private static final String dummyFirstName = "dummyFirstName";
  private static final String dummyLastName = "dummyLastName";
  private static final Integer dummyAge = 10;
  private static final String dummyFavouriteColour = "dummyFavouriteColour";
  private static final String[] dummyHobbies = {};
  private static final Long dummyPersonId = 1L;

  private static final String dummyJson = "{\"first_name\":\"%s\","
                                         + "\"last_name\":\"%s\","
                                         + "\"age\":\"%s\","
                                         + "\"favourite_colour\":\"%s\","
                                         + "\"hobby\":%s}";

  @Before
  public void setUp() {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void testDeletePerson() throws Exception {
    doNothing().when(mockPersonService).deletePerson(dummyPersonId);

    mockMvc.perform(delete(urlTemplatePersons.concat("/" + dummyPersonId))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.ALL))
           //.andDo(print())
           .andExpect(status().isNoContent())
           //https://stackoverflow.com/questions/46420404/spring-mockmvc-verify-body-is-empty
           .andExpect(jsonPath("$").doesNotExist());

    verify(mockPersonService, times(1)).deletePerson(dummyPersonId);

    verifyNoMoreInteractions(mockPersonService);
  }

  @Test
  public void testGetAllOnEmptyDataset() throws Exception {
    when(mockPersonService.all()).thenReturn(new ArrayList<PersonDto>());

    mockMvc.perform(get(urlTemplatePersons).header(HttpHeaders.CONTENT_TYPE,
                                                   MediaType.ALL)
                                           .header(HttpHeaders.ACCEPT,
                                                   MediaTypes.HAL_JSON_UTF8))
           //.andDo(print())
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8))
           .andExpect(jsonPath("$._links.self.href")
                              .value("http://localhost/persons"));

    verify(mockPersonService, times(1)).all();

    verifyNoMoreInteractions(mockPersonService);
  }

  @Test
  public void testGetOne() throws Exception {
    final PersonDto mockPersonDto = mock(PersonDto.class);

    when(mockPersonService.one(dummyPersonId))
        .thenReturn(mockPersonDto);
    when(mockPersonDto.getId()).thenReturn(dummyPersonId);
    when(mockPersonDto.getFirst_name()).thenReturn(dummyFirstName);
    when(mockPersonDto.getLast_name()).thenReturn(dummyLastName);
    when(mockPersonDto.getAge()).thenReturn(dummyAge);
    when(mockPersonDto.getFavourite_colour()).thenReturn(dummyFavouriteColour);
    when(mockPersonDto.getHobby()).thenReturn(dummyHobbies);

    mockMvc.perform(get(urlTemplatePersons.concat("/" + dummyPersonId))
                       .header(HttpHeaders.CONTENT_TYPE, MediaType.ALL))
           //.andDo(print())
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8))
           .andExpect(jsonPath("$._embedded.persons").isArray())
           .andExpect(jsonPath("$._embedded.persons.length()").value(1))
           .andExpect(jsonPath("$._embedded.persons[0].personFirstName")
                              .value(dummyFirstName))
           .andExpect(jsonPath("$._embedded.persons[0].personLastName")
                              .value(dummyLastName))
           .andExpect(jsonPath("$._embedded.persons[0].personAge")
                              .value(dummyAge))
           .andExpect(jsonPath("$._embedded.persons[0].personFavouriteColour")
                              .value(dummyFavouriteColour))
           .andExpect(jsonPath("$._embedded.persons[0].personHobbies")
                              .isArray())
           .andExpect(jsonPath("$._embedded.persons[0]._links.self").exists())
           .andExpect(jsonPath("$._embedded.persons[0]._links.delete").exists())
           .andExpect(jsonPath("$._embedded.persons[0]._links.update").exists())
           .andExpect(jsonPath("$._links.self.href").exists());

    verify(mockPersonService, times(1)).one(dummyPersonId);
    verify(mockPersonDto, times(1)).getId();
    verify(mockPersonDto, times(1)).getFirst_name();
    verify(mockPersonDto, times(1)).getLast_name();
    verify(mockPersonDto, times(1)).getAge();
    verify(mockPersonDto, times(1)).getFavourite_colour();
    verify(mockPersonDto, times(1)).getHobby();

    verifyNoMoreInteractions(mockPersonService, mockPersonDto);
  }

  @Test
  public void testPutPerson() throws Exception {
    final PersonDto mockPersonDto = mock(PersonDto.class);
    final ArgumentCaptor<Long> personIdCaptor = ArgumentCaptor.forClass(Long.class); 
    final ArgumentCaptor<PersonDto> personDtoCaptor = ArgumentCaptor.forClass(PersonDto.class);

    when(mockPersonService.updatePerson(personIdCaptor.capture(),
                                        personDtoCaptor.capture()))
        .thenReturn(mockPersonDto);
    when(mockPersonDto.getId()).thenReturn(dummyPersonId);

    final String newFirstName = "fish";
    final String newLastName = "chips";
    final Integer newAge = 20;
    final String newFavouriteColour = "blue";
    final String[] newHobbies = { "fishing" };
    when(mockPersonDto.getFirst_name()).thenReturn(newFirstName);
    when(mockPersonDto.getLast_name()).thenReturn(newLastName);
    when(mockPersonDto.getAge()).thenReturn(newAge);
    when(mockPersonDto.getFavourite_colour()).thenReturn(newFavouriteColour);
    when(mockPersonDto.getHobby()).thenReturn(newHobbies);

    mockMvc.perform(put(urlTemplatePersons.concat("/" + dummyPersonId))
                       .header(HttpHeaders.CONTENT_TYPE,
                               MediaType.APPLICATION_JSON_UTF8)
                       .content(String.format(dummyJson, newFirstName,
                                              newLastName, newAge,
                                              newFavouriteColour,
                                              "[ \"fishing\" ]")))
           //.andDo(print())
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8))
           .andExpect(jsonPath("$._embedded.persons").isArray())
           .andExpect(jsonPath("$._embedded.persons.length()").value(1))
           .andExpect(jsonPath("$._embedded.persons[0].personFirstName")
                              .value(newFirstName))
           .andExpect(jsonPath("$._embedded.persons[0].personLastName")
                              .value(newLastName))
           .andExpect(jsonPath("$._embedded.persons[0].personAge")
                              .value(newAge))
           .andExpect(jsonPath("$._embedded.persons[0].personFavouriteColour")
                              .value(newFavouriteColour))
           .andExpect(jsonPath("$._embedded.persons[0].personHobbies")
                              .isArray())
           .andExpect(jsonPath("$._embedded.persons[0]._links.self").exists())
           .andExpect(jsonPath("$._embedded.persons[0]._links.delete").exists())
           .andExpect(jsonPath("$._embedded.persons[0]._links.update").exists())
           .andExpect(jsonPath("$._links.self.href").exists());

    verify(mockPersonService, times(1)).updatePerson(Mockito.isA(Long.class),
                                                     Mockito.isA(PersonDto.class));
    verify(mockPersonDto, times(1)).getId();
    verify(mockPersonDto, times(1)).getFirst_name();
    verify(mockPersonDto, times(1)).getLast_name();
    verify(mockPersonDto, times(1)).getAge();
    verify(mockPersonDto, times(1)).getFavourite_colour();
    verify(mockPersonDto, times(1)).getHobby();

    verifyNoMoreInteractions(mockPersonService, mockPersonDto);

    assertEquals(dummyPersonId, personIdCaptor.getValue());
    assertEquals(newFirstName, personDtoCaptor.getValue().getFirst_name());
    assertEquals(newLastName, personDtoCaptor.getValue().getLast_name());
    assertEquals(newAge, personDtoCaptor.getValue().getAge());
    assertEquals(newFavouriteColour,
                 personDtoCaptor.getValue().getFavourite_colour());
    assertArrayEquals(newHobbies, personDtoCaptor.getValue().getHobby());
  }

  @Test
  public void testPostNewPerson() throws Exception {
    final PersonDto mockPersonDto = mock(PersonDto.class);

    when(mockPersonService.newPerson(Mockito.isA(PersonDto.class)))
        .thenReturn(mockPersonDto);
    when(mockPersonDto.getId()).thenReturn(dummyPersonId);
    when(mockPersonDto.getFirst_name()).thenReturn(dummyFirstName);
    when(mockPersonDto.getLast_name()).thenReturn(dummyLastName);
    when(mockPersonDto.getAge()).thenReturn(dummyAge);
    when(mockPersonDto.getFavourite_colour()).thenReturn(dummyFavouriteColour);
    when(mockPersonDto.getHobby()).thenReturn(dummyHobbies);

    mockMvc.perform(post(urlTemplatePersons)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8)
                    .content(String.format(dummyJson, dummyFirstName,
                                           dummyLastName, dummyAge,
                                           dummyFavouriteColour,
                                           Arrays.asList(dummyHobbies).toString())))
           //.andDo(print())
           .andExpect(status().isCreated())
           .andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8))
           .andExpect(jsonPath("$._embedded.persons").isArray())
           .andExpect(jsonPath("$._embedded.persons.length()").value(1))
           .andExpect(jsonPath("$._embedded.persons[0].personFirstName")
                              .value(dummyFirstName))
           .andExpect(jsonPath("$._embedded.persons[0].personLastName")
                              .value(dummyLastName))
           .andExpect(jsonPath("$._embedded.persons[0].personAge")
                              .value(dummyAge))
           .andExpect(jsonPath("$._embedded.persons[0].personFavouriteColour")
                              .value(dummyFavouriteColour))
           .andExpect(jsonPath("$._embedded.persons[0].personHobbies")
                              .isArray())
           .andExpect(jsonPath("$._embedded.persons[0]._links.self").exists())
           .andExpect(jsonPath("$._embedded.persons[0]._links.delete").exists())
           .andExpect(jsonPath("$._embedded.persons[0]._links.update").exists())
           .andExpect(jsonPath("$._links.self.href").exists());

    verify(mockPersonService, times(1)).newPerson(Mockito.isA(PersonDto.class));
    verify(mockPersonDto, times(1)).getId();
    verify(mockPersonDto, times(1)).getFirst_name();
    verify(mockPersonDto, times(1)).getLast_name();
    verify(mockPersonDto, times(1)).getAge();
    verify(mockPersonDto, times(1)).getFavourite_colour();
    verify(mockPersonDto, times(1)).getHobby();

    verifyNoMoreInteractions(mockPersonService, mockPersonDto);
  }
}