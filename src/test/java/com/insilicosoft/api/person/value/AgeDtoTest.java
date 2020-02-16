package com.insilicosoft.api.person.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test {@link AgeDto} class.
 *
 * @author geoff
 */
public class AgeDtoTest {

  private AgeDto ageDto;
  private Long dummyId;
  private Integer dummyAge;

  @Before
  public void setUp() {
    ageDto = null;

    dummyId = null;
    dummyAge = null;
  }

  @Test
  public void testConstructor() {
    ageDto = new AgeDto(dummyId, dummyAge);

    assertNull(ageDto.getId());
    assertNull(ageDto.getAge());
    assertNotNull(ageDto.toString());

    dummyId = 3L;
    dummyAge = 4;

    ageDto = new AgeDto(dummyId, dummyAge);

    assertEquals(dummyId, ageDto.getId());
    assertEquals(dummyAge, ageDto.getAge());
  }
}