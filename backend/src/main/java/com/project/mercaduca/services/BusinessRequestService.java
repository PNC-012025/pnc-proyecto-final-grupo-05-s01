package com.project.mercaduca.services;

import com.project.mercaduca.dtos.BusinessRequestCreateDTO;
import com.project.mercaduca.models.BusinessRequest;
import com.project.mercaduca.models.User;
import com.project.mercaduca.repositories.BusinessRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.project.mercaduca.repositories.RoleRepository;
import com.project.mercaduca.repositories.UserRepository;
import com.project.mercaduca.services.EmailService;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class BusinessRequestService {
    private final BusinessRequestRepository businessRequestRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    // Constructor con todas las dependencias inyectadas
    public BusinessRequestService(
            BusinessRequestRepository businessRequestRepository,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService
    ) {
        this.businessRequestRepository = businessRequestRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }


    public BusinessRequest createBusinessRequest(BusinessRequestCreateDTO dto) {
        BusinessRequest request = new BusinessRequest();

        request.setBusinessName(dto.getBusinessName());
        request.setDescription(dto.getDescription());
        request.setSector(dto.getSector());
        request.setProductType(dto.getProductType());
        request.setPriceRange(dto.getPriceRange());
        request.setSocialMedia(dto.getSocialMedia());
        request.setPhone(dto.getPhone());

        // Datos usuario
        request.setUserName(dto.getUserName());
        request.setUserLastName(dto.getUserLastName());
        request.setUserEmail(dto.getUserEmail());
        request.setUserGender(dto.getUserGender());
        request.setUserBirthDate(dto.getUserBirthDate());
        request.setUserFaculty(dto.getUserFaculty());
        request.setUserMajor(dto.getUserMajor());

        // Puedes inicializar estado, fechas, etc.
        request.setStatus("PENDIENTE");
        request.setSubmissionDate(LocalDate.now());

        return businessRequestRepository.save(request);
    }

    public List<BusinessRequest> getAllBusinessRequests() {
        return businessRequestRepository.findAll();
    }

    public void approveRequest(Long requestId) {
        BusinessRequest request = businessRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (!"PENDIENTE".equalsIgnoreCase(request.getStatus())) {
            throw new IllegalStateException("Esta solicitud ya fue procesada");
        }

        String tempPassword = UUID.randomUUID().toString().substring(0, 8);
        String encodedPassword = passwordEncoder.encode(tempPassword);

        User user = new User();
        user.setName(request.getUserName());
        user.setLastName(request.getUserLastName());
        user.setMail(request.getUserEmail());
        user.setGender(request.getUserGender());
        user.setBirthDate(request.getUserBirthDate());
        user.setFaculty(request.getUserFaculty());
        user.setMajor(request.getUserMajor());
        user.setPassword(encodedPassword);
        user.setRole(roleRepository.findByName("ROLE_STUDENT").orElseThrow());

        userRepository.save(user);

        request.setStatus("APROBADO");
        request.setReviewDate(LocalDate.now());
        businessRequestRepository.save(request);

        emailService.send(
                user.getMail(),
                "Tu cuenta en Mercaduca fue aprobada 🎉",
                "Bienvenido a Mercaduca. Tu usuario es: " + user.getMail() +
                        " y tu contraseña temporal es: " + tempPassword
        );
    }

    public void rejectRequest(Long requestId, String reason) {
        BusinessRequest request = businessRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (!"PENDIENTE".equalsIgnoreCase(request.getStatus())) {
            throw new IllegalStateException("Esta solicitud ya fue procesada");
        }

        request.setStatus("RECHAZADO");
        request.setReviewDate(LocalDate.now());

        businessRequestRepository.save(request);

        emailService.send(
                request.getUserEmail(),
                "Tu solicitud a Mercaduca fue rechazada 😢",
                "Lamentamos informarte que tu solicitud ha sido rechazada. Motivo: " + reason
        );
    }


}