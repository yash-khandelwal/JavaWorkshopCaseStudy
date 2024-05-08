package com.kritagya.khandelwal.services;

import com.kritagya.khandelwal.models.Person;
import com.kritagya.khandelwal.models.PersonCsvRepresentation;
import com.kritagya.khandelwal.repositories.PersonRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonService {

  private final PersonRepository personRepository;

  public Mono<Person> savePerson(Person person) {
    return personRepository.save(person);
  }

  public Mono<Person> findById(String id) {
    return personRepository.findById(id);
  }

  public Flux<Person> findAll() {
    return personRepository.findAll();
  }

  public Flux<Person> bulkSavePerson(Mono<MultipartFile> file) {
    Flux<Person> persons = parseCSV(file);
    return personRepository.saveAll(persons);
  }

  private Flux<Person> parseCSV(Mono<MultipartFile> file) {
    return file.flatMapMany(multipartFile -> {
      try(Reader reader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()))) {
        HeaderColumnNameMappingStrategy<PersonCsvRepresentation> strategy =
          new HeaderColumnNameMappingStrategy<>();
        strategy.setType(PersonCsvRepresentation.class);
        CsvToBean<PersonCsvRepresentation> csvToBean =
          new CsvToBeanBuilder<PersonCsvRepresentation>(reader)
            .withMappingStrategy(strategy)
            .withIgnoreEmptyLine(true)
            .withIgnoreLeadingWhiteSpace(true)
            .build();
        return Flux.fromIterable(csvToBean.parse().stream()
          .map(csvLine -> Person.builder()
            .name(csvLine.getName())
            .email(csvLine.getEmail())
            .contact(csvLine.getContact())
            .age(csvLine.getAge())
            .build())
          .collect(Collectors.toSet()));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }

  public Flux<DataBuffer> exportCsv() {

    return personRepository.findAll()
      .map(Person::getCsv)
      .reduce((p1, p2) -> String.join("\n", List.of(p1, p2)))
      .flatMapMany(content -> Flux.just(content.getBytes())
          .map(DefaultDataBufferFactory.sharedInstance::wrap)
      );
  }
}
