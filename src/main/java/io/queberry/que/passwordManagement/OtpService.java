package io.queberry.que.passwordManagement;

import io.queberry.que.enums.ResetMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    private final PasswordManagementRepository passwordManagementRepository;

    public OtpService(PasswordManagementRepository passwordManagementRepository) {
        this.passwordManagementRepository = passwordManagementRepository;
    }

    public ResponseEntity<?> verifyOtp(OtpVerificationDTO dto) {
        // Use case-insensitive method
        PasswordManagement pm = passwordManagementRepository.findByUsernameIgnoreCase(dto.getUsername());
        if (pm == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No employee record found for this username.");
        }

        // Check if the reset mode is OTP
        if (pm.getResetMode() != ResetMode.OTP) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The reset mode for this user is not set to OTP. Please request a new OTP.");
        }

        // OTP Expiry Check
        //  LocalDateTime otpGeneratedAt = pm.getOtpGeneratedAt();
//        if (otpGeneratedAt == null || otpGeneratedAt.plusMinutes(1).isBefore(LocalDateTime.now())) {
//            return ResponseEntity.status(HttpStatus.GONE).body("OTP has expired.");
//        }

        // OTP Match Check
        String storedOtp = "sms".equalsIgnoreCase(dto.getType()) ? pm.getSmsOtp() : pm.getEmailOtp();
        if (storedOtp != null && storedOtp.equals(dto.getOtp())) {
//            // Optionally clear OTP after verification
//            if ("sms".equalsIgnoreCase(dto.getType())) {
//                pm.setSmsOtp(null);
//            } else {
//                pm.setEmailOtp(null);
//            }

            // Optionally reset the OTP generation timestamp
            //  pm.setOtpGeneratedAt(null);

            // Save the updated entity
            passwordManagementRepository.save(pm);

            return ResponseEntity.ok("OTP verified successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP.");
        }
    }



}

