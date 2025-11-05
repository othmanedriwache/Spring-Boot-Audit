package com.demo.controller;

import com.auditWriter.annotations.AuditableClass;
import com.demo.entity.Order;
import com.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/orders")
@AuditableClass
public class OrderController {

    @Autowired
    private OrderService orderService;

    // POST with @RequestBody for order creation
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) throws Exception {
        Order createdOrder = orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    // GET with @PathVariable
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    // GET user orders with @PathVariable
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long userId) {
        List<Order> orders = orderService.getUserOrders(userId);
        return ResponseEntity.ok(orders);
    }

    // PATCH for status update with @PathVariable and @RequestParam
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam Order.OrderStatus status) {
        Order updatedOrder = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }

    // POST for payment processing with @PathVariable and @RequestBody
    @PostMapping("/{orderId}/payment")
    public ResponseEntity<Order> processPayment(
            @PathVariable Long orderId,
            @RequestBody Map<String, String> paymentData) {
        
        String paymentMethod = paymentData.get("paymentMethod");
        Order processedOrder = orderService.processPayment(orderId, paymentMethod);
        return ResponseEntity.ok(processedOrder);
    }

    // POST for order cancellation with @PathVariable and @RequestBody
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(
            @PathVariable Long orderId,
            @RequestBody Map<String, String> cancellationData) throws Exception {
        
        String reason = cancellationData.getOrDefault("reason", "Customer requested");
        Order cancelledOrder = orderService.cancelOrder(orderId, reason);
        return ResponseEntity.ok(cancelledOrder);
    }

    // GET with multiple @RequestParam including dates
    @GetMapping("/search")
    public ResponseEntity<List<Order>> searchOrders(
            @RequestParam(required = false) Order.OrderStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        List<Order> orders = orderService.searchOrders(status, startDate, endDate);
        return ResponseEntity.ok(orders);
    }

    // GET with @PathVariable and @RequestHeader
    @GetMapping("/{orderId}/tracking")
    public ResponseEntity<Map<String, Object>> getOrderTracking(
            @PathVariable Long orderId,
            @RequestHeader(value = "X-Request-Id", required = false) String requestId) {
        
        Order order = orderService.getOrderById(orderId);
        
        Map<String, Object> tracking = new HashMap<>();
        tracking.put("orderId", orderId);
        tracking.put("status", order.getStatus());
        tracking.put("trackingNumber", order.getTrackingNumber());
        tracking.put("requestId", requestId);
        
        return ResponseEntity.ok(tracking);
    }

    // PUT for complete order update
    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrder(
            @PathVariable Long orderId,
            @RequestBody Order orderUpdate) {
        
        Order existingOrder = orderService.getOrderById(orderId);
        
        if (orderUpdate.getShippingAddress() != null) {
            existingOrder.setShippingAddress(orderUpdate.getShippingAddress());
        }
        if (orderUpdate.getBillingAddress() != null) {
            existingOrder.setBillingAddress(orderUpdate.getBillingAddress());
        }
        if (orderUpdate.getNotes() != null) {
            existingOrder.setNotes(orderUpdate.getNotes());
        }
        
        return ResponseEntity.ok(existingOrder);
    }

    // PATCH for tracking number update
    @PatchMapping("/{orderId}/tracking")
    public ResponseEntity<Order> updateTrackingNumber(
            @PathVariable Long orderId,
            @RequestParam String trackingNumber) {
        
        Order order = orderService.getOrderById(orderId);
        order.setTrackingNumber(trackingNumber);
        
        return ResponseEntity.ok(order);
    }

    // POST with form parameters for quick order
    @PostMapping("/quick")
    public ResponseEntity<Map<String, Object>> quickOrder(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam Integer quantity,
            @RequestParam(required = false) String shippingAddress) {
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Quick order created");
        response.put("userId", userId);
        response.put("productId", productId);
        response.put("quantity", quantity);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET for order statistics with date range
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getOrderStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(value = "groupBy", defaultValue = "day") String groupBy) {
        
        List<Order> orders = orderService.searchOrders(null, startDate, endDate);
        
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalOrders", orders.size());
        statistics.put("dateRange", Map.of("start", startDate, "end", endDate));
        statistics.put("groupBy", groupBy);
        
        return ResponseEntity.ok(statistics);
    }

    // POST for exception testing
    @PostMapping("/test/exception")
    public ResponseEntity<Map<String, String>> testException(
            @RequestBody Map<String, String> payload) throws Exception {
        
        String scenario = payload.get("scenario");
        orderService.testOrderException(scenario);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Test completed");
        return ResponseEntity.ok(response);
    }

    // GET for filtering orders with multiple optional parameters
    @GetMapping("/filter")
    public ResponseEntity<List<Order>> filterOrders(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Order.OrderStatus status,
            @RequestParam(required = false) Order.PaymentStatus paymentStatus,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        
        List<Order> orders;
        
        if (userId != null) {
            orders = orderService.getUserOrders(userId);
        } else if (status != null) {
            orders = orderService.searchOrders(status, null, null);
        } else {
            orders = orderService.searchOrders(null, null, null);
        }
        
        return ResponseEntity.ok(orders);
    }

    // PATCH for bulk status update
    @PatchMapping("/bulk/status")
    public ResponseEntity<Map<String, Object>> bulkUpdateStatus(
            @RequestBody Map<String, Object> payload) {
        
        @SuppressWarnings("unchecked")
        List<Long> orderIds = (List<Long>) payload.get("orderIds");
        String statusStr = (String) payload.get("status");
        Order.OrderStatus status = Order.OrderStatus.valueOf(statusStr);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Bulk status update");
        response.put("orderCount", orderIds != null ? orderIds.size() : 0);
        response.put("newStatus", status);
        
        return ResponseEntity.ok(response);
    }

    // GET for order summary with @PathVariable and query params
    @GetMapping("/{orderId}/summary")
    public ResponseEntity<Map<String, Object>> getOrderSummary(
            @PathVariable Long orderId,
            @RequestParam(value = "includeItems", defaultValue = "true") boolean includeItems,
            @RequestParam(value = "includeUser", defaultValue = "true") boolean includeUser) {
        
        Order order = orderService.getOrderById(orderId);
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("order", order);
        summary.put("includeItems", includeItems);
        summary.put("includeUser", includeUser);
        
        return ResponseEntity.ok(summary);
    }

    // POST for order refund
    @PostMapping("/{orderId}/refund")
    public ResponseEntity<Map<String, Object>> refundOrder(
            @PathVariable Long orderId,
            @RequestParam(required = false) String reason,
            @RequestBody(required = false) Map<String, Object> refundDetails) {
        
        Order order = orderService.getOrderById(orderId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Refund processed");
        response.put("orderId", orderId);
        response.put("amount", order.getTotalAmount());
        response.put("reason", reason);
        
        return ResponseEntity.ok(response);
    }

    // DELETE for order (soft delete)
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Map<String, String>> deleteOrder(@PathVariable Long orderId) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Order deleted successfully");
        response.put("orderId", orderId.toString());
        return ResponseEntity.ok(response);
    }
}
