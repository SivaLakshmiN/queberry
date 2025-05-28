package io.queberry.que.Services;
import io.queberry.que.Entity.Appointment;
import io.queberry.que.Entity.AppointmentConfiguration;
import io.queberry.que.Entity.KpiConfiguration;
import io.queberry.que.Repository.AppointmentConfigurationRepository;
import io.queberry.que.Repository.KpiConfigurationRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;


@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultKpiService implements KpiService{

   // @Value("${vault.baseDirectory:null}")
    private String baseDirectory;

    private final KpiConfigurationRepository kpiConfigurationRepository;
    private final AppointmentConfigurationRepository appointmentConfigurationRepository;

//    @Override
//    public void check(String empEMail, String message) {
//        log.info("in mail service:" + empEMail);
//        if (kpiConfigurationRepository.count() != 0) {
//            KpiConfiguration kpiConfiguration = kpiConfigurationRepository.findAll().get(0);
//            if (!kpiConfiguration.isEnabled()) {
//                log.info("Email alerts not enabled");
//                return;
//            }
//            sendEmail(empEMail,message,kpiConfiguration);
//        }
//    }

//    @Override
//    public void check() {
//        log.info("Checking SLA Breach");
//        if (kpiConfigurationRepository.count() != 0) {
//            KpiConfiguration kpiConfiguration = kpiConfigurationRepository.findAll().get(0);
//            if (!kpiConfiguration.isEnabled()) {
//                log.info("Email alerts not enabled");
//                return;
//            }
//            branchRepository.findAll().forEach(branch -> {
//                //log.info("Checking SLA Breach for Branch : {}",branch);
//                LocalDateTime from = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
//                LocalDateTime to = LocalDateTime.of(LocalDate.now(), LocalTime.now());
//                serviceRepository.findAll().forEach(service -> {
//                    //log.info("Checking SLA Breach for Service : {}",service);
//                    Set<Assistance> assistances = assistanceRepository.findByCreatedAtBetweenAndServiceAndBranch(from, to, service, branch);
//                    //log.info("Checking SLA Breach  Total Tokens : {}",assistances.size());
//                    assistances.forEach(assistance -> {
//                        if (service.getServeSla() < (assistance.getTotalServeTime() / 60))
//                            if (kpiConfiguration.isTransactionTime())
//                                sendEmailTransaction(branch, kpiConfiguration, service, assistance);
//                        if (service.getWaitSla() < (assistance.getWaitingTime() / 60))
//                            if (kpiConfiguration.isWaitingTime())
//                                sendEmailWaiting(branch, kpiConfiguration, service, assistance);
//                    });
//                });
//            });
//        }
//    }

 //   private void sendEmail(String emailId, String msgBody,KpiConfiguration kpiConfiguration)
//    {
//        if (kpiConfiguration.getExchangeType().equals("OTHER")) {
//            //Initialize the Java Mail Sender
//            JavaMailSenderImpl javaMailSender = getJavaMailSender(kpiConfiguration);
//
//            //Initialize the Mail with the HTML body , subject and to address
//            MimeMessagePreparator preparator = mimeMessage -> {
//                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
//                message.setTo(emailId);
//                //if (emailEvent != null && emailEvent.getCc() != null && !emailEvent.getCc().isEmpty())
//                //message.setCc(emailEvent.getCc());
//                message.setSubject("New Meeting request");
//                message.setText(msgBody);
//                message.setFrom(kpiConfiguration.getFromAddress());
//            };
//
//            //Send the mail
//            try {
//                javaMailSender.send(preparator);
//                //Log the Email Event
//                log.info("Email to {}: {} Sent Successfully using {}", emailId, msgBody, kpiConfiguration);
//            } catch (Exception e) {
//                log.error("Error while Sending Email : {} using {}", msgBody, kpiConfiguration);
//                e.printStackTrace();
//            }
//        }
//        else
//        {
//            String clientId = kpiConfiguration.getClientId();
//            String clientSecret = kpiConfiguration.getClientSecret();
//            String tenantId = kpiConfiguration.getTenantId();
//            String senderId = kpiConfiguration.getSender();
//            String scope = "https://graph.microsoft.com/.default";
//
//            LinkedList<String> scopes = new LinkedList<>();
//            scopes.add(scope);
//            log.info("before authcodecred");
//
//            ClientSecretCredential clientSecretCredential =
//                    new ClientSecretCredentialBuilder()
//                                .clientId(clientId)
//                                .clientSecret(clientSecret)
//                                .tenantId(tenantId)
//                                .build();
//            final TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(scopes, clientSecretCredential);
//
//            final GraphServiceClient graphClient = (GraphServiceClient) GraphServiceClient
//                        .builder()
//                        .authenticationProvider(tokenCredentialAuthProvider)
//                        .buildClient();
//
//            URL myUrl;
//            try {
//                myUrl = new URL("https://graph.microsoft.com/v1.0/");
//                final String accessToken = tokenCredentialAuthProvider.getAuthorizationTokenAsync(myUrl).get();
//                log.info("Access token --> " + accessToken);
//            }
//            catch (MalformedURLException | InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//
//            Message message = new Message();
//
//            message.subject = "Welcome to Queberry Solutions";
//            ItemBody body = new ItemBody();
//            body.contentType = BodyType.TEXT;
//            body.content = msgBody;
//            message.body = body;
//
//            LinkedList<Recipient> toRecipientsList = new LinkedList<>();
//            Recipient toRecipients = new Recipient();
//            EmailAddress emailAddress = new EmailAddress();
//            emailAddress.address = emailId;
//            toRecipients.emailAddress = emailAddress;
//            toRecipientsList.add(toRecipients);
//            message.toRecipients = toRecipientsList;
//
//            log.info("all mail variables set");
//
//        /*LinkedList<Recipient> ccRecipientsList = new LinkedList<Recipient>();
//        Recipient ccRecipients = new Recipient();
//        EmailAddress emailAddress1 = new EmailAddress();
//        emailAddress1.address = "danas@contoso.onmicrosoft.com";
//        ccRecipients.emailAddress = emailAddress1;
//        ccRecipientsList.add(ccRecipients);
//        message.ccRecipients = ccRecipientsList;*/
//
//            boolean saveToSentItems = kpiConfiguration.isSaveEMail();
//
//            try {
//                graphClient.users(senderId)
//                        .sendMail(UserSendMailParameterSet
//                                .newBuilder()
//                                .withMessage(message)
//                                .withSaveToSentItems(saveToSentItems)
//                                .build())
//                        .buildRequest()
//                        .post();
//                //Log the Email Event
//                log.info("Email to {}: {} Sent Successfully using {}", toRecipientsList, msgBody, kpiConfiguration);
//            } catch (Exception e) {
//                log.error("Error while Sending Email : {} using {}", msgBody, kpiConfiguration);
//                e.printStackTrace();
//            }
//        }
//    }
//
//   @Override
//    public void sendAEmail(Appointment appointment, String subject, String emailContent, Branch branch) throws Exception {
//        log.info("send a email");
//        KpiConfiguration kpiConfiguration = kpiConfigurationRepository.findAll().get(0);
//        AppointmentConfiguration appointmentConfiguration = appointmentConfigurationRepository.findByBranchIsNull();
//
//        log.info(emailContent);
//        Path path = Paths.get(baseDirectory + "/" + emailContent + ".html");
////        log.info("path: {}", path);
//
//        String htmlContent = new String(Files.readAllBytes(path));
////        String url = appointmentConfiguration.getHostUrl() + "/api/appointments/" + appointment.getId();
//        String url = appointmentConfiguration.getHostUrl() + "/?appointment=" + appointment.getId() + "&agent=no";
//
//        log.info("url:{}", url);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy");
//        String dateconverted = appointment.getDate().format(formatter);
//
//        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        String cdateconverted = appointment.getDate().format(formatter1);
//
//        String address="";
////        address = appointment.getBranch().getName();
//        if (branch.getAddress().getBuilding() != null && !branch.getAddress().getBuilding().trim().isEmpty())
//            address = branch.getAddress().getBuilding().trim();
//
//        if (branch.getAddress().getStreet() != null && !branch.getAddress().getStreet().trim().isEmpty()) {
//            if (!address.isEmpty())
//                address = address + ", " + branch.getAddress().getStreet().trim();
//            else
//                address = branch.getAddress().getStreet().trim();
//        }
//
//        if (branch.getAddress().getArea() != null && !branch.getAddress().getArea().trim().isEmpty()) {
//            if (!address.isEmpty())
//                address = address + ", " + branch.getAddress().getArea().trim();
//            else
//                address = branch.getAddress().getArea().trim();
//        }
//
//        if (branch.getAddress().getCity() != null && !branch.getAddress().getCity().trim().isEmpty()){
//            if (!address.isEmpty())
//                address = address + ", " + branch.getAddress().getCity().trim() + ".";
//            else
//                address = branch.getAddress().getCity().trim() + ".";
//        }
//
//        htmlContent = htmlContent.replace("#NAME#", appointment.getFirstName()).replace("#BRANCH#", branch.getName().trim()).replace("#REFNO#", appointment.getSmsOtp());
//        htmlContent = htmlContent.replace("#DATE#", dateconverted);
//        htmlContent = htmlContent.replace("#TIME#", appointment.getFrom().toString());
//        htmlContent = htmlContent.replace("#ADDRESS#", address);
//        htmlContent = htmlContent.replace("#URL", url);
//        htmlContent = htmlContent.replace("URL", url);
//        htmlContent = htmlContent.replace("#CHECKIN#", appointment.getCheckinCode());
//        htmlContent = htmlContent.replace("#CANDATE#", cdateconverted);
//        htmlContent = htmlContent.replace("#SERVICE#", appointment.getService().getName().trim());
//        htmlContent = htmlContent.replace("white-space:pre-wrap;", "/*! white-space:pre-wrap; */");
//
//        if(appointment.getSchedulingType().equals(Appointment.SchedulingType.GROUP)) {
//            htmlContent = htmlContent.replace("reschedule or","");
//        }
//
//        if(appointmentConfiguration.getCancelReason() != null) {
//            htmlContent = htmlContent.replace("#CANCELREASON#", (appointment.getState().equals(Appointment.State.CANCELLEDBYAGENT))? appointmentConfiguration.getCancelReason() : "");
//        }
//        else {
//            htmlContent = htmlContent.replace("#CANCELREASON#", "");
//        }
//
//        if(appointmentConfiguration.getEditReason()!=null){
//            htmlContent = htmlContent.replace("#RESCHEDULEREASON#", (appointment.getAppointmentType().equals(Appointment.AppointmentType.EDITBYAGENT))? appointmentConfiguration.getEditReason() : "");
//        } else {
//            htmlContent = htmlContent.replace("#RESCHEDULEREASON#", "");
//        }
//
////        log.info("Html:{}", htmlContent);
//
//        subject = subject.replace("#REFNO#", appointment.getSmsOtp());
//
//        if (kpiConfiguration.getExchangeType().equals("AWS SES")) {
//            String host = kpiConfiguration.getHost();
//            int port = kpiConfiguration.getPort();
//            String username = kpiConfiguration.getUsername();
//            String password = kpiConfiguration.getPassword();
//            String to = EncryptionUtil.decrypt(appointment.getEmail());
//            log.info("decrypted email: {}", to);
//            Properties props = new Properties();
//            props.put("mail.smtp.host", host);
//            props.put("mail.smtp.port", port);
//            props.put("mail.smtp.auth", "true");
//            props.put("mail.smtp.starttls.enable", "true"); // Enable TLS
//
//            try {
//                Session session = Session.getInstance(props, new Authenticator() {
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication(username, password);
//                    }
//                });
//
//                log.info("session created");
//                MimeMessage messageq = new MimeMessage(session);
//                messageq.setFrom(new InternetAddress(kpiConfiguration.getFromAddress())); // Set sender email address
//                messageq.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(to)); // Set recipient email address
//                messageq.setSubject(subject); // Set email subject
//                messageq.setContent(htmlContent, "text/html; charset=UTF-8"); // Set HTML content
//
//                log.info("message created");
//
//                Transport.send(messageq);
//                log.info("message sent");
//            } catch (Exception e) {
//                log.info(e.toString());
//                e.printStackTrace();
//            }
//        }
//        else if (kpiConfiguration.getExchangeType().equals("OTHER")) {
//            //Initialize the Java Mail Sender
//            JavaMailSenderImpl javaMailSender = getJavaMailSender(kpiConfiguration);
//
//            String to = EncryptionUtil.decrypt(appointment.getEmail());
//            log.info("decrypted email: {}", to);
//
//            String finalHtmlContent = htmlContent;
//            String finalSubject = subject;
//            MimeMessagePreparator preparator = mimeMessage -> {
//                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
////                message.setTo(appointment.getEmail());
//                message.setTo(to);
//                message.setSubject(finalSubject);
//                message.setText(finalHtmlContent, true);
//                message.setFrom(kpiConfiguration.getFromAddress());
//            };
//
//            //Send the mail
//            try {
//                javaMailSender.send(preparator);
//                log.info("Email to {}: {} Sent Successfully using {}", appointment.getEmail(), htmlContent, kpiConfiguration);
//            } catch (Exception e) {
//                log.error("Error while Sending Email : {} using {}", htmlContent, kpiConfiguration);
//                e.printStackTrace();
//            }
//        } else {
//            String clientId = kpiConfiguration.getClientId();
//            String clientSecret = kpiConfiguration.getClientSecret();
//            String tenantId = kpiConfiguration.getTenantId();
//            String senderId = kpiConfiguration.getSender();
//            String scope = "https://graph.microsoft.com/.default";
//
//            LinkedList<String> scopes = new LinkedList<>();
//            scopes.add(scope);
//
//            log.info("before authcodecred");
//
//            ClientSecretCredential clientSecretCredential =
//                    new ClientSecretCredentialBuilder()
//                            .clientId(clientId)
//                            .clientSecret(clientSecret)
//                            .tenantId(tenantId)
//                            .build();
//            final TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(scopes, clientSecretCredential);
//
//            final GraphServiceClient graphClient = (GraphServiceClient) GraphServiceClient
//                    .builder()
//                    .authenticationProvider(tokenCredentialAuthProvider)
//                    .buildClient();
//
//            URL myUrl;
//            try {
//                myUrl = new URL("https://graph.microsoft.com/v1.0/");
//                final String accessToken = tokenCredentialAuthProvider.getAuthorizationTokenAsync(myUrl).get();
//                log.info("Access token --> " + accessToken);
//            } catch (MalformedURLException | InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//
//            Message message = new Message();
//
//            message.subject = subject;
//            ItemBody body = new ItemBody();
//            body.contentType = BodyType.HTML;
//            body.content = htmlContent;
//            message.body = body;
//
//            String to = EncryptionUtil.decrypt(appointment.getEmail());
//            log.info("decrypted email: {}", to);
//
//            LinkedList<Recipient> toRecipientsList = new LinkedList<>();
//            Recipient toRecipients = new Recipient();
//            EmailAddress emailAddress = new EmailAddress();
////            emailAddress.address = appointment.getEmail();
//            emailAddress.address = to;
//            toRecipients.emailAddress = emailAddress;
//            toRecipientsList.add(toRecipients);
//
//            message.toRecipients = toRecipientsList;
//
////        Recipient fromRecipients = new Recipient();
////        EmailAddress femailAddress = new EmailAddress();
////        femailAddress.address = "Siva.Lakshmi@sequreglobal.com";
////        fromRecipients.emailAddress = femailAddress;
////        message.from = fromRecipients;
//
//            log.info("all mail variables set");
//
//            boolean saveToSentItems = kpiConfiguration.isSaveEMail();
//
//            /*UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
//                    .getPrincipal();
//            String username = userDetails.getUsername();
//            log.info("username:" + username);
//            log.info("user details:" + userDetails);
//            */
//            //Send the mail
//            try {
//                graphClient.users(senderId)
//                        .sendMail(UserSendMailParameterSet
//                                .newBuilder()
//                                .withMessage(message)
//                                .withSaveToSentItems(saveToSentItems)
//                                .build())
//                        .buildRequest()
//                        .post();
//
//                log.info("Email to {}: {} Sent Successfully using {}", appointment.getEmail(), htmlContent, kpiConfiguration);
//            } catch (Exception e) {
//                log.error("Error while Sending Email : {} using {}", htmlContent, kpiConfiguration);
//                e.printStackTrace();
//            }
//        }
//    }


    /**
     * Initialize the JavaMailSender with the Kpi Configuration
     * @param kpiConfiguration
     * @return
     */
//    private JavaMailSenderImpl getJavaMailSender(KpiConfiguration kpiConfiguration) {
//        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
//        javaMailSender.setProtocol(kpiConfiguration.getProtocol());
//        javaMailSender.setHost(kpiConfiguration.getHost());
//        javaMailSender.setPort(kpiConfiguration.getPort());
//        if(kpiConfiguration.getUsername() != null && !kpiConfiguration.getUsername().isEmpty())
//            javaMailSender.setUsername(kpiConfiguration.getUsername());
//        if(kpiConfiguration.getPassword() != null && !kpiConfiguration.getPassword().isEmpty())
//            javaMailSender.setPassword(kpiConfiguration.getPassword());
//        javaMailSender.setJavaMailProperties(getMailProperties(kpiConfiguration));
//        return javaMailSender;
//    }

    /**
     * Initialize the default Email Properties
     * @return
     */
//    private Properties getMailProperties(KpiConfiguration kpiConfiguration) {
//        Properties properties = new Properties();
//        properties.setProperty("mail.smtp.auth", "true");
//        properties.setProperty("mail.smtp.starttls.enable", "true");
//        properties.setProperty("mail.smtps.ssl.checkserveridentity", "true");
//        properties.setProperty("mail.smtps.ssl.trust", "*");
//        properties.setProperty("mail.debug", "true");
//        properties.setProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
//
//        if (kpiConfiguration.isEhlo()==true)
//            properties.setProperty("mail.smtp.ehlo","true");
//        else
//            properties.setProperty("mail.smtp.ehlo","false");
//        return properties;
//    }
//    @Override
//    public void sendOtpEmail(String emailId, String otp) throws Exception {
//        log.info("Sending OTP email to {}", emailId);
//
//        KpiConfiguration kpiConfiguration = kpiConfigurationRepository.findAll().get(0);
//
//        if (!kpiConfiguration.isEnabled()) {
//            log.info("Email sending is disabled in configuration.");
//            return;
//        }
//
//        // Get template from uploaded file using emailUploadId
//        String templateId = kpiConfiguration.getEmailUploadId();
//        System.out.println("Upload ID:"+kpiConfiguration.getEmailUploadId());
//        if (templateId == null || templateId.trim().isEmpty()) {
//            log.error("No emailUploadId configured");
//            return;
//        }
//
//        Path path = Paths.get(baseDirectory + "/" + templateId + ".html");
//        if (!Files.exists(path)) {
//            log.error("Template file not found at path: {}", path);
//            return;
//        }

//        String htmlContent = new String(Files.readAllBytes(path));
//        htmlContent = htmlContent.replace("#OTP#", otp);
//
//        String subject = (kpiConfiguration.getEmailSubjectBody() != null && !kpiConfiguration.getEmailSubjectBody().isEmpty())
//                ? kpiConfiguration.getEmailSubjectBody()
//                : "Your OTP Code";
//
//        if ("AWS SES".equalsIgnoreCase(kpiConfiguration.getExchangeType())) {
//            sendOtpViaAwsSes(emailId, subject, htmlContent, kpiConfiguration);
//        } else if ("OTHER".equalsIgnoreCase(kpiConfiguration.getExchangeType())) {
//            sendOtpViaJavaMail(emailId, subject, htmlContent, kpiConfiguration);
//        } else {
//            sendOtpViaGraphApi(emailId, subject, htmlContent, kpiConfiguration);
//        }
//
//        log.info("OTP email sent to {}", emailId);
//    }
//    private void sendOtpViaJavaMail(String to, String subject, String htmlContent, KpiConfiguration config) {
//        try {
//            JavaMailSenderImpl mailSender = getJavaMailSender(config);
//            MimeMessagePreparator preparator = mimeMessage -> {
//                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
//                message.setTo(to);
//                message.setSubject(subject);
//                message.setText(htmlContent, true);
//                message.setFrom(config.getFromAddress());
//            };
//            mailSender.send(preparator);
//        } catch (Exception e) {
//            log.error("Error sending OTP via JavaMail", e);
//        }
//    }
//    private void sendOtpViaAwsSes(String to, String subject, String htmlContent, KpiConfiguration config) {
//        try {
//            Properties props = new Properties();
//            props.put("mail.smtp.host", config.getHost());
//            props.put("mail.smtp.port", config.getPort());
//            props.put("mail.smtp.auth", "true");
//            props.put("mail.smtp.starttls.enable", "true");
//
//            Session session = Session.getInstance(props, new Authenticator() {
//                protected PasswordAuthentication getPasswordAuthentication() {
//                    return new PasswordAuthentication(config.getUsername(), config.getPassword());
//                }
//            });
//
//            MimeMessage message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(config.getFromAddress()));
//            message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(to));
//            message.setSubject(subject);
//            message.setContent(htmlContent, "text/html; charset=UTF-8");
//
//            Transport.send(message);
//        } catch (Exception e) {
//            log.error("Error sending OTP via AWS SES", e);
//        }
//    }
//    private void sendOtpViaGraphApi(String to, String subject, String htmlContent, KpiConfiguration config) {
//        try {
//            ClientSecretCredential credential = new ClientSecretCredentialBuilder()
//                    .clientId(config.getClientId())
//                    .clientSecret(config.getClientSecret())
//                    .tenantId(config.getTenantId())
//                    .build();
//
//            TokenCredentialAuthProvider authProvider = new TokenCredentialAuthProvider(
//                    Collections.singletonList("https://graph.microsoft.com/.default"), credential);
//
//            GraphServiceClient<?> graphClient = GraphServiceClient
//                    .builder()
//                    .authenticationProvider(authProvider)
//                    .buildClient();
//
//            Message message = new Message();
//            message.subject = subject;
//
//            ItemBody body = new ItemBody();
//            body.contentType = BodyType.HTML;
//            body.content = htmlContent;
//            message.body = body;
//
//            Recipient recipient = new Recipient();
//            EmailAddress email = new EmailAddress();
//            email.address = to;
//            recipient.emailAddress = email;
//            message.toRecipients = Collections.singletonList(recipient);
//
//            graphClient.users(config.getSender())
//                    .sendMail(UserSendMailParameterSet.newBuilder()
//                            .withMessage(message)
//                            .withSaveToSentItems(config.isSaveEMail())
//                            .build())
//                    .buildRequest()
//                    .post();
//        } catch (Exception e) {
//            log.error("Error sending OTP via Microsoft Graph", e);
//        }
//    }


}


