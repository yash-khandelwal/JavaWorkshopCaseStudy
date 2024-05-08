package com.kritagya.khandelwal.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.opencsv.bean.CsvBindByName;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonCsvRepresentation {

  @CsvBindByName(column = "name")
  private String name;
  @CsvBindByName(column = "email")
  private String email;
  @CsvBindByName(column = "contact")
  private String contact;
  @CsvBindByName(column = "age")
  private Integer age;

}
