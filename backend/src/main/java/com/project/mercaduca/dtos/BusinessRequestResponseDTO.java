package com.project.mercaduca.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BusinessRequestResponseDTO {
    private Long id;
    private String businessName;
    private String description;
    private String status;
    private LocalDate submissionDate;
    private LocalDate reviewDate;
    private String sector;
    private String productType;
    private String priceRange;
    private String socialMedia;
    private String phone;

    private String userName;
    private String userLastName;
    private String userEmail;
}