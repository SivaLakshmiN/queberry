package io.queberry.que.Controller;
import io.queberry.que.Entity.CloudloomController;
import io.queberry.que.Entity.ThemeConfiguration;
import io.queberry.que.Repository.ThemeConfigurationRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Objects;

@Slf4j
@CloudloomController
@RequiredArgsConstructor
public class ThemeConfigurationController {

    private final ThemeConfigurationRepository themeConfigurationRepository;

    @GetMapping("/config/theme")
    public ResponseEntity<?> getTheme(HttpServletRequest request){
        log.info("request header in transferToUser {}",request.getHeader("X-TenantID"));
        return ResponseEntity.ok(themeConfigurationRepository.findByBranchKey(request.getHeader("X-TenantID")));
    }

    @PutMapping("/config/themeConfig")
    public ResponseEntity getThemeByBranch(@RequestBody Theme theme){
        return ResponseEntity.ok(themeConfigurationRepository.findByBranchKey(theme.getBranchKey()));
    }

    @PutMapping("/config/theme")
    public ResponseEntity editTheme(@RequestBody Theme edit){
        ThemeConfiguration themeConfiguration = themeConfigurationRepository.findByBranchKey(edit.getBranchKey()).orElse(null);
        if(themeConfiguration == null){
            themeConfiguration = new ThemeConfiguration(edit.getSignageLogo(), edit.getDispenserLogo(), edit.getPrinterLogo(), edit.getFeedbackLogo(),
                    edit.getQrcodeLogo(),edit.getPrimaryColor(), edit.getSecondaryColor(), edit.getPrimaryTextColor(), edit.getSecondaryTextColor(),
                    edit.getQrPrimaryColor(),edit.getQrSecondaryColor(),edit.getBackgroundImage(),
            false, false, false, false, false, false ,edit.getLoginScreenLogo(),edit.getBranchKey());

        }else{
           themeConfiguration.change(edit);
        }
        return ResponseEntity.ok(Objects.requireNonNull(themeConfigurationRepository.save(themeConfiguration)));
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Theme{
        private String signageLogo;
        private String dispenserLogo;
        private String printerLogo;
        private String feedbackLogo;
        private String primaryColor;
        private String secondaryColor;
        private String primaryTextColor;
        private String secondaryTextColor;
        private String backgroundImage;
        private boolean showTime;
        private String qrPrimaryColor;
        private String qrSecondaryColor;
        private String qrcodeLogo;
//        private boolean showArabic;
//        private boolean showWaitingCustomers;
//        private boolean showWaitingTime;
//        private boolean showInQueueAlert;
//        private boolean sendInQueueMeetMail;
        private String loginScreenLogo;
        private String branchKey;
    }
}
