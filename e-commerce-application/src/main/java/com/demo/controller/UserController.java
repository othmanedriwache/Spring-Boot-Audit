package com.demo.controller;

import com.auditWriter.annotations.AuditableClass;
import com.demo.entity.User;
import com.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
@AuditableClass
public class UserController {

    @Autowired
    private UserService userService;

    // POST with @RequestBody
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // GET with @PathVariable
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // GET all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() throws InterruptedException {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // GET with @RequestParam
    @GetMapping("/status")
    public ResponseEntity<List<User>> getUsersByStatus(
            @RequestParam("status") User.UserStatus status) {
        List<User> users = userService.getUsersByStatus(status);
        return ResponseEntity.ok(users);
    }

    // GET with multiple @RequestParam
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "includeInactive", defaultValue = "false") boolean includeInactive) {
        List<User> users = userService.searchUsers(keyword);
        return ResponseEntity.ok(users);
    }

    // PUT with @PathVariable and @RequestBody
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    // PATCH with @PathVariable and @RequestParam
    @PatchMapping("/{id}/suspend")
    public ResponseEntity<User> suspendUser(@PathVariable Long id) {
        User suspendedUser = userService.suspendUser(id);
        return ResponseEntity.ok(suspendedUser);
    }

    // PATCH with @PathVariable and @RequestBody
    @PatchMapping("/{id}/promote")
    public ResponseEntity<User> promoteToAdmin(@PathVariable Long id) {
        User promotedUser = userService.promoteToAdmin(id);
        return ResponseEntity.ok(promotedUser);
    }

    // DELETE with @PathVariable
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        // This would call a delete method
        Map<String, String> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }

    // POST with @RequestParam (form-style parameters)
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName) {
        
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // POST with mixed @PathVariable and @RequestBody
    @PostMapping("/{userId}/profile")
    public ResponseEntity<User> updateProfile(
            @PathVariable Long userId,
            @RequestBody Map<String, String> profileData) {
        
        User user = new User();
        user.setFirstName(profileData.get("firstName"));
        user.setLastName(profileData.get("lastName"));
        user.setPhoneNumber(profileData.get("phoneNumber"));
        
        User updatedUser = userService.updateUser(userId, user);
        return ResponseEntity.ok(updatedUser);
    }

    // GET with @RequestHeader
    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> getCurrentUser(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "User profile endpoint");
        response.put("auth", authHeader != null ? "Provided" : "Not provided");
        return ResponseEntity.ok(response);
    }

    // POST for testing exceptions with @RequestBody
    @PostMapping("/test/exception")
    public ResponseEntity<Map<String, String>> testException(
            @RequestBody Map<String, String> payload) {
        
        String testType = payload.get("type");
        userService.testExceptionScenario(testType);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Exception test completed");
        return ResponseEntity.ok(response);
    }

    // POST for testing long operations with @RequestParam
    @PostMapping("/test/long-operation")
    public ResponseEntity<Map<String, String>> testLongOperation(
            @RequestParam(value = "duration", defaultValue = "1000") int duration) throws InterruptedException {
        
        String result = userService.performLongOperation(duration);
        
        Map<String, String> response = new HashMap<>();
        response.put("result", result);
        return ResponseEntity.ok(response);
    }

    // GET with multiple optional @RequestParam
    @GetMapping("/filter")
    public ResponseEntity<List<User>> filterUsers(
            @RequestParam(required = false) User.UserStatus status,
            @RequestParam(required = false) User.UserRole role,
            @RequestParam(required = false) String searchTerm) throws InterruptedException {
        
        List<User> users;
        
        if (status != null) {
            users = userService.getUsersByStatus(status);
        } else if (searchTerm != null) {
            users = userService.searchUsers(searchTerm);
        } else {
            users = userService.getAllUsers();
        }
        
        return ResponseEntity.ok(users);
    }

    // POST with @RequestBody containing nested data
    @PostMapping("/bulk")
    public ResponseEntity<Map<String, Object>> createBulkUsers(
            @RequestBody Map<String, Object> payload) {
        
        @SuppressWarnings("unchecked")
        List<Map<String, String>> usersData = (List<Map<String, String>>) payload.get("users");
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Bulk user creation endpoint");
        response.put("count", usersData != null ? usersData.size() : 0);
        
        return ResponseEntity.ok(response);
    }

    // PUT with @PathVariable and @RequestParam
    @PutMapping("/{id}/role")
    public ResponseEntity<User> updateUserRole(
            @PathVariable Long id,
            @RequestParam User.UserRole role) {
        
        User user = userService.getUserById(id);
        user.setRole(role);
        User updatedUser = userService.updateUser(id, user);
        
        return ResponseEntity.ok(updatedUser);
    }

    // PATCH with @PathVariable and JSON body
    @PatchMapping("/{id}/status")
    public ResponseEntity<User> updateUserStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusData) {
        
        String statusStr = statusData.get("status");
        User.UserStatus status = User.UserStatus.valueOf(statusStr);
        
        User user = userService.getUserById(id);
        user.setStatus(status);
        User updatedUser = userService.updateUser(id, user);
        
        return ResponseEntity.ok(updatedUser);
    }

    // GET with path variable and query params combined
    @GetMapping("/{id}/summary")
    public ResponseEntity<Map<String, Object>> getUserSummary(
            @PathVariable Long id,
            @RequestParam(value = "includeOrders", defaultValue = "false") boolean includeOrders,
            @RequestParam(value = "includeReviews", defaultValue = "false") boolean includeReviews) {
        
        User user = userService.getUserById(id);
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("user", user);
        summary.put("includeOrders", includeOrders);
        summary.put("includeReviews", includeReviews);
        
        return ResponseEntity.ok(summary);
    }
}
