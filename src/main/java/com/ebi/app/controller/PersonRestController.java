package com.ebi.app.controller;

import com.ebi.app.model.web.response.Error;
import com.ebi.app.model.web.response.*;
import com.ebi.app.service.PersonService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
@Api(value = "/person", protocols = "HTTP", tags = { "Person Rest Controller" })
public class PersonRestController {

    private final PersonService personService;

    @ApiOperation(value = "Get person by Id", notes = "The caller invokes this URL to retrieve a person with specific Id "
            , response = WebApiResponse.class, tags = { "Person Rest Controller" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The Person retrieved successfully.", response = PersonDTO.class),
            @ApiResponse(code = 404, message = "The Person doesn't exist", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class) })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ebi:user', 'ebi:admin')")
    public ResponseEntity<WebApiResponse<PersonDTO>> getPerson(
            @ApiParam(name = "id", value = "id of the person to be retrieved", required = true, example = "634dfdedrdgdret334344df")
            @PathVariable String id) {
        var personDTO = personService.findById(id);
        if (personDTO.isEmpty()) {
            return new ResponseEntity<>(WebApiResponse.error(ErrorResponse.builder()
                    .message("Not Found")
                    .statusCode(HttpStatus.NOT_FOUND)
                    .build()) ,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(WebApiResponse.success(personDTO.get()), HttpStatus.OK);
    }

    @ApiOperation(value = "Get paginated list of persons", notes = "The caller invokes this URL to retrieve all persons as paginated query."
            , response = WebApiResponse.class, tags = { "Person Rest Controller" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Persons page retrieved successfully.", response = Persons.class),
            @ApiResponse(code = 404, message = "Persons do not exist", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class) })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ebi:user', 'ebi:admin')")
    public ResponseEntity<WebApiResponse<Persons>> getPersons(
            @ApiParam(name = "page", value = "page number to query the available persons", defaultValue = "0")
            @RequestParam(defaultValue = "0") int page,
            @ApiParam(name = "size", value = "number of persons to be included in each page", defaultValue = "10")
            @RequestParam(defaultValue = "10") int size) {
        var persons = personService.findAll(page, size);
        if (persons == null || persons.getPerson() == null || persons.getPerson().isEmpty()) {
            return new ResponseEntity<>(WebApiResponse.error(ErrorResponse.builder()
                    .message("Not Found")
                    .statusCode(HttpStatus.NOT_FOUND)
                    .build()) ,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(WebApiResponse.success(persons), HttpStatus.OK);
    }

    @ApiOperation(value = "Save/Create a person", notes = "The caller invokes this URL to save a person "
            , response = WebApiResponse.class, tags = { "Person Rest Controller" })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The Person created/saved successfully.", response = PersonDTO.class),
            @ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class) })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ebi:admin')")
    public ResponseEntity<WebApiResponse<PersonDTO>> savePerson(
            @ApiParam(name = "person", value = "PersonDTO", required = true, type = "com.ebi.app.model.web.request.PersonDTO")
            @RequestBody @Valid com.ebi.app.model.web.request.PersonDTO personRequest,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return getBindingErrorResponse(bindingResult);
        }
        return new ResponseEntity<>(WebApiResponse.success(personService.save(personRequest)), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update a person by Id", notes = "The caller invokes this URL to update a person by Id and Payload"
            , response = WebApiResponse.class, tags = { "Person Rest Controller" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The Person updated successfully.", response = PersonDTO.class),
            @ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "Person not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class) })
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ebi:admin')")
    public ResponseEntity<WebApiResponse<PersonDTO>> updatePerson(
            @ApiParam(name = "id", value = "id of the person to be updated", required = true, example = "634dfdedrdgdret334344df")
            @PathVariable String id,
            @ApiParam(name = "personDTO", value = "PersonDTO", required = true, type = "com.ebi.app.model.web.request.PersonDTO")
            @RequestBody @Valid com.ebi.app.model.web.request.PersonDTO personRequest,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return getBindingErrorResponse(bindingResult);
        }
        var updatedPersonDTO = personService.update(personRequest, id);
        if (updatedPersonDTO.isEmpty()) {
            return new ResponseEntity<>(WebApiResponse.error(ErrorResponse.builder()
                    .message("Not Found")
                    .statusCode(HttpStatus.NOT_FOUND)
                    .build()) ,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(WebApiResponse.success(updatedPersonDTO.get()), HttpStatus.OK);
    }

    @ApiOperation(value = "Delete person by Id", notes = "The caller invokes this URL to delete a person with specific Id "
            , response = WebApiResponse.class, tags = { "Person Rest Controller" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The Person is deleted successfully.", response = PersonDTO.class),
            @ApiResponse(code = 404, message = "The Person doesn't exist", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class) })
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ebi:admin')")
    public ResponseEntity<WebApiResponse<PersonDTO>> deletePerson(
            @ApiParam(name = "id", value = "id of the person to be deleted", required = true, example = "634dfdedrdgdret334344df")
            @PathVariable String id) {
        var deletedPersonDTO = personService.deleteById(id);
        if (deletedPersonDTO.isEmpty()) {
            return new ResponseEntity<>(WebApiResponse.error(ErrorResponse.builder()
                    .message("Not Found")
                    .statusCode(HttpStatus.NOT_FOUND)
                    .build()) ,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(WebApiResponse.success(deletedPersonDTO.get()), HttpStatus.OK);
    }

    private ResponseEntity<WebApiResponse<PersonDTO>> getBindingErrorResponse(BindingResult bindingResult) {
        return new ResponseEntity<>(WebApiResponse.error(ErrorResponse.builder().statusCode(HttpStatus.BAD_REQUEST)
                .message("Invalid payload")
                .bindingErrors(bindingResult.getFieldErrors().parallelStream().map(fieldError -> Error.builder()
                        .fieldName(fieldError.getField())
                        .defaultMessage(fieldError.getDefaultMessage())
                        .errorMessage(fieldError.toString())
                        .build()).collect(Collectors.toList()))
                .build()), HttpStatus.BAD_REQUEST);
    }

}
