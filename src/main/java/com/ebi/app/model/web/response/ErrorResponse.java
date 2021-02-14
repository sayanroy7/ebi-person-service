package com.ebi.app.model.web.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.util.List;

@Builder
public class ErrorResponse {
    @JsonProperty("responseStatus")
    private HttpStatus statusCode;

    @JsonProperty("errorMessage")
    private String message;

    @JsonProperty("bindingErrors")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Error> bindingErrors;

    HttpStatus getStatusCode() {
        return statusCode;
    }

}
