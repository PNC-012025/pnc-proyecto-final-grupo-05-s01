package com.project.mercaduca.services;

import com.project.mercaduca.dtos.ProductCreateDTO;
import com.project.mercaduca.models.*;
import com.project.mercaduca.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductPriceRepository productPriceRepository;

    @Autowired
    private ProductApprovalRepository productApprovalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public void createProduct(ProductCreateDTO dto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow();

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setStock(dto.getStock());
        product.setUrlImage(dto.getUrlImage());
        product.setStatus("PENDIENTE");
        product.setUser(user);
        product.setCategory(category);
        product = productRepository.save(product);

        ProductPrice price = new ProductPrice();
        price.setPrice(dto.getPrice());
        price.setStartDate(LocalDate.now());
        price.setProduct(product);
        productPriceRepository.save(price);

        ProductApproval approval = new ProductApproval();
        approval.setStatus("PENDIENTE");
        approval.setReviewDate(null);
        approval.setRemarks(null);
        approval.setProduct(product);
        productApprovalRepository.save(approval);
    }

    @Transactional
    public void reviewProduct(Long productId, boolean aprobado, String remarks) {
        Product product = productRepository.findById(productId).orElseThrow();
        ProductApproval approval = productApprovalRepository.findByProduct(product)
                .orElseThrow();

        approval.setStatus(aprobado ? "APROBADO" : "RECHAZADO");
        approval.setReviewDate(LocalDate.now());
        approval.setRemarks(remarks);
        productApprovalRepository.save(approval);

        product.setStatus(approval.getStatus());
        productRepository.save(product);
    }

    public List<Product> getApprovedProducts() {
        return productRepository.findByStatus("APROBADO");
    }

    public List<Product> getPendingProducts() {
        return productRepository.findByStatus("PENDIENTE");
    }

    public List<Product> getProductsByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return productRepository.findByUser(user);
    }


}
