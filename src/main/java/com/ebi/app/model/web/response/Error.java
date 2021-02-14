package com.ebi.app.model.web.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class Error {
    @JsonProperty(value = "fieldName", access = JsonProperty.Access.READ_ONLY)
    private String fieldName;

    @JsonProperty(value = "defaultMessage", access = JsonProperty.Access.READ_ONLY)
    private String defaultMessage;

    @JsonProperty(value = "errorMessage", access = JsonProperty.Access.READ_ONLY)
    private String errorMessage;
}
