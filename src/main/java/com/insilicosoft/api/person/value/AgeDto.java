package com.insilicosoft.api.person.value;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Public-facing <acronym title="Data Transfer Object">DTO</acronym>
 * representation of an entity's age.
 *
 * @author geoff
 */
public class AgeDto {

  // Don't show the id in Swagger UI's.
  // TODO: Satisfy self why I have to declare id's in DTOs (for object mapping purposes).
  @JsonIgnore
  private Long id;
  private Integer age;

  /**
   * Initialising constructor.
   * 
   * @param id
   * @param age
   */
  public AgeDto(final Long id, final Integer age) {
    setId(id);
    setAge(age);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "AgeDto [id=" + id + ", age=" + age + "]";
  }

  /**
   * Retrieve the application identifier.
   * 
   * @return Application identifier (or {@code null} if unassigned).
   */
  public Long getId() {
    return id;
  }
 
  /**
   * Assign the application identifier.
   * 
   * @param id Application identifier to set
   */
  public void setId(final Long id) {
    this.id = id;
  }

  /**
   * Retrieve the age.
   * 
   * @return The age.
   */
  public Integer getAge() {
    return age;
  }

  /**
   * Assign the age.
   * 
   * @param age The age to set.
   */
  public void setAge(final Integer age) {
    this.age = age;
  }
}