//package io.queberry.que.controller;
//
//
//
//import com.example.QueApplication.DTO.OtpVerificationDTO;
//import com.example.QueApplication.Entity.CloudloomController;
//import com.example.QueApplication.Entity.PasswordPolicy;
//import com.example.QueApplication.Repository.PasswordPolicyRepository;
//import com.example.QueApplication.Services.OtpService;
//import lombok.AllArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//import java.util.Optional;
//import java.util.concurrent.ConcurrentHashMap;
//
//@AllArgsConstructor
//@CloudloomController
//public class PolicyController {
//
//    private final PasswordPolicyRepository passwordPolicyRepository;
//    private final OtpService otpService;
//    private final Map<String, String> otpStore = new ConcurrentHashMap<>();
//    @GetMapping("/passwordPolicy")
//    private ResponseEntity<?> fetch(){
//        return ResponseEntity.ok(passwordPolicyRepository.findAll().stream().findFirst());
//    }
//
//    @PutMapping("/passwordPolicy/{id}/edit")
//    private ResponseEntity<?> edit(@PathVariable String id, @RequestBody PasswordPolicy passwordPolicy){
//        Optional<PasswordPolicy> passwordPolicy1 = passwordPolicyRepository.findById(id);
//
//        return passwordPolicy1.map(policy -> {
//            policy.setChanel(passwordPolicy.getChanel());
//            policy.setExpireDays(passwordPolicy.getExpireDays());
//            policy.setLength(passwordPolicy.getLength());
//            policy.setFailedAttempts(passwordPolicy.getFailedAttempts());
//            policy.setResetMode(passwordPolicy.getResetMode());
//            return ResponseEntity.ok(passwordPolicyRepository.save(policy));
//        }).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    @PostMapping("/verify-otp")
//    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerificationDTO dto) {
//        return otpService.verifyOtp(dto);
//    }
//
//}
//
