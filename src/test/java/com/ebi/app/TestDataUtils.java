package com.ebi.app;

import com.ebi.app.model.Person;
import com.ebi.app.model.web.request.PersonDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class TestDataUtils {

    public static List<Person> getPersons(ObjectMapper objectMapper) throws IOException {
        var personJson = Files
                .lines(Paths.get("src/test/resources/persons.json"), StandardCharsets.UTF_8)
                .collect(Collectors.joining());
        var personDTOS = objectMapper.readValue(personJson, new TypeReference<List<PersonDTO>>() {});
        return personDTOS.stream().map(p -> Person.builder()
                .firstName(p.getFirstName()).lastName(p.getLastName())
                .age(p.getAge()).favouriteColour(p.getFavouriteColour())
                .build()).collect(Collectors.toList());
    }

    public static List<PersonDTO> getPersonsDTO(ObjectMapper objectMapper) throws IOException {
        var personJson = Files
                .lines(Paths.get("src/test/resources/persons.json"), StandardCharsets.UTF_8)
                .collect(Collectors.joining());
        return objectMapper.readValue(personJson, new TypeReference<List<PersonDTO>>() {});
    }

}
