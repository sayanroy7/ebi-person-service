package com.ebi.app.service;

import com.ebi.app.model.web.response.PersonDTO;
import com.ebi.app.model.web.response.Persons;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PersonService {

    Optional<PersonDTO> findById(String id);

    PersonDTO save(com.ebi.app.model.web.request.PersonDTO personDTO);

    Optional<PersonDTO> update(com.ebi.app.model.web.request.PersonDTO personDTO, String id);

    Optional<PersonDTO> deleteById(String id);

    Persons findAll(int page, int size);


}
