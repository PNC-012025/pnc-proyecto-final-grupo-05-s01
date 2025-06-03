package com.project.mercaduca.controllers;

import com.project.mercaduca.dtos.ProductApprovalRequestDTO;
import com.project.mercaduca.dtos.ProductCreateDTO;
import com.project.mercaduca.dtos.ProductReviewDTO;
import com.project.mercaduca.models.Product;
import com.project.mercaduca.models.ProductApproval;
import com.project.mercaduca.models.User;
import com.project.mercaduca.repositories.ProductApprovalRepository;
import com.project.mercaduca.repositories.ProductRepository;
import com.project.mercaduca.repositories.UserRepository;
import com.project.mercaduca.services.CloudinaryService;
import com.project.mercaduca.services.EmailService;
import com.project.mercaduca.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductApprovalRepository productApprovalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    /*
    @PostMapping
    @PreAuthorize("hasRole('EMPRENDEDOR')")
    public ResponseEntity<?> createProduct(
            @RequestParam("image") MultipartFile image,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("stock") int stock,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("price") Double price,
            @RequestParam("businessId") Long businessId
    ) {
        try {
            String imageUrl = cloudinaryService.uploadImage(image);

            ProductCreateDTO dto = new ProductCreateDTO(name, description, stock, imageUrl, categoryId, price);

            productService.createProduct(dto, businessId);

            return ResponseEntity.ok("Producto enviado para aprobación.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear producto: " + e.getMessage());
        }
    }*/
    @PostMapping
    @PreAuthorize("hasRole('EMPRENDEDOR')")
    public ResponseEntity<?> createProduct(
            @RequestParam("image") MultipartFile image,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("stock") int stock,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("price") Double price
    ) {
        try {
            String imageUrl = cloudinaryService.uploadImage(image);
            ProductCreateDTO dto = new ProductCreateDTO(name, description, stock, imageUrl, categoryId, price);

            productService.createProduct(dto);

            return ResponseEntity.ok("Producto enviado para aprobación.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear producto: " + e.getMessage());
        }
    }

    @GetMapping("/business/{businessId}/approved")
    public ResponseEntity<?> getBusinessWithApprovedProducts(@PathVariable Long businessId) {
        try {
            return ResponseEntity.ok(productService.getBusinessWithApprovedProducts(businessId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}/review")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> reviewProduct(
            @PathVariable Long id,
            @RequestBody ProductReviewDTO reviewDTO
    ) {
        try {
            productService.reviewProduct(id, reviewDTO.isAprobado(), reviewDTO.getRemarks());
            return ResponseEntity.ok("Producto " + (reviewDTO.isAprobado() ? "aprobado" : "rechazado") + " exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al revisar producto: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllApprovedProducts() {
        return ResponseEntity.ok(productService.getApprovedProducts());
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getPendingProducts(@RequestParam Long businessId) {
        return ResponseEntity.ok(productService.getPendingProducts(businessId));
    }

    @GetMapping("/business/{businessId}")
    @PreAuthorize("hasRole('EMPRENDEDOR')")
    public ResponseEntity<?> getProductsByBusiness(@PathVariable Long businessId) {
        return ResponseEntity.ok(productService.getProductsByBusiness(businessId));
    }

    @PostMapping("/approve-batch")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveProductsBatch(@RequestBody ProductApprovalRequestDTO request) {
        List<Product> approvedProducts = productRepository.findAllById(request.getApprovedProductIds());
        List<Product> rejectedProducts = productRepository.findAllById(request.getRejectedProductIds());

        LocalDate now = LocalDate.now();

        for (Product product : approvedProducts) {
            product.setStatus("APROBADO");

            ProductApproval approval = productApprovalRepository.findByProduct(product)
                    .orElse(new ProductApproval());
            approval.setProduct(product);
            approval.setStatus("APROBADO");
            approval.setReviewDate(now);
            approval.setRemarks(request.getRemark());
            productApprovalRepository.save(approval);
        }

        for (Product product : rejectedProducts) {
            product.setStatus("RECHAZADO");

            ProductApproval approval = productApprovalRepository.findByProduct(product)
                    .orElse(new ProductApproval());
            approval.setProduct(product);
            approval.setStatus("RECHAZADO");
            approval.setReviewDate(now);
            approval.setRemarks(request.getRemark());
            productApprovalRepository.save(approval);
        }

        productRepository.saveAll(approvedProducts);
        productRepository.saveAll(rejectedProducts);

        Optional<User> optionalUser = userRepository.findById(request.getUserId());
        optionalUser.ifPresent(user -> {
            emailService.sendProductApprovalSummaryEmail(user, approvedProducts, rejectedProducts, request.getRemark());
        });

        return ResponseEntity.ok("Productos procesados correctamente.");
    }




}
