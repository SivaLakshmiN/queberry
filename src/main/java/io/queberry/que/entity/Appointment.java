package io.queberry.que.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.queberry.que.exception.QueueException;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Slf4j
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Appointment extends AggregateRoot<Appointment> {

}

//    private String branch;
//    private String branchName;
//    @ManyToOne
//    private Service service;
//    private LocalDateTime createdAt = LocalDateTime.now();
//    private LocalDate date;
//    @Column(name = "_FROM")
//    private LocalTime from;
//    @Column(name = "_TO")
//    private LocalTime to;
//
//    private String firstName;
//    private String lastName;
//    private String name;
//    private String email;
//    private String mobile;
//    private String smsOtp;
//    private String checkinCode;
//    private LocalDateTime checkedInAt;
//    private String token;
//    @Setter
//    @Enumerated(EnumType.STRING)
//    private State state;
//    private String teamsId;
//    @JsonIgnore
//    private boolean isDelete = Boolean.FALSE;
//    private String qrCode;
//    @JsonIgnore
//    @Setter
//    @Enumerated(EnumType.STRING)
//    private AppointmentType appointmentType=AppointmentType.NEW;
//    @Enumerated(EnumType.STRING)
//    private ModeofAppointment typeofAppointment = ModeofAppointment.F2F;
//    @Enumerated(EnumType.STRING)
//    private SchedulingType schedulingType=SchedulingType.SINGLE;
//    @Column(name="no_of_appointments", nullable=false, columnDefinition = "int default 1")
//    private int noOfAppointments = 1;
//    private String eventId;
//    private String accountId;
//    private String accountName;
//    private String contactId;
//    @Column(name = "key_account", columnDefinition = "bit default 0")
//    private Boolean keyAccount;
//    private String countryName;
//    private String customer;
//    private String tenant;
//    private LocalDateTime appointmentdt;
//    private String assistanceid;
//
//    @ElementCollection(fetch = FetchType.EAGER)
//    private Set<String> srnos;
//
////    public Appointment branch(Branch branch){
////        this.branch = branch;
////        return this;
////    }
//
//    public Appointment(Branch branch, Service service, LocalDate date, LocalTime from, LocalTime to, String firstName, String lastName, String email, String mobile, int noOfAppointments,ModeofAppointment type, String tenantId, String clientId, String clientSecret, String userName, AppointmentConfiguration appointmentConfiguration, String baseDirectory) throws IOException {
//        this.branch = branch.getBranchKey();
//        this.branchName = branch.getName();
//        //  this.branch = branch;
//        String smsOtp = getRandom6DigitNumber().toString();
//        log.info("{}",smsOtp);
//        this.service = service;
//        this.date = date;
//        this.from = from;
//        this.to = to;
//        this.name = firstName;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.email = email;
//        this.mobile = mobile;
//        this.noOfAppointments = noOfAppointments;
////        this.smsOtp =  getRandom6DigitNumber().toString();
//        this.smsOtp = smsOtp;
//        this.state = State.PENDING_OTP_VERIFICATION;
//        this.appointmentType = AppointmentType.NEW;
//        this.isDelete = Boolean.FALSE;
//        this.typeofAppointment = type;
//        TeamsResponse teamsResponse = new TeamsResponse();
//        if (type.equals(ModeofAppointment.VIRTUAL)) {
////            teamsResponse = getMeetingLink(name,email,date,from,to,tenantId,clientId,clientSecret,userName,subject.replace("#REFNO#", smsOtp),body,appointmentConfiguration.getHostUrl(),this.getId());
//            teamsResponse = getVirtualMeetingLink(name,email,date,from,to,service,branch,tenantId,clientId,clientSecret,userName,appointmentConfiguration.getConfirmedVSubject().replace("#REFNO#", smsOtp),appointmentConfiguration.getConfirmedVContent(),appointmentConfiguration.getHostUrl(),this.getId(), baseDirectory,smsOtp);
//        }
//        this.schedulingType = (this.noOfAppointments == 1)? SchedulingType.SINGLE: SchedulingType.GROUP;
//        this.checkinCode = (type.equals(ModeofAppointment.F2F)? getRandom6DigitNumber().toString() : teamsResponse.getVLink());
//        this.teamsId = teamsResponse.getTeamsId();
//        this.registerEvent(AppointmentCreated.of(this,appointmentConfiguration,branch));
//    }
//
////    public Appointment(Branch branch, Service service, LocalDate date, Slot.SlotTiming timing, MobileUser mobileUser, boolean delete){
////        this.branch = branch;
////        //this.branch = branch;
////        this.service = service;
////        this.date = date;
////        this.from = timing.getFrom();
////        this.to = timing.getTo();
////        this.name = mobileUser.getName();
////        this.email = mobileUser.getEmail();
////        this.mobile = mobileUser.getMobile();
////        this.smsOtp = getRandom6DigitNumber().toString();
////        this.checkinCode = getRandom6DigitNumber().toString();
////        this.isDelete = delete;
////    }
//
//    public Appointment(Branch branch, Service service, LocalDate date, String name, String email, String mobile, boolean keyAccount) {
//        //this.branch = branch;
//        this.branch = branch.getBranchKey();
//        this.service = service;
//        this.date = date;
//        this.name = name;
//        this.email = email;
//        this.mobile = mobile;
//        this.keyAccount = keyAccount;
//        // this.state = State.CHECKEDIN;
//    }
//    public Appointment confirm(String otp) {
//        if (this.smsOtp.equals(otp)) {
//            this.state = State.CONFIRMED;
//            return this;
//        }
//        throw new QueueException("Invalid OTP", HttpStatus.PRECONDITION_FAILED);
//    }
//
//    public Appointment checkin() {
//
//        if ((this.getDate().equals(LocalDate.now())) && (this.state.equals(State.CONFIRMED))) {
//            log.info("in checkin");
//            this.state = State.CHECKEDIN;
//            this.checkedInAt = LocalDateTime.now();
//            this.andEvent(AppointmentCheckedIn.of(this));
//            return this;
//        } else if (!this.state.equals(State.CONFIRMED)) {
//            throw new QueueException("Appointment not confirmed", HttpStatus.PRECONDITION_FAILED);
//        } throw new QueueException("Appointment is expired", HttpStatus.PRECONDITION_FAILED);
//
//        /*
//
//        if (this.state.equals(State.CONFIRMED)) {
//            this.state = State.CHECKEDIN;
//            this.checkedInAt = LocalDateTime.now();
//            return this;
//        }
//        throw new QueueException("Appointment not confirmed", HttpStatus.PRECONDITION_FAILED);*/
//    }
//
//    public Appointment walkIn(){
//        this.state = State.CONFIRMED;
//        this.typeofAppointment = ModeofAppointment.WALKIN;
//        return this;
//    }
//
//
//    public Appointment edit(AppointmentConfiguration appointmentConfiguration, Branch branch){
//        if(this.state.equals(State.CONFIRMED)){
//            this.appointmentType = AppointmentType.EDIT;
////            return this;
//            this.andEvent(AppointmentEdited.of(this,appointmentConfiguration,branch));
//            return this;
//        }
//        throw new QueueException("Appointment Edit failed as the appointment isn't confirmed", HttpStatus.PRECONDITION_FAILED);
//    }
//
//    public Appointment cancel(AppointmentConfiguration appointmentConfiguration, Branch branch){
//        if(this.state.equals(State.CONFIRMED)){
//            this.setDelete(true);
//            this.setState(State.CANCELLED);;
////            return this;
//            this.andEvent(AppointmentCancelled.of(this,appointmentConfiguration, branch));
//            return this;
//        }
//        throw new QueueException("Appointment Cancel failed as the appointment isn't confirmed", HttpStatus.PRECONDITION_FAILED);
//    }
//
//    public Appointment cancelbyAgent(AppointmentConfiguration appointmentConfiguration, Branch branch){
//        if(this.state.equals(State.CONFIRMED)){
//            this.setDelete(true);
//            this.setState(State.CANCELLEDBYAGENT);;
////            return this;
//            this.andEvent(AppointmentCancelled.of(this,appointmentConfiguration, branch));
//            return this;
//        }
//        throw new QueueException("Appointment Cancel failed as the appointment isn't confirmed", HttpStatus.PRECONDITION_FAILED);
//    }
//
//    public Appointment expired(){
//        if(this.state.equals(State.CONFIRMED)){
//            this.setState(State.EXPIRED);;
//            this.andEvent(AppointmentExpired.of(this));
//            return this;
//        }
//        throw new QueueException("Appointment Expiry failed as the appointment isn't confirmed", HttpStatus.PRECONDITION_FAILED);
//    }
//
//    public Appointment complete(){
//        if(this.state.equals(State.CHECKEDIN)){
//            this.setState(State.COMPLETED);
////            return this;
//            this.andEvent(AppointmentCompleted.of(this));
//            return this;
//        }
//        throw new QueueException("Appointment Complete failed as the appointment isn't checked-in", HttpStatus.PRECONDITION_FAILED);
//    }
//
//    public Appointment noshow(){
//        if(this.state.equals(State.CHECKEDIN)){
//            this.setState(State.NOSHOW);
////            return this;
//            this.andEvent(AppointmentNoShow.of(this));
//            return this;
//        }
//        throw new QueueException("Appointment NoShow failed as the appointment isn't checked-in", HttpStatus.PRECONDITION_FAILED);
//    }
//
//    public Appointment token(String token){
//        this.token = token;
//        return this;
//    }
//
////    public Branch getBranchMeta(){
////        return this.getBranch();
////    }
//
//    public Service getServiceMeta(){
//        return this.getService();
//    }
//
//    /*public byte[] generateQr() throws Exception{
//        ObjectMapper mapper = new ObjectMapper();
//        String qrCodeText = mapper.writeValueAsString(this);
//        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
//        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
//        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//        BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, 250, 250, hintMap);
//        int matrixWidth = byteMatrix.getWidth();
//        BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
//        image.createGraphics();
//        Graphics2D graphics = (Graphics2D) image.getGraphics();
//        graphics.setColor(Color.WHITE);
//        graphics.fillRect(0, 0, matrixWidth, matrixWidth);
//        graphics.setColor(Color.BLACK);
//        for (int i = 0; i < matrixWidth; i++) {
//            for (int j = 0; j < matrixWidth; j++) {
//                if (byteMatrix.get(i, j)) {
//                    graphics.fillRect(i, j, 1, 1);
//                }
//            }
//        }
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ImageIO.write(image, "png", baos);
//        byte[] bytes = baos.toByteArray();
//        return bytes;
//    }*/
//
//    public static Integer getRandom6DigitNumber(){
//        Random rnd = new Random();
//        int n = 100000 + rnd.nextInt(900000);
//        return n;
//    }
//
//    public TeamsResponse getVirtualMeetingLink(String name, String email,LocalDate date, LocalTime from, LocalTime to, Service service, Branch branch, String tenantId, String clientId, String clientSecret, String userName,String subject,String emailContent,String hostUrl,String apptId, String baseDirectory, String refNo) throws IOException {
//        log.info("in vlink");
//        String scope = "https://graph.microsoft.com/.default";
//        String accessToken = "";
//        LinkedList<String> scopes = new LinkedList<>();
//        scopes.add(scope);
//
//        ClientSecretCredential clientSecretCredential =
//                new ClientSecretCredentialBuilder()
//                        .clientId(clientId)
//                        .clientSecret(clientSecret)
//                        .tenantId(tenantId)
//                        .build();
//        //log.info("client credential");
//        final TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(scopes, clientSecretCredential);
//        //log.info("after token cred");
//        final GraphServiceClient graphClient = (GraphServiceClient) GraphServiceClient
//                .builder()
//                .authenticationProvider(tokenCredentialAuthProvider)
//                .buildClient();
//
////        log.info("after graph");
//        URL myUrl;
//        try {
//            myUrl = new URL("https://graph.microsoft.com/v1.0/");
//            //          log.info("url");
//            accessToken = tokenCredentialAuthProvider.getAuthorizationTokenAsync(myUrl).get();
//            //          log.info("Access token --> " + accessToken);
//        }
//        catch (MalformedURLException | InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//
//        LinkedList<Option> requestOptions = new LinkedList<Option>();
//        requestOptions.add(new HeaderOption("Prefer", "outlook.timezone=\"Arabian Standard Time\""));
//
//        String htmlContent = virtualEmailContent(name, date, from, service, branch, emailContent,hostUrl, apptId, baseDirectory, refNo);
//        log.info("Html:{}", htmlContent);
//
//        Event event = new Event();
//        event.subject = subject;
//        ItemBody mbody = new ItemBody();
////        body = body.replace("#URL#",url);
////        body = body.replace("#REASON#","");
//        mbody.contentType = BodyType.HTML;
////        mbody.content = "Dear " + name + ", <br>" + body;
//        mbody.content = htmlContent;
//        event.body = mbody;
//        DateTimeTimeZone start = new DateTimeTimeZone();
//        start.dateTime = date + "T" + from+":00";
//        start.timeZone = "Arabian Standard Time";
//        event.start = start;
//        DateTimeTimeZone end = new DateTimeTimeZone();
//        end.dateTime = date + "T" + to +":00";
//        end.timeZone = "Arabian Standard Time";
//        event.end = end;
///*        Location location = new Location();
//        location.displayName = "Harry's Bar";
//        event.location = location;*/
//        LinkedList<Attendee> attendeesList = new LinkedList<Attendee>();
//        Attendee attendees = new Attendee();
//        EmailAddress emailAddress = new EmailAddress();
//        emailAddress.address = email;
//        emailAddress.name = name;
//        attendees.emailAddress = emailAddress;
//        attendees.type = AttendeeType.REQUIRED;
//        attendeesList.add(attendees);
//        event.attendees = attendeesList;
//        event.isOnlineMeeting=true;
//        event.onlineMeetingProvider = OnlineMeetingProviderType.TEAMS_FOR_BUSINESS;
//        event.isReminderOn = true;
//
//        //event.allowNewTimeProposals = true;
//        //event.transactionId = "7E163156-7762-4BEB-A1C6-729EA81755A7";
//
//        //log.info("before event call");
//        TeamsResponse teamsResponse = new TeamsResponse();
//
//        try {
//            Event meetingEvent = (graphClient.users(userName).events()
//                    .buildRequest(requestOptions)
//                    .post(event));
//
////            log.info(meetingEvent.onlineMeeting.joinUrl);
//            log.info(meetingEvent.onlineMeeting.toString());
//            teamsResponse.setTeamsId(meetingEvent.id);
//            teamsResponse.setVLink(meetingEvent.onlineMeeting.joinUrl);
//            return teamsResponse;
//        }
//        catch (Exception e) {
//            log.info(e.getMessage());
//            throw new RuntimeException();
//        }
//    }
//
//    public String virtualEmailContent(String name, LocalDate date, LocalTime from, Service service, Branch branch, String emailContent, String hostUrl, String apptId, String baseDirectory, String refNo) throws IOException {
//        log.info(emailContent);
//        Path path = Paths.get(baseDirectory + "/" + emailContent + ".html");
////        log.info("path: {}", path);
//
//        String htmlContent = new String(Files.readAllBytes(path));
//        String url = hostUrl + "/?appointment=" + apptId + "&agent=no";
//
//        log.info("url:{}", url);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy");
//        String dateconverted = date.format(formatter);
//
//        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        String cdateconverted = date.format(formatter1);
//
//        String address="";
////        address = appointment.getBranch().getName();
//        if (branch.getAddress().getBuilding() != null && branch.getAddress().getBuilding().trim().length() > 0)
//            address = branch.getAddress().getBuilding().trim();
//
//        if (branch.getAddress().getStreet() != null && branch.getAddress().getStreet().trim().length() > 0) {
//            if (!address.isEmpty())
//                address = address + ", " + branch.getAddress().getStreet().trim();
//            else
//                address = branch.getAddress().getStreet().trim();
//        }
//
//        if (branch.getAddress().getArea() != null && branch.getAddress().getArea().trim().length() > 0) {
//            if (!address.isEmpty())
//                address = address + ", " + branch.getAddress().getArea().trim();
//            else
//                address = branch.getAddress().getArea().trim();
//        }
//
//        if (branch.getAddress().getCity() != null && branch.getAddress().getCity().trim().length() > 0){
//            if (!address.isEmpty())
//                address = address + ", " + branch.getAddress().getCity().trim() + ".";
//            else
//                address = branch.getAddress().getCity().trim() + ".";
//        }
//
//        htmlContent = htmlContent.replace("#NAME#", name).replace("#BRANCH#", branch.getName().trim()).replace("#REFNO#", refNo);
//        htmlContent = htmlContent.replace("#DATE#", dateconverted);
//        htmlContent = htmlContent.replace("#TIME#", from.toString());
//        htmlContent = htmlContent.replace("#ADDRESS#", address);
//        htmlContent = htmlContent.replace("#URL", url);
//        htmlContent = htmlContent.replace("URL", url);
//        htmlContent = htmlContent.replace("#CANDATE#", cdateconverted);
//        htmlContent = htmlContent.replace("#SERVICE#", service.getName().trim());
//        htmlContent = htmlContent.replace("white-space:pre-wrap;", "/*! white-space:pre-wrap; */");
//
//        log.info("Html:{}", htmlContent);
//        return(htmlContent);
//    }
//
//    public TeamsResponse getMeetingLink(String name, String email,LocalDate date, LocalTime from, LocalTime to, String tenantId, String clientId, String clientSecret, String userName,String subject,String body,String hostUrl,String apptId) {
//        log.info("in vlink");
//        /*String clientId = "0cb49604-b421-4e62-bbd2-f93c14f508ec";
//        String clientSecret = "zvA8Q~juXtKyUWdd_IaCKDj2W_DscvzcA-MgybFV";
//        String tenantId = "3d01b049-9150-4b38-9152-17396556f7e3";*/
//        /*String clientId = "ad3279ba-9b62-4d01-a3c6-d27ee721d2cb";
//        String clientSecret = "k2Z8Q~OT9QCvLQeSSchNIRHn_DQsasns.HbtPaXV";
//        String tenantId = "e3a14b3f-47bd-45b5-ae37-6452eb122771";*/
//        String scope = "https://graph.microsoft.com/.default";
//        //String senderId = "8b2eaced-6721-4141-89b8-bca7b72614a8";
//
//        /*log.info("config dtls");
//
//        log.info(tenantId);
//        log.info(clientId);
//        log.info(clientSecret);
//        log.info(userName);*/
//        String accessToken = "";
//        LinkedList<String> scopes = new LinkedList<>();
//        scopes.add(scope);
//
//        ClientSecretCredential clientSecretCredential =
//                new ClientSecretCredentialBuilder()
//                        .clientId(clientId)
//                        .clientSecret(clientSecret)
//                        .tenantId(tenantId)
//                        .build();
//        //log.info("client credential");
//        final TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(scopes, clientSecretCredential);
////log.info("after token cred");
//        final GraphServiceClient graphClient = (GraphServiceClient) GraphServiceClient
//                .builder()
//                .authenticationProvider(tokenCredentialAuthProvider)
//                .buildClient();
//
////        log.info("after graph");
//        URL myUrl;
//        try {
//            myUrl = new URL("https://graph.microsoft.com/v1.0/");
//            //          log.info("url");
//            accessToken = tokenCredentialAuthProvider.getAuthorizationTokenAsync(myUrl).get();
//            //          log.info("Access token --> " + accessToken);
//        }
//        catch (MalformedURLException | InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//
//        LinkedList<Option> requestOptions = new LinkedList<Option>();
//        requestOptions.add(new HeaderOption("Prefer", "outlook.timezone=\"Arabian Standard Time\""));
//
//        String url = hostUrl + "/?appointment=" + apptId;
//        log.info("url {}",url);
//
//        Event event = new Event();
//        event.subject = subject;
//        ItemBody mbody = new ItemBody();
//        body = body.replace("#URL#",url);
//        body = body.replace("#REASON#","");
//        mbody.contentType = BodyType.HTML;
//        mbody.content = "Dear " + name + ", <br>" + body;
//        event.body = mbody;
//        DateTimeTimeZone start = new DateTimeTimeZone();
//        start.dateTime = date + "T" + from+":00";
//        start.timeZone = "Arabian Standard Time";
//        event.start = start;
//        DateTimeTimeZone end = new DateTimeTimeZone();
//        end.dateTime = date + "T" + to +":00";
//        end.timeZone = "Arabian Standard Time";
//        event.end = end;
///*        Location location = new Location();
//        location.displayName = "Harry's Bar";
//        event.location = location;*/
//        LinkedList<Attendee> attendeesList = new LinkedList<Attendee>();
//        Attendee attendees = new Attendee();
//        EmailAddress emailAddress = new EmailAddress();
//        emailAddress.address = email;
//        emailAddress.name = name;
//        attendees.emailAddress = emailAddress;
//        attendees.type = AttendeeType.REQUIRED;
//        attendeesList.add(attendees);
//        event.attendees = attendeesList;
//        event.isOnlineMeeting=true;
//        event.onlineMeetingProvider = OnlineMeetingProviderType.TEAMS_FOR_BUSINESS;
//        event.isReminderOn = true;
//        //event.allowNewTimeProposals = true;
//        //event.transactionId = "7E163156-7762-4BEB-A1C6-729EA81755A7";
//
//        //log.info("before event call");
//        TeamsResponse teamsResponse = new TeamsResponse();
//
//        try {
//            Event meetingEvent = (graphClient.users(userName).events()
//                    .buildRequest(requestOptions)
//                    .post(event));
//
//            log.info(meetingEvent.onlineMeeting.joinUrl);
//            teamsResponse.setTeamsId(meetingEvent.id);
//            teamsResponse.setVLink(meetingEvent.onlineMeeting.joinUrl);
//            return teamsResponse;
//        }
//        catch (Exception e) {
//            log.info(e.getMessage());
//            throw new RuntimeException();
//        }
//    }
//
//    public void editMeetingLink(String name,String teamsId,LocalDate date, LocalTime from, LocalTime to,String tenantId, String clientId, String clientSecret, String userName,String subject,String body) {
//        log.info("in edit meeting");
//        String scope = "https://graph.microsoft.com/.default";
//
//        String accessToken = "";
//        LinkedList<String> scopes = new LinkedList<>();
//        scopes.add(scope);
//
//        ClientSecretCredential clientSecretCredential =
//                new ClientSecretCredentialBuilder()
//                        .clientId(clientId)
//                        .clientSecret(clientSecret)
//                        .tenantId(tenantId)
//                        .build();
//        final TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(scopes, clientSecretCredential);
//
//        final GraphServiceClient graphClient = (GraphServiceClient) GraphServiceClient
//                .builder()
//                .authenticationProvider(tokenCredentialAuthProvider)
//                .buildClient();
//
//        URL myUrl;
//        try {
//            myUrl = new URL("https://graph.microsoft.com/v1.0/");
//            accessToken = tokenCredentialAuthProvider.getAuthorizationTokenAsync(myUrl).get();
//        }
//        catch (MalformedURLException | InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//
//        LinkedList<Option> requestOptions = new LinkedList<Option>();
//        requestOptions.add(new HeaderOption("Prefer", "outlook.timezone=\"Arabian Standard Time\""));
//
//        Event event = new Event();
//        event.subject = subject;
//        ItemBody mbody = new ItemBody();
//        mbody.contentType = BodyType.HTML;
////        mbody.content = "Dear " + name + ", <br>" + body;
//        mbody.content = body;
//
//        event.body = mbody;
//        DateTimeTimeZone start = new DateTimeTimeZone();
//        start.dateTime = date + "T" + from+":00";
//        start.timeZone = "Arabian Standard Time";
//        event.start = start;
//        DateTimeTimeZone end = new DateTimeTimeZone();
//        end.dateTime = date + "T" + to +":00";
//        end.timeZone = "Arabian Standard Time";
//        event.end = end;
//        event.isOnlineMeeting=true;
//        event.onlineMeetingProvider = OnlineMeetingProviderType.TEAMS_FOR_BUSINESS;
//        event.isReminderOn = true;
//
//        TeamsResponse teamsResponse = new TeamsResponse();
//
//        try {
//            Event meetingEvent = (graphClient.users(userName).events(teamsId)
//                    .buildRequest(requestOptions)
//                    .patch(event));
//
//            //teamsResponse.setTeamsId(meetingEvent.id);
//            //teamsResponse.setVLink(meetingEvent.onlineMeeting.joinUrl);
//            //return teamsResponse;
//        }
//        catch (Exception e) {
//            log.info(e.getMessage());
//            throw new QueueException("Issue while generating meeting link. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    public void cancelMeetingLink(String teamsId,String tenantId, String clientId, String clientSecret, String userName, String subject, String body, boolean byagent) {
//        log.info("in cancel meeting");
//        String scope = "https://graph.microsoft.com/.default";
//
//        String accessToken = "";
//        LinkedList<String> scopes = new LinkedList<>();
//        scopes.add(scope);
//
//        ClientSecretCredential clientSecretCredential =
//                new ClientSecretCredentialBuilder()
//                        .clientId(clientId)
//                        .clientSecret(clientSecret)
//                        .tenantId(tenantId)
//                        .build();
//        final TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(scopes, clientSecretCredential);
//
//        final GraphServiceClient graphClient = (GraphServiceClient) GraphServiceClient
//                .builder()
//                .authenticationProvider(tokenCredentialAuthProvider)
//                .buildClient();
//
//        URL myUrl;
//        try {
//            myUrl = new URL("https://graph.microsoft.com/v1.0/");
//            accessToken = tokenCredentialAuthProvider.getAuthorizationTokenAsync(myUrl).get();
//        }
//        catch (MalformedURLException | InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//
//        LinkedList<Option> requestOptions = new LinkedList<Option>();
//        requestOptions.add(new HeaderOption("Prefer", "outlook.timezone=\"Arabian Standard Time\""));
//
//        Event event = new Event();
//        event.subject = subject;
//        if(body.length() > 0){
//            ItemBody mbody = new ItemBody();
//            mbody.contentType = BodyType.HTML;
////            mbody.content = "Dear " + name + ", <br>" + body;
//            mbody.content = body;
//
//            event.body = mbody;
//        }
//        DateTimeTimeZone start = new DateTimeTimeZone();
//        start.dateTime = date + "T" + from+":00";
//        start.timeZone = "Arabian Standard Time";
//        event.start = start;
//        DateTimeTimeZone end = new DateTimeTimeZone();
//        end.dateTime = date + "T" + to +":00";
//        end.timeZone = "Arabian Standard Time";
//        event.end = end;
//        event.isOnlineMeeting=true;
//        event.onlineMeetingProvider = OnlineMeetingProviderType.TEAMS_FOR_BUSINESS;
//
//        TeamsResponse teamsResponse = new TeamsResponse();
//
//        try {
//            Event meetingEvent = (graphClient.users(userName).events(teamsId)
//                    .buildRequest(requestOptions)
//                    .patch(event));
//        }
//        catch (Exception e) {
//            log.info(e.getMessage());
//            throw new RuntimeException();
//        }
//
//        try {
//            graphClient.users(userName).events(teamsId)
//                    .cancel(EventCancelParameterSet
//                            .newBuilder()
//                            .build())
//                    .buildRequest()
//                    .post();
//        }
//        catch (Exception e) {
//            log.info(e.getMessage());
//            throw new RuntimeException();
//        }
//    }
//
//    public enum State{
//        PENDING_OTP_VERIFICATION,
//        CONFIRMED,
//        CHECKEDIN,
//        WALKIN,
//        NOSHOW,
//        COMPLETED,
//        CANCELLED,
//        EXPIRED,
//        CANCELLEDBYAGENT
//    }
//    public enum AppointmentType{
//        NEW,
//        EDIT,
//        EDITBYAGENT
//    }
//    public enum ModeofAppointment{
//        F2F,
//        VIRTUAL,
//        WALKIN
//    }
//    public enum SchedulingType{
//        SINGLE,
//        GROUP
//    }
//
//    @Value
//    @EqualsAndHashCode(callSuper = true)
//    @RequiredArgsConstructor(staticName = "of")
//    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
//    public static class AppointmentCreated extends DomainEvent<Appointment> implements AppointmentEvent {
//
//        @AggregateReference
//        final Appointment appointment;
//
//        @AggregateReference
//        final AppointmentConfiguration appointmentConfiguration;
//
//        @AggregateReference
//        final Branch branch;
//    }
//
//
//    @Value
//    @EqualsAndHashCode(callSuper = true)
//    @RequiredArgsConstructor(staticName = "of")
//    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
//    public static class AppointmentEdited extends DomainEvent<Appointment> implements AppointmentEvent {
//
//        @AggregateReference
//        final Appointment appointment;
//
//        @AggregateReference
//        final AppointmentConfiguration appointmentConfiguration;
//
//        @AggregateReference
//        final Branch branch;
//    }
//
//    @Value
//    @EqualsAndHashCode(callSuper = true)
//    @RequiredArgsConstructor(staticName = "of")
//    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
//    public static class AppointmentCheckedIn extends DomainEvent<Appointment> implements AppointmentEvent {
//
//        @AggregateReference
//        final Appointment appointment;
//    }
//
//
//    @Value
//    @EqualsAndHashCode(callSuper = true)
//    @RequiredArgsConstructor(staticName = "of")
//    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
//    public static class AppointmentCancelled extends DomainEvent<Appointment> implements AppointmentEvent {
//
//        @AggregateReference
//        final Appointment appointment;
//
//        @AggregateReference
//        final AppointmentConfiguration appointmentConfiguration;
//
//        @AggregateReference
//        final Branch branch;
//    }
//
//    @Value
//    @EqualsAndHashCode(callSuper = true)
//    @RequiredArgsConstructor(staticName = "of")
//    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
//    public static class AppointmentCompleted extends DomainEvent<Appointment> implements AppointmentEvent {
//
//        @AggregateReference
//        final Appointment appointment;
//    }
//
//    @Value
//    @EqualsAndHashCode(callSuper = true)
//    @RequiredArgsConstructor(staticName = "of")
//    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
//    public static class AppointmentExpired extends DomainEvent<Appointment> implements AppointmentEvent {
//
//        @AggregateReference
//        final Appointment appointment;
//    }
//
//    @Value
//    @EqualsAndHashCode(callSuper = true)
//    @RequiredArgsConstructor(staticName = "of")
//    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
//    public static class AppointmentNoShow extends DomainEvent<Appointment> implements AppointmentEvent {
//
//        @AggregateReference
//        final Appointment appointment;
//    }
//
//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class TeamsResponse{
//        private String teamsId;
//        private String vLink;
//    }
//}
//
