package com.project.mercaduca.controllers;

import com.project.mercaduca.dtos.BusinessRequestCreateDTO;
import com.project.mercaduca.dtos.BusinessRequestResponseDTO;
import com.project.mercaduca.dtos.BusinessSummaryDTO;
import com.project.mercaduca.dtos.RejectRequestDTO;
import com.project.mercaduca.models.BusinessRequest;
import com.project.mercaduca.services.BusinessRequestService;
import com.project.mercaduca.services.CloudinaryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/business-requests")
public class BusinessRequestController {

    @Autowired
    private CloudinaryService cloudinaryService;

    private final BusinessRequestService businessRequestService;

    public BusinessRequestController(CloudinaryService cloudinaryService, BusinessRequestService businessRequestService) {
        this.cloudinaryService = cloudinaryService;
        this.businessRequestService = businessRequestService;
    }

    @PostMapping
    public ResponseEntity<?> createRequest(
            @RequestParam("logo") MultipartFile logo,
            @RequestParam("businessName") String businessName,
            @RequestParam("description") String description,
            @RequestParam("sector") String sector,
            @RequestParam("productType") String productType,
            @RequestParam("priceRange") String priceRange,
            @RequestParam("socialMedia") String socialMedia,
            @RequestParam("phone") String phone,
            @RequestParam("userName") String userName,
            @RequestParam("userLastName") String userLastName,
            @RequestParam("userEmail") String userEmail,
            @RequestParam("userGender") String userGender,
            @RequestParam("userBirthDate") String userBirthDate, // formato: yyyy-MM-dd
            @RequestParam("userFaculty") String userFaculty,
            @RequestParam("userMajor") String userMajor
    ) {
        try {
            String logoUrl = cloudinaryService.uploadImage(logo);

            BusinessRequestCreateDTO dto = new BusinessRequestCreateDTO();
            dto.setBusinessName(businessName);
            dto.setDescription(description);
            dto.setSector(sector);
            dto.setProductType(productType);
            dto.setPriceRange(priceRange);
            dto.setSocialMedia(socialMedia);
            dto.setPhone(phone);
            dto.setUrlLogo(logoUrl);
            dto.setUserName(userName);
            dto.setUserLastName(userLastName);
            dto.setUserEmail(userEmail);
            dto.setUserGender(userGender);
            dto.setUserBirthDate(LocalDate.parse(userBirthDate));
            dto.setUserFaculty(userFaculty);
            dto.setUserMajor(userMajor);

            businessRequestService.createBusinessRequest(dto);

            return ResponseEntity.ok("Solicitud enviada correctamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear solicitud: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<BusinessRequestResponseDTO>> getAllRequests() {
        List<BusinessRequest> requests = businessRequestService.getAllBusinessRequests();

        List<BusinessRequestResponseDTO> responseDTOs = requests.stream().map(req -> {
            BusinessRequestResponseDTO dto = new BusinessRequestResponseDTO();
            dto.setId(req.getId());
            dto.setUrlLogo(req.getUrlLogo());
            dto.setBusinessName(req.getBusinessName());
            dto.setDescription(req.getDescription());
            dto.setStatus(req.getStatus());
            dto.setSubmissionDate(req.getSubmissionDate());
            dto.setSector(req.getSector());
            dto.setProductType(req.getProductType());
            dto.setPriceRange(req.getPriceRange());
            dto.setSocialMedia(req.getSocialMedia());
            dto.setPhone(req.getPhone());
            dto.setUserName(req.getUserName());
            dto.setUserLastName(req.getUserLastName());
            dto.setUserEmail(req.getUserEmail());
            return dto;
        }).collect(Collectors.toList());

        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<String> approveRequest(@PathVariable Long id) {
        businessRequestService.approveRequest(id);
        return new ResponseEntity<>("Solicitud aprobada y usuario creado exitosamente.", HttpStatus.OK);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<String> rejectRequest(@PathVariable Long id, @RequestBody RejectRequestDTO dto) {
        businessRequestService.rejectRequest(id, dto.getReason());
        return new ResponseEntity<>("Solicitud rechazada.", HttpStatus.OK);
    }


    @GetMapping("/approved-summary")
    public List<BusinessSummaryDTO> getApprovedBusinessSummaries() {
        return businessRequestService.getApprovedBusinessSummaries();
    }
}
