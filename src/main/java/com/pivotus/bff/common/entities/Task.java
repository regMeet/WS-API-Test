package com.pivotus.bff.common.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Task {
    private Long id;
    private String name;
    private String description;
    private boolean done;
    @JsonIgnore
    private boolean deleted;
}
