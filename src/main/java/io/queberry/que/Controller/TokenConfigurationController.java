package io.queberry.que.Controller;
import io.queberry.que.Entity.CloudloomController;
import io.queberry.que.Entity.TokenConfiguration;
import io.queberry.que.Repository.TokenConfigurationRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CloudloomController
@RequiredArgsConstructor
public class TokenConfigurationController {

    private final TokenConfigurationRepository tokenConfigurationRepository;

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
