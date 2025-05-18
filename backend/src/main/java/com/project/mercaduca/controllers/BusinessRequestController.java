package com.project.mercaduca.controllers;

import com.project.mercaduca.dtos.BusinessRequestCreateDTO;
import com.project.mercaduca.dtos.BusinessRequestResponseDTO;
import com.project.mercaduca.dtos.RejectRequestDTO;
import com.project.mercaduca.models.BusinessRequest;
import com.project.mercaduca.services.BusinessRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/business-requests")
public class BusinessRequestController {

    private final BusinessRequestService businessRequestService;

    public BusinessRequestController(BusinessRequestService businessRequestService) {
        this.businessRequestService = businessRequestService;
    }

    @PostMapping
    public ResponseEntity<BusinessRequestResponseDTO> createRequest(@Valid @RequestBody BusinessRequestCreateDTO dto) {
        BusinessRequest savedRequest = businessRequestService.createBusinessRequest(dto);

        // Mapea a DTO de respuesta (puedes usar MapStruct o manual)
        BusinessRequestResponseDTO responseDTO = new BusinessRequestResponseDTO();
        responseDTO.setId(savedRequest.getId());
        responseDTO.setBusinessName(savedRequest.getBusinessName());
        responseDTO.setDescription(savedRequest.getDescription());
        responseDTO.setStatus(savedRequest.getStatus());
        responseDTO.setSubmissionDate(savedRequest.getSubmissionDate());
        responseDTO.setSector(savedRequest.getSector());
        responseDTO.setProductType(savedRequest.getProductType());
        responseDTO.setPriceRange(savedRequest.getPriceRange());
        responseDTO.setSocialMedia(savedRequest.getSocialMedia());
        responseDTO.setPhone(savedRequest.getPhone());

        responseDTO.setUserName(savedRequest.getUserName());
        responseDTO.setUserLastName(savedRequest.getUserLastName());
        responseDTO.setUserEmail(savedRequest.getUserEmail());

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BusinessRequestResponseDTO>> getAllRequests() {
        List<BusinessRequest> requests = businessRequestService.getAllBusinessRequests();

        List<BusinessRequestResponseDTO> responseDTOs = requests.stream().map(req -> {
            BusinessRequestResponseDTO dto = new BusinessRequestResponseDTO();
            dto.setId(req.getId());
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



}
