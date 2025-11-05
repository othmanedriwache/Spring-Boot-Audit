package com.demo.service;

import com.auditWriter.annotations.AuditableClass;
import com.auditWriter.annotations.NotAuditableFunction;
import com.auditWriter.service.auditWriterService.AuditWriterService;
import com.demo.entity.Order;
import com.demo.entity.OrderItem;
import com.demo.entity.Product;
import com.demo.entity.User;
import com.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AuditableClass
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private AuditWriterService auditWriterService;
    
    @Autowired
    private ApplicationContext applicationContext;
    
    private OrderService self;
    
    @PostConstruct
    public void init() {
        this.self = applicationContext.getBean(OrderService.class);
    }

    // Auditable function with complex transaction
    @Transactional
    public Order createOrder(Order order) throws Exception {
        auditWriterService.logBusinessInfo("Creating new order for user: " + order.getUser().getId());
        
        // Validate user exists
        User user = userService.getUserById(order.getUser().getId());
        order.setUser(user);
        
        // Generate order number
        String orderNumber = generateOrderNumber();
        order.setOrderNumber(orderNumber);
        
        // Calculate totals
        self.calculateOrderTotals(order);
        
        // Validate stock availability
        self.validateStockAvailability(order);
        
        // Save order
        Order savedOrder = orderRepository.save(order);
        
        // Update product stocks
        self.updateProductStocks(savedOrder);
        
        auditWriterService.logBusinessInfo("Order created successfully: " + orderNumber);
        
        return savedOrder;
    }

    // Auditable helper function
    public void calculateOrderTotals(Order order) throws Exception {
        BigDecimal subtotal = BigDecimal.ZERO;
        
        for (OrderItem item : order.getOrderItems()) {
            Product product = productService.getProductById(item.getProduct().getId());
            BigDecimal itemTotal = product.getPrice().multiply(new BigDecimal(item.getQuantity()));
            item.setUnitPrice(product.getPrice());
            item.setTotalPrice(itemTotal);
            subtotal = subtotal.add(itemTotal);
        }
        
        // Calculate tax (10%)
        BigDecimal taxAmount = subtotal.multiply(new BigDecimal("0.10"));
        order.setTaxAmount(taxAmount);
        
        // Add shipping cost if not set
        if (order.getShippingCost() == null) {
            order.setShippingCost(new BigDecimal("10.00"));
        }
        
        // Calculate total
        BigDecimal total = subtotal.add(taxAmount).add(order.getShippingCost());
        
        if (order.getDiscountAmount() != null) {
            total = total.subtract(order.getDiscountAmount());
        }
        
        order.setTotalAmount(total);
    }

    // Auditable validation function
    public void validateStockAvailability(Order order) throws Exception {
        for (OrderItem item : order.getOrderItems()) {
            Product product = productService.getProductById(item.getProduct().getId());
            
            if (product.getStockQuantity() == null || product.getStockQuantity() < item.getQuantity()) {
                auditWriterService.logBusinessError(
                    "Insufficient stock for product: " + product.getName()
                );
                throw new IllegalStateException(
                    "Insufficient stock for product: " + product.getName()
                );
            }
        }
    }

    // Auditable stock update function
    @Transactional
    public void updateProductStocks(Order order) throws Exception {
        for (OrderItem item : order.getOrderItems()) {
            productService.updateStock(
                item.getProduct().getId(),
                item.getQuantity(),
                "SUBTRACT"
            );
        }
    }

    // Non-auditable helper
    @NotAuditableFunction
    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Auditable function
    public Order getOrderById(Long id) {
        auditWriterService.logBusinessInfo("Fetching order: " + id);
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    // Auditable function
    public List<Order> getUserOrders(Long userId) {
        auditWriterService.logBusinessInfo("Fetching orders for user: " + userId);
        
        // Validate user exists
        userService.getUserById(userId);
        
        return orderRepository.findByUserId(userId);
    }

    // Auditable function with status update
    @Transactional
    public Order updateOrderStatus(Long orderId, Order.OrderStatus newStatus) {
        auditWriterService.logBusinessInfo(
            String.format("Updating order %d status to: %s", orderId, newStatus)
        );
        
        Order order = getOrderById(orderId);
        Order.OrderStatus oldStatus = order.getStatus();
        
        // Validate status transition
        validateStatusTransition(oldStatus, newStatus);
        
        order.setStatus(newStatus);
        
        // Update timestamps based on status
        switch (newStatus) {
            case SHIPPED:
                order.setShippedAt(LocalDateTime.now());
                break;
            case DELIVERED:
                order.setDeliveredAt(LocalDateTime.now());
                break;
        }
        
        Order updatedOrder = orderRepository.save(order);
        
        auditWriterService.logBusinessInfo(
            String.format("Order status updated: %s -> %s", oldStatus, newStatus)
        );
        
        return updatedOrder;
    }

    // Non-auditable validation
    @NotAuditableFunction
    private void validateStatusTransition(Order.OrderStatus from, Order.OrderStatus to) {
        // Simple validation - can be expanded
        if (from == Order.OrderStatus.DELIVERED && to != Order.OrderStatus.RETURNED) {
            throw new IllegalStateException("Cannot change status of delivered order");
        }
        if (from == Order.OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cannot change status of cancelled order");
        }
    }

    // Auditable function with payment processing
    @Transactional
    public Order processPayment(Long orderId, String paymentMethod) {
        auditWriterService.logBusinessInfo(
            String.format("Processing payment for order %d with method: %s", orderId, paymentMethod)
        );
        
        Order order = getOrderById(orderId);
        
        if (order.getPaymentStatus() == Order.PaymentStatus.CAPTURED) {
            throw new IllegalStateException("Payment already processed");
        }
        
        try {
            // Simulate payment processing
            Thread.sleep(500);
            
            order.setPaymentMethod(paymentMethod);
            order.setPaymentStatus(Order.PaymentStatus.CAPTURED);
            order.setStatus(Order.OrderStatus.CONFIRMED);
            
            auditWriterService.logBusinessInfo("Payment processed successfully for order: " + orderId);
            
            return orderRepository.save(order);
            
        } catch (InterruptedException e) {
            auditWriterService.logBusinessError("Payment processing interrupted: " + orderId);
            order.setPaymentStatus(Order.PaymentStatus.FAILED);
            orderRepository.save(order);
            throw new RuntimeException("Payment processing failed", e);
        }
    }

    // Auditable function with cancellation logic
    @Transactional
    public Order cancelOrder(Long orderId, String reason) throws Exception {
        auditWriterService.logBusinessInfo(
            String.format("Cancelling order %d. Reason: %s", orderId, reason)
        );
        
        Order order = getOrderById(orderId);
        
        if (order.getStatus() == Order.OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot cancel delivered order");
        }
        
        if (order.getStatus() == Order.OrderStatus.SHIPPED) {
            throw new IllegalStateException("Cannot cancel shipped order. Request return instead.");
        }
        
        // Restore product stocks
        for (OrderItem item : order.getOrderItems()) {
            productService.updateStock(
                item.getProduct().getId(),
                item.getQuantity(),
                "ADD"
            );
        }
        
        order.setStatus(Order.OrderStatus.CANCELLED);
        order.setNotes(order.getNotes() != null ? 
            order.getNotes() + "\nCancellation reason: " + reason : 
            "Cancellation reason: " + reason);
        
        return orderRepository.save(order);
    }

    // Auditable function for order search
    public List<Order> searchOrders(Order.OrderStatus status, 
                                     LocalDateTime startDate, 
                                     LocalDateTime endDate) {
        auditWriterService.logBusinessInfo(
            String.format("Searching orders - Status: %s, Date range: %s to %s", 
                status, startDate, endDate)
        );
        
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        
        if (status != null) {
            return orderRepository.findByStatus(status);
        } else if (startDate != null && endDate != null) {
            return orderRepository.findOrdersInDateRange(startDate, endDate);
        } else {
            return orderRepository.findAll();
        }
    }

    // Auditable function that tests exception scenarios
    public void testOrderException(String scenario) throws Exception {
        auditWriterService.logBusinessInfo("Testing order exception scenario: " + scenario);
        
        switch (scenario) {
            case "insufficient_stock":
                Order testOrder = new Order();
                testOrder.setUser(new User());
                testOrder.getUser().setId(1L);
                OrderItem item = new OrderItem();
                item.setProduct(new Product());
                item.getProduct().setId(1L);
                item.setQuantity(99999);
                testOrder.setOrderItems(List.of(item));
                createOrder(testOrder);
                break;
                
            case "invalid_status_transition":
                updateOrderStatus(1L, Order.OrderStatus.DELIVERED);
                updateOrderStatus(1L, Order.OrderStatus.PENDING);
                break;
                
            case "payment_failure":
                Thread.interrupted(); // Set interrupted flag
                processPayment(1L, "CREDIT_CARD");
                break;
                
            case "cancel_delivered":
                Order deliveredOrder = getOrderById(1L);
                deliveredOrder.setStatus(Order.OrderStatus.DELIVERED);
                orderRepository.save(deliveredOrder);
                cancelOrder(1L, "Test cancellation");
                break;
                
            default:
                throw new IllegalArgumentException("Unknown scenario: " + scenario);
        }
    }

    // Non-auditable bulk operation
    @NotAuditableFunction
    public void cleanupOldOrders(int daysOld) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysOld);
        List<Order> oldOrders = orderRepository.findOrdersInDateRange(
            LocalDateTime.of(2000, 1, 1, 0, 0),
            cutoffDate
        );
        
        oldOrders.stream()
            .filter(o -> o.getStatus() == Order.OrderStatus.CANCELLED)
            .forEach(orderRepository::delete);
    }
}
