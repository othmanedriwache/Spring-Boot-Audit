package com.demo.service;

import com.auditWriter.annotations.AuditableFunction;
import com.auditWriter.annotations.NotAuditableFunction;
import com.auditWriter.service.auditWriterService.AuditWriterService;
import com.demo.entity.Product;
import com.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
// Note: This service is NOT annotated with @AuditableClass
// So we must use @AuditableFunction on individual methods
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private AuditWriterService auditWriterService;

    // Explicitly auditable function in non-auditable class
    @AuditableFunction
    public Product createProduct(Product product) {
        auditWriterService.logBusinessInfo("Creating new product: " + product.getName());
        
        if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            auditWriterService.logBusinessError("Invalid product price: " + product.getPrice());
            throw new IllegalArgumentException("Product price must be positive");
        }
        
        return productRepository.save(product);
    }

    // Explicitly auditable function
    @AuditableFunction
    public Product getProductById(Long id) throws Exception {
        auditWriterService.logBusinessInfo("Fetching product: " + id);
        return productRepository.findById(id)
                .orElseThrow(() -> new Exception("Product not found: " + id));
    }

    // Not auditable (no annotation in non-auditable class)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Explicitly non-auditable
    @NotAuditableFunction
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    // Auditable function with price range validation
    @AuditableFunction
    public List<Product> searchByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        auditWriterService.logBusinessInfo(
            String.format("Searching products in price range: %s - %s", minPrice, maxPrice)
        );
        
        if (minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("Min price cannot be greater than max price");
        }
        
        return productRepository.findByPriceRange(minPrice, maxPrice);
    }

    // Auditable function with stock management
    @AuditableFunction
    @Transactional
    public Product updateStock(Long productId, Integer quantity, String operation) throws Exception {
        auditWriterService.logBusinessInfo(
            String.format("Updating stock for product %d: %s %d", productId, operation, quantity)
        );
        
        Product product = getProductById(productId);
        
        int currentStock = product.getStockQuantity() != null ? product.getStockQuantity() : 0;
        int newStock;
        
        switch (operation.toUpperCase()) {
            case "ADD":
                newStock = currentStock + quantity;
                break;
            case "SUBTRACT":
                newStock = currentStock - quantity;
                if (newStock < 0) {
                    auditWriterService.logBusinessError("Insufficient stock for product: " + productId);
                    throw new IllegalStateException("Insufficient stock");
                }
                break;
            case "SET":
                newStock = quantity;
                break;
            default:
                throw new IllegalArgumentException("Invalid operation: " + operation);
        }
        
        product.setStockQuantity(newStock);
        
        // Update product status based on stock
        if (newStock == 0) {
            product.setStatus(Product.ProductStatus.OUT_OF_STOCK);
        } else if (product.getStatus() == Product.ProductStatus.OUT_OF_STOCK) {
            product.setStatus(Product.ProductStatus.AVAILABLE);
        }
        
        return productRepository.save(product);
    }

    // Auditable search function
    @AuditableFunction
    public List<Product> searchProducts(String keyword) {
        auditWriterService.logBusinessInfo("Searching products with keyword: " + keyword);
        
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Search keyword cannot be empty");
        }
        
        return productRepository.searchProducts(keyword);
    }

    // Auditable function with discount calculation
    @AuditableFunction
    public Product applyDiscount(Long productId, BigDecimal discountPercentage) throws Exception {
        auditWriterService.logBusinessInfo(
            String.format("Applying %s%% discount to product: %d", discountPercentage, productId)
        );
        
        if (discountPercentage.compareTo(BigDecimal.ZERO) < 0 || 
            discountPercentage.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }
        
        Product product = getProductById(productId);
        product.setDiscountPercentage(discountPercentage);
        
        return productRepository.save(product);
    }

    // Non-auditable bulk update
    @NotAuditableFunction
    public void discontinueProductsByBrand(String brand) {
        List<Product> products = productRepository.findByBrand(brand);
        products.forEach(p -> p.setStatus(Product.ProductStatus.DISCONTINUED));
        productRepository.saveAll(products);
    }

    // Auditable function that tests various exception scenarios
    @AuditableFunction
    public void testProductException(String exceptionType, Long productId) {
        auditWriterService.logBusinessInfo("Testing product exception: " + exceptionType);
        
        switch (exceptionType) {
            case "NOT_FOUND":
                productRepository.findById(999999L)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
                break;
            case "INVALID_PRICE":
                Product product = new Product();
                product.setPrice(new BigDecimal("-100"));
                createProduct(product);
                break;
            case "NULL_POINTER":
                Product nullProduct = null;
                nullProduct.getName();
                break;
            case "ARITHMETIC":
                BigDecimal price = new BigDecimal("100");
                BigDecimal zero = BigDecimal.ZERO;
                price.divide(zero);
                break;
            default:
                throw new IllegalArgumentException("Unknown exception type: " + exceptionType);
        }
    }

    // Auditable function with rating update
    @AuditableFunction
    @Transactional
    public Product updateProductRating(Long productId, Double newRating, Integer reviewCount) throws Exception {
        auditWriterService.logBusinessInfo(
            String.format("Updating rating for product %d: %.2f (%d reviews)", 
                productId, newRating, reviewCount)
        );
        
        if (newRating < 0 || newRating > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }
        
        Product product = getProductById(productId);
        product.setRating(newRating);
        product.setReviewCount(reviewCount);
        
        return productRepository.save(product);
    }
}
