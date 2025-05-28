package io.queberry.que.controller;

import io.queberry.que.config.AppointmentConfiguration;
import io.queberry.que.entity.Branch;
import io.queberry.que.entity.FileReference;
import io.queberry.que.repository.AppointmentConfigurationRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.docx4j.Docx4J;
import org.docx4j.convert.out.HTMLSettings;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
@RestController
@RequestMapping("/api")
public class AppointmentConfigurationController {

    @Value("${vault.baseDirectory:null}")
    private String baseDirectory;

    @Value("${user.home}")
    private String userHome;

    private final AppointmentConfigurationRepository appointmentConfigurationRepository;

    public AppointmentConfigurationController(AppointmentConfigurationRepository appointmentConfigurationRepository) {
        this.appointmentConfigurationRepository = appointmentConfigurationRepository;
    }

    @GetMapping("/config/appointment")
    public ResponseEntity<AppointmentConfiguration> getAppointmentConfig(){
        return ResponseEntity.ok().body(appointmentConfigurationRepository.findAll().get(0));
//        return ResponseEntity.ok(appointmentConfigurationRepository.findAll().stream().findFirst().orElse(null));
    }

    @PutMapping("/config/appointment")
    public ResponseEntity<AppointmentConfiguration> editAppointmentConfig(@RequestBody AppointmentConfigurationResource resource){
//        AppointmentConfiguration appointmentConfiguration = appointmentConfigurationRepository.findAll().stream().findFirst().orElse(null);
//        if (appointmentConfiguration != null) {
//            appointmentConfiguration.change(resource);
//            appointmentConfiguration = appointmentConfigurationRepository.save(appointmentConfiguration);
//            return ResponseEntity.ok(appointmentConfiguration);
//        }else{
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        return null;
    }


    @PostMapping(value="/config/convertHtml", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FileReference convertToHtml(@RequestParam(value = "file") MultipartFile file) throws Exception {
        byte[] fileContent = file.getBytes();

        try (ByteArrayInputStream is = new ByteArrayInputStream(fileContent);
             ByteArrayOutputStream os = new ByteArrayOutputStream()) {

            WordprocessingMLPackage wordMLPackage = Docx4J.load(is);

            // Ensure that the MainDocumentPart is properly initialized
            MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();
            if (documentPart == null) {
                throw new IOException("Error loading Word document");
            }

            HTMLSettings htmlSettings = Docx4J.createHTMLSettings();
            htmlSettings.setWmlPackage(wordMLPackage);

            // Perform HTML conversion
            Docx4J.toHTML(htmlSettings, os, Docx4J.FLAG_EXPORT_PREFER_XSL);

            UUID uuid = UUID.randomUUID();

            if(this.baseDirectory != null && this.baseDirectory.equals("null")){
                this.baseDirectory = this.userHome + "/vault/files";
            }

            try (FileOutputStream fos = new FileOutputStream(baseDirectory + "/" + uuid + ".html")) {
                fos.write(os.toString().getBytes());
            }
            return new FileReference(uuid.toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Error converting DOCX to HTML", e);
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AppointmentConfigurationResource {
        private Boolean enabled;
        private boolean sms;
        private String smsTextEnglish;
        private String smsTextSecondary;
        private String apptEditedEnglish;
        private String apptCancelEnglish;
        private String checkInTime;
        private String checkInMaxTime;
        private String tenantId;
        private String clientId;
        private String clientSecret;
        private String userName;
        private String subject;
        private String body;
        private Boolean sms_alert;
        private Boolean email_alert;
        private Boolean whatsapp_alert;
        private Boolean f2f_mode;
        private Boolean virtual_mode;
        private Boolean single;
        private Boolean bulk;
        private Branch branch;
        private String maxSlots;
        private String emailContent;
        private String confirmedContent;
        private String confirmedSubject;
        private String editedContent;
        private String editedSubject;
        private String cancelledContent;
        private String cancelledSubject;
        private String hostUrl;
        private String cancelReason;
        private String editReason;
        private String confirmedVContent;
        private String confirmedVSubject;
        private String editedVContent;
        private String editedVSubject;
        private String cancelledVContent;
        private String cancelledVSubject;
    }
}
