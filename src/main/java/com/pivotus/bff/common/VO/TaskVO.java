package com.pivotus.bff.common.VO;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class TaskVO {
    @NotNull
    private String name;
    @NotNull
    private String description;
}
