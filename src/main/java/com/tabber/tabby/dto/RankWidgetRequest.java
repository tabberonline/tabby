package com.tabber.tabby.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankWidgetRequest {

    @JsonProperty("website_id")
    Integer websiteId;

    @JsonProperty("rank")
    Integer rank;

    @JsonProperty("username")
    String username;
}