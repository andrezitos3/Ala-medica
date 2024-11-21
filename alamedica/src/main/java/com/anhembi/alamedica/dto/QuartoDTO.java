package com.anhembi.alamedica.dto;

import jakarta.validation.constraints.NotBlank;

public class QuartoDTO {

    @NotBlank(message = "O número do quarto é obrigatório.")
    private String numero;
    
}
