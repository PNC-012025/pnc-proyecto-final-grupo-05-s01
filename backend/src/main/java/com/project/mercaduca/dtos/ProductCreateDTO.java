package com.project.mercaduca.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductCreateDTO {

    @NotBlank(message = "El nombre del producto es obligatorio")
    private String name;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    @NotNull(message = "El stock es obligatorio")
    private Integer stock;

    private String urlImage; // opcional

    @NotNull(message = "Debe asociarse a un usuario")
    private Long userId;

    @NotNull(message = "Debe asociarse a una categoría")
    private Long categoryId;
}
