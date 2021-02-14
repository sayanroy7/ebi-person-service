package com.ebi.app.service.impl;

import com.ebi.app.model.Person;
import com.ebi.app.model.web.response.PersonDTO;
import com.ebi.app.model.web.response.Persons;
import com.ebi.app.service.PersonService;
import com.ebi.app.store.PersonStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log4j2
public class PersonServiceImpl implements PersonService {

    private final PersonStore<Person> personStore;

    @Override
    public Optional<PersonDTO> findById(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        var person = personStore.findById(id);
        if (person.isPresent()) {
            var personEntity = person.get();
            return Optional.of(PersonDTO.builder().id(personEntity.getId())
                    .age(personEntity.getAge())
                    .firstName(personEntity.getFirstName()).lastName(personEntity.getLastName())
                    .favouriteColour(personEntity.getFavouriteColour()).build());
        }
        return Optional.empty();
    }

    @Override
    public PersonDTO save(com.ebi.app.model.web.request.PersonDTO personDTO) {
        if (personDTO == null) {
            return null;
        }
        var savedPerson = personStore.save(Person.builder().firstName(personDTO.getFirstName())
                .lastName(personDTO.getLastName()).age(personDTO.getAge())
                .favouriteColour(personDTO.getFavouriteColour()).build());
        return PersonDTO.builder().firstName(savedPerson.getFirstName())
                .lastName(savedPerson.getLastName()).age(savedPerson.getAge())
                .favouriteColour(savedPerson.getFavouriteColour()).build();
    }

    @Override
    public Optional<PersonDTO> update(com.ebi.app.model.web.request.PersonDTO personDTO, String id) {
        if (id == null || id.isBlank() || personDTO == null) {
            return Optional.empty();
        }
        var existingPersonEntity = personStore.findById(id);
        if (existingPersonEntity.isEmpty()) {
            return Optional.empty();
        }
        var existingPerson = existingPersonEntity.get();
        existingPerson.setAge(personDTO.getAge());
        existingPerson.setFirstName(personDTO.getFirstName());
        existingPerson.setLastName(personDTO.getLastName());
        existingPerson.setFavouriteColour(personDTO.getFavouriteColour());
        var updatedPerson = personStore.update(existingPerson);

        if (log.isInfoEnabled()) {
            log.info("Updated person entity : {} with Id: {}", updatedPerson, updatedPerson.getId());
        }
        return Optional.of(PersonDTO.builder().id(updatedPerson.getId()).age(updatedPerson.getAge())
                .firstName(updatedPerson.getFirstName()).lastName(updatedPerson.getLastName())
                .favouriteColour(updatedPerson.getFavouriteColour()).build());
    }

    @Override
    public Optional<PersonDTO> deleteById(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        var person = personStore.findById(id);
        if (person.isPresent()) {
            personStore.deleteById(id);
            var personEntity = person.get();
            return Optional.of(PersonDTO.builder().id(personEntity.getId())
                    .age(personEntity.getAge())
                    .firstName(personEntity.getFirstName()).lastName(personEntity.getLastName())
                    .favouriteColour(personEntity.getFavouriteColour()).build());
        }
        return Optional.empty();
    }

    @Override
    public Persons findAll(int page, int size) {
        var personsEntityPage = personStore.findAll(page, size);
        var personDTOS = personsEntityPage.get().map(personEntity -> PersonDTO.builder().id(personEntity.getId())
                .age(personEntity.getAge())
                .firstName(personEntity.getFirstName()).lastName(personEntity.getLastName())
                .favouriteColour(personEntity.getFavouriteColour()).build())
                .collect(Collectors.toList());
        return Persons.builder().person(personDTOS).currentPage(personsEntityPage.getNumber())
                .totalItems(personsEntityPage.getTotalElements()).totalPages(personsEntityPage.getTotalPages())
                .build();
    }
}
