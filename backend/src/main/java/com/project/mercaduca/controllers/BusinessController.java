package com.project.mercaduca.controllers;

import com.project.mercaduca.dtos.BusinessUpdateDTO;
import com.project.mercaduca.services.BusinessService;
import com.project.mercaduca.services.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/business")
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @PutMapping("/profile")
    public ResponseEntity<String> updateBusinessProfile(
            @RequestParam("businessName") String businessName,
            @RequestParam("description") String description,
            @RequestParam("sector") String sector,
            @RequestParam("productType") String productType,
            @RequestParam("priceRange") String priceRange,
            @RequestParam("socialMedia") String socialMedia,
            @RequestParam("phone") String phone,
            @RequestPart(value = "logo", required = false) MultipartFile logo
    ) {
        try {
            String logoUrl = null;
            if (logo != null && !logo.isEmpty()) {
                logoUrl = cloudinaryService.uploadImage(logo);
            }

            BusinessUpdateDTO dto = new BusinessUpdateDTO();
            dto.setBusinessName(businessName);
            dto.setDescription(description);
            dto.setSector(sector);
            dto.setProductType(productType);
            dto.setPriceRange(priceRange);
            dto.setSocialMedia(socialMedia);
            dto.setPhone(phone);
            dto.setUrlLogo(logoUrl);

            businessService.updateBusiness(dto);

            return ResponseEntity.ok("Negocio actualizado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar negocio: " + e.getMessage());
        }
    }
}