package com.kritagya.khandelwal.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document("person")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Person implements Serializable {

  @Id
  private String id;
  private String name;
  private String email;
  private String contact;
  private Integer age;

  public String getCsv() {
    return String.join(",", List.of(id, name, email, contact, age.toString()));
  }
}
