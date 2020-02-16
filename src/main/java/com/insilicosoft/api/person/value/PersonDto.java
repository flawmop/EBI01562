package com.insilicosoft.api.person.value;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.insilicosoft.api.person.entity.Person;

/**
 * Public-facing <acronym title="Data Transfer Object">DTO</acronym>
 * representation for {@link Person} entities.
 * 
 * @author geoff
 */
public class PersonDto {

  // Don't show the id in Swagger UI's.
  @JsonIgnore
  private Long id;

  private String first_name;
  private String last_name;
  private Integer age;
  private String favourite_colour;
  private String[] hobby = {};

  /**
   * Initialising constructor.
   * 
   * @param id Person id.
   * @param first_name First name.
   * @param last_name Last name.
   * @param age Age.
   * @param favourite_colour Favourite colour.
   * @param hobby Hobbies.
   */
  public PersonDto(final Long id, final String first_name,
                   final String last_name, final Integer age,
                   final String favouriteColour, final String[] hobby) {
    setId(id);
    setFirst_name(first_name);
    setLast_name(last_name);
    setAge(age);
    setFavourite_colour(favouriteColour);
    setHobby(hobby);
  }

  /**
   * Initialising constructor from domain object.
   * 
   * @param person Domain object representation.
   */
  public PersonDto(final Person person) {
    this(person.getId(), person.getFirstName(), person.getLastName(),
         person.getAge(), person.getFavouriteColour(), person.getHobbies());
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "PersonDto [id=" + id + ", first_name=" + first_name + ", last_name="
        + last_name + ", age=" + age + ", favourite_colour=" + favourite_colour
        + ", hobby=" + Arrays.toString(hobby) + "]";
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
   * Retrieve the first name.
   * 
   * @return First name (or {@code null} if unassigned).
   */
  public String getFirst_name() {
    return first_name;
  }

  /**
   * Assign the first name.
   * 
   * @param first_name First name to assign.
   */
  public void setFirst_name(final String first_name) {
    this.first_name = first_name;
  }

  /**
   * Retrieve the last name.
   * 
   * @return Last name (or {@code null} if unassigned).
   */
  public String getLast_name() {
    return last_name;
  }

  /**
   * Assign the last name.
   * 
   * @param last_name Last name to assign.
   */
  public void setLast_name(final String last_name) {
    this.last_name = last_name;
  }

  /**
   * Retrieve the age.
   * 
   * @return The age (or {@code null} if unassigned).
   */
  public Integer getAge() {
    return age;
  }

  /**
   * Assign the age.
   * 
   * @param age Age to assign.
   */
  public void setAge(final Integer age) {
    this.age = age;
  }

  /**
   * Retrieve the favourite colour.
   * 
   * @return Favourite colour (or {@code null} if unassigned).
   */
  public String getFavourite_colour() {
    return favourite_colour;
  }

  /**
   * Assign the favourite colour.
   * 
   * @param favourite_colour Favourite colour to assign.
   */
  public void setFavourite_colour(final String favourite_colour) {
    this.favourite_colour = favourite_colour;
  }

  /**
   * Retrieve the hobbies.
   * 
   * @return The hobbies (or {@code null} if unassigned).
   */
  public String[] getHobby() {
    return hobby;
  }

  /**
   * Assign the hobbies.
   * 
   * @param hobby Hobbies to assign.
   */
  public void setHobby(final String[] hobby) {
    this.hobby = hobby;
  }
}