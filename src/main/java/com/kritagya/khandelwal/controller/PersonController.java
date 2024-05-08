package com.kritagya.khandelwal.controller;

import com.kritagya.khandelwal.models.Person;
import com.kritagya.khandelwal.services.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/person")
@RequiredArgsConstructor
@Slf4j
public class PersonController {

  private final PersonService personService;

  @PostMapping("/")
  @PreAuthorize("hasRole('ROLE_CustomRole')")
  public Mono<Person> postPerson(@RequestBody Person person) {
    return personService.savePerson(person);
  }

  @GetMapping("/")
  @PreAuthorize("hasRole('ROLE_CustomRole')")
  public Flux<Person> getAllPerson() {
    return personService.findAll();
  }

  @GetMapping(value = "/download-csv")
  @PreAuthorize("hasRole('ROLE_CustomRole')")
  public Mono<ResponseEntity<Flux<DataBuffer>>> downloadCsvFlux() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.parseMediaType("text/csv"));
    httpHeaders.setContentDispositionFormData("attachment", "sample.csv");
    return Mono.just(ResponseEntity.ok()
      .headers(httpHeaders)
      .body(personService.exportCsv()));
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ROLE_CustomRole')")
  public Mono<Person> getPerson(@PathVariable("id") String id) {
    return personService.findById(id);
  }

  @PostMapping(value = "/bulk", consumes = {"multipart/form-data"})
  @PreAuthorize("hasRole('ROLE_CustomRole')")
  public Flux<Person> bulkPostPerson(@RequestPart("file") Mono<MultipartFile> file) {
    return personService.bulkSavePerson(file);
  }

}
