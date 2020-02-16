package com.insilicosoft.api.person.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.insilicosoft.api.person.exception.InvalidRequestException;
import com.insilicosoft.api.person.exception.PersonNotFoundException;
import com.insilicosoft.api.person.service.PersonService;
import com.insilicosoft.api.person.value.PersonDto;

/**
 * Verify that the {@link org.springframework.web.bind.annotation.ControllerAdvice}
 * is being heeded.
 *
 * @author geoff
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonControllerProblemDetailsIT {

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockBean
  private PersonService mockPersonService;

  private static final String urlTemplatePersons = "/persons";

  @Before
  public void setUp() {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void testFail400OnBadRequest() throws Exception {
    final String dummyMessage = "dummyMessage";
    // We're providing a valid name, but doThrow throws the exception nonetheless. 
    final String dummyName = "dummyName";
    final PersonDto mockPersonDto = mock(PersonDto.class);

    doThrow(new InvalidRequestException(dummyMessage))
           .when(mockPersonService).newPerson(Mockito.isA(PersonDto.class));

    mockMvc.perform(post(urlTemplatePersons)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8)
                    .content("{ \"name\" : \"" + dummyName + "\" }") )
            //.andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8))
            .andExpect(jsonPath("$.title", equalTo("Invalid request submitted")))
            .andExpect(jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.value())))
            .andExpect(jsonPath("$.detail", equalTo(dummyMessage)));

    verify(mockPersonService, times(1)).newPerson(Mockito.isA(PersonDto.class));

    verifyNoMoreInteractions(mockPersonService, mockPersonDto);
  }

  @Test
  public void testFail404OnPersonNotFound() throws Exception {
    final Long dummyPersonId = 1L;
    doThrow(new PersonNotFoundException(String.valueOf(dummyPersonId)))
           .when(mockPersonService).deletePerson(dummyPersonId);

    mockMvc.perform(delete(urlTemplatePersons.concat("/" + dummyPersonId))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.ALL))
            //.andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8))
            .andExpect(jsonPath("$.title").value("Person not found"))
            .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
            .andExpect(jsonPath("$.detail")
                               .value("Person with id '" + dummyPersonId + "' not found"));

    verify(mockPersonService, times(1)).deletePerson(dummyPersonId);

    verifyNoMoreInteractions(mockPersonService);
  }

  @Test
  public void testFail406OnUnsupportedAcceptMediaType() throws Exception {
    mockMvc.perform(get(urlTemplatePersons).header(HttpHeaders.CONTENT_TYPE,
                                                   MediaType.ALL)
                                           // Request shouldn't expect application/xml!
                                           .header(HttpHeaders.ACCEPT,
                                                   MediaType.APPLICATION_XML))
           //.andDo(print())
           .andExpect(status().isNotAcceptable())
           .andExpect(jsonPath("$.title").value("Not Acceptable"))
           .andExpect(jsonPath("$.status").value(HttpStatus.NOT_ACCEPTABLE.value()))
           .andExpect(jsonPath("$.detail")
                              .value("Could not find acceptable representation"));
  }
}