package com.insilicosoft.api.person.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.insilicosoft.api.person.service.PersonService;
import com.insilicosoft.api.person.value.PersonDto;

/**
 * Derived from <a href="https://mkyong.com/spring-boot/spring-rest-spring-security-example/">Spring REST + Spring Security Example</a>
 * 
 * @see <a href="https://github.com/mkyong/spring-boot/blob/master/spring-rest-security/src/test/java/com/mkyong/BookControllerTest.java">mkyong/spring-boot</a>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
// TODO: Satisfy self why PersonControllerSecurityIT component scans controller advice yet others don't!
public class PersonControllerSecurityIT {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PersonService mockPersonService;

  private static final String urlTemplatePersons = "/persons";
  private static final String dummyUserName = "dummyUserName";
  private static final String dummyPassword = "dummyPassword";
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
                                         + "\"hobby\": %s }";

  // No authentication.
  @Test
  public void find_nologin_401() throws Exception {
      mockMvc.perform(get("/persons"))
              //.andDo(print())
              .andExpect(status().isUnauthorized());
  }

  // Authenticated, valid ROLE user 
  @Test
  @WithMockUser(username = dummyUserName, password = dummyPassword,
                roles = "USER")
  public void find_login_200() throws Exception {
    mockMvc.perform(get("/persons"))
            //.andDo(print())
            .andExpect(status().isOk());
  }

  // Authenticated, invalid ROLE user
  @Test
  @WithMockUser(username = dummyUserName, password = dummyPassword,
                roles = "SWISS ROLL")
  public void find_login_403() throws Exception {
    mockMvc.perform(get("/persons"))
            //.andDo(print())
            .andExpect(status().isForbidden());
  }

  // Authenticated, insufficient privileges ROLE
  @Test
  @WithMockUser(username = dummyUserName, password = dummyPassword,
                roles = "USER")
  public void new_login_403() throws Exception {
    mockMvc.perform(post(urlTemplatePersons)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8)
                    .content(String.format(dummyJson, dummyFirstName,
                                           dummyLastName, dummyAge,
                                           dummyFavouriteColour,
                                           Arrays.asList(dummyHobbies).toString())))
           //.andDo(print())
           .andExpect(status().isForbidden());
  }

  // Authenticated, sufficient privileges ROLE
  @Test
  @WithMockUser(username = dummyUserName, password = dummyPassword,
                roles = "ADMIN")
  public void new_login_201() throws Exception {
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
           .andExpect(status().isCreated());

    verify(mockPersonService, times(1)).newPerson(Mockito.isA(PersonDto.class));
    verify(mockPersonDto, times(1)).getId();
    verify(mockPersonDto, times(1)).getFirst_name();
    verify(mockPersonDto, times(1)).getLast_name();
    verify(mockPersonDto, times(1)).getAge();
    verify(mockPersonDto, times(1)).getFavourite_colour();
    verify(mockPersonDto, times(1)).getHobby();

    verifyNoMoreInteractions(mockPersonService, mockPersonDto);
  }

  // TODO: Replicate security tests for all actions!
}