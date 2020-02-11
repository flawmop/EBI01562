package com.insilicosoft.api.person.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.insilicosoft.api.person.dao.jpa.PersonRepository;

/**
 * Integration test the Person API.
 *
 * @author geoff
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PersonControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PersonRepository mockRepository;

  @Test
  public void testGetOnEmptyDataset() throws Exception {
    mockMvc.perform(get("/persons"))
           .andDo(print())
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(0)));
  }
}