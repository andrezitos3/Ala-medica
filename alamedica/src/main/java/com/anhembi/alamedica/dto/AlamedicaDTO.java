package com.anhembi.alamedica.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlamedicaDTO {

    @NotNull(message = "O andar é obrigatório.")
    private int andar;


    private Integer 
}
