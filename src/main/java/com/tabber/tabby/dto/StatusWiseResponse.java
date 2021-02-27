package com.tabber.tabby.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class StatusWiseResponse {
    @JsonProperty("status")
    private String status;

    @JsonProperty( "message")
    private String message;

    @JsonProperty( "detailed_message")
    private String detailed_message;

    @JsonProperty("data")
    private JsonNode data;
}
