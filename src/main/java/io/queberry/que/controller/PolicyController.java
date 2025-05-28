package io.queberry.que.controller;

import io.queberry.que.dto.OtpVerificationDTO;
import io.queberry.que.entity.PasswordPolicy;
import io.queberry.que.repository.PasswordPolicyRepository;
import io.queberry.que.service.OtpService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PolicyController {

    private final PasswordPolicyRepository passwordPolicyRepository;
    private final OtpService otpService;
    private final Map<String, String> otpStore = new ConcurrentHashMap<>();
    @GetMapping("/passwordPolicy")
    private ResponseEntity<?> fetch(){
        return ResponseEntity.ok(passwordPolicyRepository.findAll().stream().findFirst());
    }

    @PutMapping("/passwordPolicy/{id}/edit")
    private ResponseEntity<?> edit(@PathVariable String id, @RequestBody PasswordPolicy passwordPolicy){
        Optional<PasswordPolicy> passwordPolicy1 = passwordPolicyRepository.findById(id);

        return passwordPolicy1.map(policy -> {
            policy.setChanel(passwordPolicy.getChanel());
            policy.setExpireDays(passwordPolicy.getExpireDays());
            policy.setLength(passwordPolicy.getLength());
            policy.setFailedAttempts(passwordPolicy.getFailedAttempts());
            policy.setResetMode(passwordPolicy.getResetMode());
            return ResponseEntity.ok(passwordPolicyRepository.save(policy));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerificationDTO dto) {

        return otpService.verifyOtp(dto);
    }

}

