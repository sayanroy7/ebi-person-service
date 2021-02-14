package com.ebi.app.model.web.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
public class WebApiResponse<T> {
    @JsonProperty("data")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    @JsonProperty("error")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ErrorResponse error;

    public static <T> WebApiResponse<T> success(T data) {
        WebApiResponse<T> out = new WebApiResponse<>();
        out.setData(data);
        return out;
    }

    public static <T> WebApiResponse<T> error(ErrorResponse errorResponse) {
        WebApiResponse<T> out = new WebApiResponse<>();
        out.setError(errorResponse);
        return out;
    }

    public ResponseEntity<WebApiResponse<T>> toResponse() {
        return data != null ? new ResponseEntity<>(this, HttpStatus.OK)
                : new ResponseEntity<>(this, error.getStatusCode());
    }

}
