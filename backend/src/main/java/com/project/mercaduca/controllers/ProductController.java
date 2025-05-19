package com.project.mercaduca.controllers;

import com.project.mercaduca.dtos.ProductCreateDTO;
import com.project.mercaduca.dtos.ProductReviewDTO;
import com.project.mercaduca.services.CloudinaryService;
import com.project.mercaduca.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('EMPRENDEDOR')")
    public ResponseEntity<?> createProduct(
            @RequestParam("image") MultipartFile image,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("stock") int stock,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("price") Double price,
            @RequestParam("userId") Long userId
    ) {
        try {
            String imageUrl = cloudinaryService.uploadImage(image);

            ProductCreateDTO dto = new ProductCreateDTO(name, description, stock, imageUrl, categoryId, price);

            productService.createProduct(dto, userId);

            return ResponseEntity.ok("Producto enviado para aprobación.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear producto: " + e.getMessage());
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
    public ResponseEntity<?> getPendingProducts() {
        return ResponseEntity.ok(productService.getPendingProducts());
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('EMPRENDEDOR')")
    public ResponseEntity<?> getProductsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(productService.getProductsByUser(userId));
    }


}
