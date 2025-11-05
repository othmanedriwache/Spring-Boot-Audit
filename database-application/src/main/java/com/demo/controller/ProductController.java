package com.demo.controller;

import com.auditWriter.annotations.AuditableClass;
import com.demo.entity.Product;
import com.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/products")
@AuditableClass
public class ProductController {

    @Autowired
    private ProductService productService;

    // POST with @RequestBody
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    // GET with @PathVariable
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) throws Exception {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    // GET all
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // GET with @RequestParam for search
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam("q") String keyword) {
        List<Product> products = productService.searchProducts(keyword);
        return ResponseEntity.ok(products);
    }

    // GET with multiple @RequestParam (price range)
    @GetMapping("/price-range")
    public ResponseEntity<List<Product>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<Product> products = productService.searchByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    // GET with @PathVariable for category
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategory(
            @PathVariable Long categoryId) {
        List<Product> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }

    // PATCH with @PathVariable and @RequestParam for stock update
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Product> updateStock(
            @PathVariable Long id,
            @RequestParam Integer quantity,
            @RequestParam String operation) throws Exception {
        Product updatedProduct = productService.updateStock(id, quantity, operation);
        return ResponseEntity.ok(updatedProduct);
    }

    // PATCH with @PathVariable and @RequestBody for discount
    @PatchMapping("/{id}/discount")
    public ResponseEntity<Product> applyDiscount(
            @PathVariable Long id,
            @RequestBody Map<String, String> discountData) throws Exception {
        BigDecimal discountPercentage = new BigDecimal(discountData.get("percentage"));
        Product updatedProduct = productService.applyDiscount(id, discountPercentage);
        return ResponseEntity.ok(updatedProduct);
    }

    // PUT with full product update
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product product) throws Exception {
        Product existingProduct = productService.getProductById(id);
        
        if (product.getName() != null) existingProduct.setName(product.getName());
        if (product.getDescription() != null) existingProduct.setDescription(product.getDescription());
        if (product.getPrice() != null) existingProduct.setPrice(product.getPrice());
        if (product.getStockQuantity() != null) existingProduct.setStockQuantity(product.getStockQuantity());
        
        // Note: This would call an update method in service
        return ResponseEntity.ok(existingProduct);
    }

    // POST with @RequestParam (form-style)
    @PostMapping("/quick-add")
    public ResponseEntity<Product> quickAddProduct(
            @RequestParam String name,
            @RequestParam BigDecimal price,
            @RequestParam(required = false, defaultValue = "0") Integer stock,
            @RequestParam(required = false) String description) {
        
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setStockQuantity(stock);
        product.setDescription(description);
        
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    // PATCH for rating update with mixed parameters
    @PatchMapping("/{productId}/rating")
    public ResponseEntity<Product> updateRating(
            @PathVariable Long productId,
            @RequestParam Double rating,
            @RequestParam Integer reviewCount) throws Exception {
        Product updatedProduct = productService.updateProductRating(productId, rating, reviewCount);
        return ResponseEntity.ok(updatedProduct);
    }

    // POST for exception testing
    @PostMapping("/test/exception")
    public ResponseEntity<Map<String, String>> testException(
            @RequestBody Map<String, String> payload) {
        
        String exceptionType = payload.get("type");
        Long productId = payload.containsKey("productId") ? 
            Long.parseLong(payload.get("productId")) : null;
        
        productService.testProductException(exceptionType, productId);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Test completed");
        return ResponseEntity.ok(response);
    }

    // DELETE with @PathVariable
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Product deleted successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }

    // GET with optional parameters for filtering
    @GetMapping("/filter")
    public ResponseEntity<List<Product>> filterProducts(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Product.ProductStatus status,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        
        List<Product> products;
        
        if (brand != null) {
            products = productService.getProductsByCategory(null); // Simplified
        } else if (status != null) {
            products = productService.getAllProducts();
        } else if (minPrice != null && maxPrice != null) {
            products = productService.searchByPriceRange(minPrice, maxPrice);
        } else {
            products = productService.getAllProducts();
        }
        
        return ResponseEntity.ok(products);
    }

    // POST with @RequestBody containing array of products
    @PostMapping("/bulk")
    public ResponseEntity<Map<String, Object>> bulkCreateProducts(
            @RequestBody Map<String, Object> payload) {
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> productsData = 
            (List<Map<String, Object>>) payload.get("products");
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Bulk creation endpoint");
        response.put("count", productsData != null ? productsData.size() : 0);
        
        return ResponseEntity.ok(response);
    }

    // PATCH with @PathVariable and JSON for status update
    @PatchMapping("/{id}/status")
    public ResponseEntity<Product> updateProductStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusData) throws Exception {
        
        String statusStr = statusData.get("status");
        Product.ProductStatus status = Product.ProductStatus.valueOf(statusStr);
        
        Product product = productService.getProductById(id);
        product.setStatus(status);
        
        return ResponseEntity.ok(product);
    }

    // GET with @RequestHeader
    @GetMapping("/featured")
    public ResponseEntity<List<Product>> getFeaturedProducts(
            @RequestHeader(value = "X-User-Location", required = false) String location,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        
        // This would return featured products based on location
        List<Product> products = productService.getAllProducts();
        
        return ResponseEntity.ok(products.stream().limit(limit).toList());
    }

    // GET for product details with various query params
    @GetMapping("/{id}/details")
    public ResponseEntity<Map<String, Object>> getProductDetails(
            @PathVariable Long id,
            @RequestParam(value = "includeReviews", defaultValue = "false") boolean includeReviews,
            @RequestParam(value = "includeRelated", defaultValue = "false") boolean includeRelated) throws Exception {
        
        Product product = productService.getProductById(id);
        
        Map<String, Object> details = new HashMap<>();
        details.put("product", product);
        details.put("includeReviews", includeReviews);
        details.put("includeRelated", includeRelated);
        
        return ResponseEntity.ok(details);
    }

    // POST for bulk discount application
    @PostMapping("/bulk-discount")
    public ResponseEntity<Map<String, Object>> applyBulkDiscount(
            @RequestParam Long categoryId,
            @RequestParam BigDecimal discountPercentage,
            @RequestBody(required = false) Map<String, Object> options) {
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Bulk discount applied");
        response.put("categoryId", categoryId);
        response.put("discount", discountPercentage);
        
        return ResponseEntity.ok(response);
    }
}
