package com.demo.service;

import com.auditWriter.annotations.AuditableClass;
import com.auditWriter.annotations.AuditableFunction;
import com.auditWriter.annotations.NotAuditableFunction;
import com.auditWriter.service.auditWriterService.AuditWriterService;
import com.demo.entity.User;
import com.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AuditableClass
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AuditWriterService auditWriterService;
    
    @Autowired
    private ApplicationContext applicationContext;
    
    private UserService self;
    
    @PostConstruct
    public void init() {
        this.self = applicationContext.getBean(UserService.class);
    }

    // Auditable function - will be logged
    public User createUser(User user) {
        auditWriterService.logBusinessInfo("Creating new user: " + user.getUsername());
        
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        // Call another auditable function (inner call scenario)
        self.validateUserData(user);
        
        return userRepository.save(user);
    }

    // Auditable function with exception scenario
    public User getUserById(Long id) {
        auditWriterService.logBusinessInfo("Fetching user by ID: " + id);
        
        if (id == null || id <= 0) {
            auditWriterService.logBusinessError("Invalid user ID: " + id);
            throw new IllegalArgumentException("User ID must be positive");
        }
        
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // Non-auditable function - will NOT be logged

    public List<User> getAllUsers() throws InterruptedException {
        self.internalHelper();
        return userRepository.findAll();
    }


    public void internalHelper() throws InterruptedException {
        Thread.sleep(300); // This won't be logged
    }

    // Auditable function that calls non-auditable function (mixed scenario)
    public List<User> getUsersByStatus(User.UserStatus status) {
        auditWriterService.logBusinessInfo("Fetching users by status: " + status);
        
        // This internal call is not auditable
        self.logInternalMessage("Status filter applied");
        
        return userRepository.findByStatus(status);
    }

    // Non-auditable internal helper function
    @NotAuditableFunction
    public void logInternalMessage(String message) {
        System.out.println("Internal log: " + message);
    }

    // Auditable function with validation
    public void validateUserData(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            int a = 5/0;
        }
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (user.getPassword() == null || user.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
    }

    // Auditable function with transaction and exception handling
    @Transactional
    public User updateUser(Long id, User updatedUser) {
        auditWriterService.logBusinessInfo("Updating user: " + id);
        
        User existingUser = getUserById(id);
        
        try {
            if (updatedUser.getFirstName() != null) {
                existingUser.setFirstName(updatedUser.getFirstName());
            }
            if (updatedUser.getLastName() != null) {
                existingUser.setLastName(updatedUser.getLastName());
            }
            if (updatedUser.getEmail() != null) {
                existingUser.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getPhoneNumber() != null) {
                existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            }
            
            // Simulate processing time
            Thread.sleep(100);
            
            return userRepository.save(existingUser);
            
        } catch (InterruptedException e) {
            auditWriterService.logBusinessError("Thread interrupted during user update");
            throw new RuntimeException("Update interrupted", e);
        }
    }

    // Auditable function that tests exception scenario
    public User suspendUser(Long id) {
        auditWriterService.logBusinessInfo("Suspending user: " + id);
        
        User user = getUserById(id);
        
        if (user.getStatus() == User.UserStatus.SUSPENDED) {
            auditWriterService.logBusinessError("User already suspended: " + id);
            throw new IllegalStateException("User is already suspended");
        }
        
        user.setStatus(User.UserStatus.SUSPENDED);
        return userRepository.save(user);
    }

    // Non-auditable bulk operation
    @NotAuditableFunction
    public void deleteInactiveUsers() {
        List<User> inactiveUsers = userRepository.findByStatus(User.UserStatus.INACTIVE);
        userRepository.deleteAll(inactiveUsers);
    }

    // Auditable search function
    public List<User> searchUsers(String searchTerm) {
        auditWriterService.logBusinessInfo("Searching users with term: " + searchTerm);
        return userRepository.searchByName(searchTerm);
    }

    // Auditable function with complex logic and multiple inner calls
    public User promoteToAdmin(Long userId) {
        auditWriterService.logBusinessInfo("Promoting user to admin: " + userId);
        
        User user = self.getUserById(userId);
        
        if (user.getRole() == User.UserRole.ADMIN) {
            throw new IllegalStateException("User is already an admin");
        }
        
        // Validate user is eligible
        self.validateAdminEligibility(user);
        
        user.setRole(User.UserRole.ADMIN);
        User savedUser = userRepository.save(user);
        
        // Log the promotion
        self.logUserPromotion(savedUser);
        
        return savedUser;
    }

    // Auditable validation function
    public void validateAdminEligibility(User user) {
        if (user.getStatus() != User.UserStatus.ACTIVE) {
            throw new IllegalStateException("Only active users can be promoted");
        }
    }

    // Non-auditable logging function
    @NotAuditableFunction
    public void logUserPromotion(User user) {
        System.out.println("User promoted: " + user.getUsername() + " at " + LocalDateTime.now());
    }

    // Auditable function that deliberately throws exception for testing
    public void testExceptionScenario(String testType) {
        auditWriterService.logBusinessInfo("Testing exception scenario: " + testType);
        
        switch (testType) {
            case "null_pointer":
                String test = null;
                test.length(); // Will throw NullPointerException
                break;
            case "arithmetic":
                int result = 10 / 0; // Will throw ArithmeticException
                break;
            case "illegal_argument":
                throw new IllegalArgumentException("Test illegal argument exception");
            case "runtime":
                throw new RuntimeException("Test runtime exception");
            default:
                auditWriterService.logBusinessError("Unknown test type: " + testType);
                throw new IllegalArgumentException("Unknown test type");
        }
    }

    // Auditable function with sleep for timing tests
    public String performLongOperation(int durationMs) throws InterruptedException {
        auditWriterService.logBusinessInfo("Starting long operation: " + durationMs + "ms");
        Thread.sleep(durationMs);
        auditWriterService.logBusinessInfo("Completed long operation");
        return "Operation completed in " + durationMs + "ms";
    }
}
