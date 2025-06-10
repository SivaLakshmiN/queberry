package io.queberry.que.config;

import io.queberry.que.config.Appointment.AppointmentConfigurationRepository;
import io.queberry.que.config.Audio.AudioConfigurationRepository;
import io.queberry.que.config.Break.BreakConfigurationRepository;
import io.queberry.que.config.Dispenser.DispenserConfigurationRepository;
import io.queberry.que.config.Kpi.KpiConfigurationRepository;
import io.queberry.que.config.Meeting.MeetingConfigurationRepository;
import io.queberry.que.config.Queue.QueueConfigurationRepository;
import io.queberry.que.config.Sms.SmsConfigurationRepository;
import io.queberry.que.config.Survey.SurveyConfigurationRepository;
import io.queberry.que.config.Tenant.TenantContext;
import io.queberry.que.config.Theme.ThemeConfigurationRepository;
import io.queberry.que.config.Token.TokenConfigurationRepository;
import io.queberry.que.home.HomeRepository;
import io.queberry.que.license.LicenseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigurationService {

    private final AudioConfigurationRepository audioConfigurationRepository;

    private final DispenserConfigurationRepository dispenserConfigurationRepository;

    private final QueueConfigurationRepository queueConfigurationRepository;

    private final ThemeConfigurationRepository themeConfigurationRepository;

    private final TokenConfigurationRepository tokenConfigurationRepository;

    private final SmsConfigurationRepository smsConfigurationRepository;

    private final SurveyConfigurationRepository surveyConfigurationRepository;

    private final AppointmentConfigurationRepository appointmentConfigurationRepository;

    private final MeetingConfigurationRepository meetingConfigurationRepository;

    private final KpiConfigurationRepository kpiConfigurationRepository;

    private final HomeRepository homeRepository;

    private final Environment environment;

    private final BreakConfigurationRepository breakConfigurationRepository;

    private final LicenseRepository licenseRepository;

    public ConfigurationResource getGlobalConfiguration(){
        return build();
    }

    public MainConfigResource getMainConfiguration(){
        return buildMainConfig();
    }

//    public ConfigurationResource build(){
//        return new ConfigurationResource(
//                audioConfigurationRepository.findAll().stream().findFirst().orElse(null),
//                dispenserConfigurationRepository.findAll().stream().findFirst().orElse(null),
//                queueConfigurationRepository.findAll().stream().findFirst().orElse(null),
//                themeConfigurationRepository.findAll().stream().findFirst().orElse(null),
//                tokenConfigurationRepository.findAll().stream().findFirst().orElse(null),
//                smsConfigurationRepository.findAll().stream().findFirst().orElse(null),
//                surveyConfigurationRepository.findAll().stream().findFirst().orElse(null),
//                appointmentConfigurationRepository.findAll().stream().findFirst().orElse(null),
//                meetingConfigurationRepository.findAll().stream().findFirst().orElse(null),
//                kpiConfigurationRepository.findAll().stream().findFirst().orElse(null),
//                homeRepository.findAll().stream().findFirst().orElse(null),
//                environment.getActiveProfiles(),
//                breakConfigurationRepository.findAll().stream().findFirst().orElse(null),
//                licenseRepository.findAll().stream().findFirst().orElse(null)
//
//        );
//    }

    public ConfigurationResource build(){
        return new ConfigurationResource(
                audioConfigurationRepository.findByBranchKey(TenantContext.getBranchKey()).orElse(null),
                dispenserConfigurationRepository.findByBranchKey(TenantContext.getBranchKey()).orElse(null),
                queueConfigurationRepository.findByBranchKey(TenantContext.getBranchKey()).orElse(null),
                themeConfigurationRepository.findByBranchKey(TenantContext.getBranchKey()).orElse(null),
                tokenConfigurationRepository.findAll().stream().findFirst().orElse(null),
                smsConfigurationRepository.findAll().stream().findFirst().orElse(null),
                surveyConfigurationRepository.findAll().stream().findFirst().orElse(null),
//                appointmentConfigurationRepository.findAll().stream().findFirst().orElse(null),
                meetingConfigurationRepository.findAll().stream().findFirst().orElse(null),
                kpiConfigurationRepository.findAll().stream().findFirst().orElse(null),
                homeRepository.findAll().stream().findFirst().orElse(null),
                environment.getActiveProfiles(),

//                breakConfigurationRepository.findAll().stream().findFirst().orElse(null),
                licenseRepository.findAll().stream().findFirst().orElse(null)

        );
    }

    public MainConfigResource buildMainConfig(){
        log.info("branch key {}", TenantContext.getBranchKey());
        return new MainConfigResource(
                queueConfigurationRepository.findByBranchKey(null).orElse(null),
                homeRepository.findAll().stream().findFirst().orElse(null),
                environment.getActiveProfiles(),
                licenseRepository.findAll().stream().findFirst().orElse(null)
        );
    }
}
