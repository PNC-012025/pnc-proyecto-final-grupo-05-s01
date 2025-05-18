package com.project.mercaduca.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Integer stock;
    private String status;
    private String urlImage;
    private String userName;
    private String categoryName;
}
