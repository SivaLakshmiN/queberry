package io.queberry.que.config.Token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TokenConfigurationController {

    private final TokenConfigurationRepository tokenConfigurationRepository;

    public TokenConfigurationController(TokenConfigurationRepository tokenConfigurationRepository) {
        this.tokenConfigurationRepository = tokenConfigurationRepository;
    }

    @GetMapping("/config/token")
    public ResponseEntity getTokenConfiguration(){
        return ResponseEntity.ok(tokenConfigurationRepository.findAll().stream().findFirst().orElse(null));
    }

    @PutMapping("/config/token")
    public ResponseEntity editTokenConfiguration(@RequestBody TokenConfigurationResource resource){
        TokenConfiguration tokenConfiguration = tokenConfigurationRepository.findAll().stream().findFirst().orElse(null);
        tokenConfiguration.change(resource);
        tokenConfiguration = tokenConfigurationRepository.save(tokenConfiguration);
        return ResponseEntity.ok(tokenConfiguration);
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TokenConfigurationResource{
        private Integer tokenValidity;
    }
}
