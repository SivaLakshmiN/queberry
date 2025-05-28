package io.queberry.que.Config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Profile("enterprise")
@Configuration
public class EnterpriseConfiguration {
}
