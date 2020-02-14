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
public class PersonControllerSecurityIT {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PersonService mockPersonService;

  private static final String urlTemplatePersons = "/persons";
  private static final String dummyUserName = "dummyUserName";
  private static final String dummyPassword = "dummyPassword";
  private static final String dummyName = "dummyName";
  private static final Long dummyPersonId = 1L;

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
                    .content("{ \"name\" : \"" + dummyName + "\" }"))
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
    when(mockPersonDto.getName()).thenReturn(dummyName);

    mockMvc.perform(post(urlTemplatePersons)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8)
                    .content("{ \"name\" : \"" + dummyName + "\" }"))
           //.andDo(print())
           .andExpect(status().isCreated());

    verify(mockPersonService, times(1)).newPerson(Mockito.isA(PersonDto.class));
    verify(mockPersonDto, times(1)).getId();
    verify(mockPersonDto, times(1)).getName();

    verifyNoMoreInteractions(mockPersonService, mockPersonDto);
  }
}