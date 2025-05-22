package com.project.mercaduca.services;

import com.project.mercaduca.dtos.BusinessUpdateDTO;
import com.project.mercaduca.models.Business;
import com.project.mercaduca.models.User;
import com.project.mercaduca.repositories.BusinessRepository;
import com.project.mercaduca.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class BusinessService {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private AuthService authService;

    public void updateBusiness(BusinessUpdateDTO dto) {
        User currentUser = authService.getAuthenticatedUser();
        Business business = businessRepository.findByOwner(currentUser)
                .orElseThrow(() -> new RuntimeException("No se encontró negocio del usuario"));

        business.setBusinessName(dto.getBusinessName());
        business.setDescription(dto.getDescription());
        business.setSector(dto.getSector());
        business.setProductType(dto.getProductType());
        business.setPriceRange(dto.getPriceRange());
        business.setSocialMedia(dto.getSocialMedia());
        business.setPhone(dto.getPhone());

        if (dto.getUrlLogo() != null) {
            business.setUrlLogo(dto.getUrlLogo());
        }

        businessRepository.save(business);
    }
}
