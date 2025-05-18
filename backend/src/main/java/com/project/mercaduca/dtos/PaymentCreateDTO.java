package com.project.mercaduca.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class PaymentCreateDTO {

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate date;

    @NotNull(message = "El monto es obligatorio")
    private Double amount;

    private String remarks;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;
}
