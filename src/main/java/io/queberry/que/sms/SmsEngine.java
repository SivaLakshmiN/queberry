//package io.queberry.que.sms;
//
//import io.queberry.que.appointment.Appointment;
//import io.queberry.que.assistance.Assistance;
//import io.queberry.que.branch.Branch;
//import io.queberry.que.branch.BranchRepository;
//import io.queberry.que.config.Appointment.AppointmentConfiguration;
//import io.queberry.que.config.Appointment.AppointmentConfigurationRepository;
//import io.queberry.que.config.EncryptionUtil;
//import io.queberry.que.config.Sms.SmsConfiguration;
//import io.queberry.que.config.Sms.SmsConfigurationRepository;
//import io.queberry.que.enums.Type;
//import io.queberry.que.token.Token;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class SmsEngine {
//
//    private final SmsConfigurationRepository smsConfigurationRepository;
//    private final AppointmentConfigurationRepository appointmentConfigurationRepository;
//    private final BranchRepository branchRepository;
//
//    @Qualifier("ajmanSmsService")
//    private final SmsService ajmanSmsService;
//
//    @Qualifier("vectraSmsService")
//    private final SmsService vectraSmsService;
//
//    @Qualifier("etisalatSmsService")
//    private final SmsService etisalatSmsService;
//
//    @Qualifier("smsCountryService")
//    private final SmsService smsCountryService;
//
//    @Qualifier("etisalatRestSmsService")
//    private final SmsService etisalatRestSmsService;
//
//    @Qualifier("dpWorldSmsService")
//    private final SmsService dpWorldSmsService;
//
//    @Qualifier("emaratSMSService")
//    private final SmsService emaratSMSService;
//
//    @Qualifier("rtaSmsService")
//    private final SmsService rtaSmsService;
//
//    //Added to send confirmation SMS to customer -- AlMaryah Requirement
////    @Async
////    @EventListener
////    public void onAppointment(Appointment.AppointmentCreated appointmentCreated){
////
////        try {
////            log.info("in appointment sms");
////            SmsConfiguration smsConfiguration = smsConfigurationRepository.findAll().get(0);
////            AppointmentConfiguration appointmentConfiguration = appointmentConfigurationRepository.findAll().get(0);
////            if (smsConfiguration.isEnabled()){
////                if (appointmentCreated.getAppointment().getState().equals(Appointment.State.CONFIRMED)){
////                    Appointment appointment = appointmentCreated.getAppointment();
////                    Branch home = branchRepository.findByBranchKey(appointment.getBranch());
////                    String text = null;
////                    //text = "Your Appointment is confirmed. Details: \n Date: " + appointment.getDate() + "\n Branch: " + home.getName() + " \n Service: " + appointment.getService().getName() + "\n Slot: " + appointment.getFrom() + " - " + appointment.getTo() ;
////
////                    text = appointmentConfiguration.getSmsTextEnglish().replace("#BRANCH#", home.getName()).replace("#SERVICE#",appointment.getService().getName()).replace("#DATE#",appointment.getDate().toString()).replace("#SLOT#",appointment.getFrom() + " - " + appointment.getTo()).replace("#CHECKIN#",appointment.getCheckinCode());
//////                    log.info("provider name", smsConfiguration.getProvider());
////                    log.info("appointment confirmation sms:"+text);
////                    send(appointment,text,smsConfiguration);
////                }
////            }
////        }catch (Exception e){
//////            log.error("Error while sending SMS token");
////            e.printStackTrace();
////        }
////    }
////
////    //    @Async
////    @EventListener
////    public void onAppointment(Appointment.AppointmentEdited appointmentEdited){
////
////        try {
////            log.info("in edit appointment sms");
////            SmsConfiguration smsConfiguration = smsConfigurationRepository.findAll().get(0);
////            AppointmentConfiguration appointmentConfiguration = appointmentConfigurationRepository.findAll().get(0);
////            if (smsConfiguration.isEnabled()){
////                if (appointmentEdited.getAppointment().getState().equals(Appointment.State.CONFIRMED) && ((appointmentEdited.getAppointment().getTypeofAppointment().equals(Appointment.ModeofAppointment.F2F))) || (appointmentEdited.getAppointment().getTypeofAppointment().equals(Appointment.ModeofAppointment.VIRTUAL))){
//////                    log.info("all cond");
////                    Appointment appointment = appointmentEdited.getAppointment();
//////                    log.info("appt found");
////                    String text = null;
////                    //Optional<Branch> branch = branchRepository.findById(appointment.getBranch().getId());
//////                    log.info("branch found");
////
////                    //text = "Your Appointment is confirmed. Details: \n Date: " + appointment.getDate() + "\n Branch: " + home.getName() + " \n Service: " + appointment.getService().getName() + "\n Slot: " + appointment.getFrom() + " - " + appointment.getTo() ;
//////                    if (appointment.getTypeofAppointment().equals(Appointment.TypeofAppointment.F2F))
////                    text = appointmentConfiguration.getApptEditedEnglish().replace("#SERVICE#",appointment.getService().getName()).replace("#DATE#",appointment.getDate().toString()).replace("#SLOT#",appointment.getFrom() + " - " + appointment.getTo()).replace("#CHECKIN#",appointment.getCheckinCode());
//////                    else
//////                        text = appointmentConfiguration.getApptEditedEnglish().replace("#BRANCH#",branch.get().getName()).replace("#SERVICE#",appointment.getService().getName()).replace("#DATE#",appointment.getDate().toString()).replace("#SLOT#",appointment.getFrom() + " - " + appointment.getTo()).replace("#CHECKIN#","https://teams.microsoft.com/l/meetup-join/19%3ameeting_Nz");
////
//////                    log.info("provider name", smsConfiguration.getProvider());
////                    log.info("appointment confirmation sms:"+text);
////                    send(appointment,text,smsConfiguration);
////                }
////            }
////        }catch (Exception e){
////            log.error("Error while sending SMS token");
////            e.printStackTrace();
////        }
////    }
////
////    //    @Async
////    @EventListener
////    public void onAppointment(Appointment.AppointmentCancelled appointmentCancelled){
////
////        try {
////            log.info("in cancel appointment sms");
////            SmsConfiguration smsConfiguration = smsConfigurationRepository.findAll().get(0);
////            AppointmentConfiguration appointmentConfiguration = appointmentConfigurationRepository.findAll().get(0);
////            if (smsConfiguration.isEnabled()){
////                if (appointmentCancelled.getAppointment().getState().equals(Appointment.State.CANCELLED) && ((appointmentCancelled.getAppointment().getTypeofAppointment().equals(Appointment.ModeofAppointment.F2F))) || (appointmentCancelled.getAppointment().getTypeofAppointment().equals(Appointment.ModeofAppointment.VIRTUAL))){
////                    Appointment appointment = appointmentCancelled.getAppointment();
////                    String text = null;
////                    //Optional<Branch> branch = branchRepository.findById(appointment.getBranch().getId());
////
////                    //text = "Your Appointment is confirmed. Details: \n Date: " + appointment.getDate() + "\n Branch: " + home.getName() + " \n Service: " + appointment.getService().getName() + "\n Slot: " + appointment.getFrom() + " - " + appointment.getTo() ;
////
////                    text = appointmentConfiguration.getApptCancelEnglish().replace("#SERVICE#",appointment.getService().getName()).replace("#DATE#",appointment.getDate().toString()).replace("#SLOT#",appointment.getFrom() + " - " + appointment.getTo()).replace("#CHECKIN#",appointment.getCheckinCode());
////
//////                    log.info("provider name", smsConfiguration.getProvider());
////                    log.info("appointment confirmation sms:"+text);
////                    send(appointment,text,smsConfiguration);
////                }
////            }
////        }catch (Exception e){
////            log.error("Error while sending SMS token");
////            e.printStackTrace();
////        }
////    }
////
////
////    //    @Async
////    @EventListener
////    public void onAssistance(Assistance.AssistanceCreated assistanceCreated){
////
////        try {
////            log.info("in assistance sms");
////            SmsConfiguration smsConfiguration = smsConfigurationRepository.findAll().get(0);
////            if (smsConfiguration.isEnabled() && smsConfiguration.getChooseMedium()){
////                if ((assistanceCreated.getAssistance().getType().equals(Type.DISPENSER) || assistanceCreated.getAssistance().getType().equals(Type.MEETING)) && assistanceCreated.getAssistance().getMobile() != null && !assistanceCreated.getAssistance().getMobile().isEmpty() && assistanceCreated.getAssistance().getMedium().equals(Token.Medium.SMS)){
////                    Assistance assistance = assistanceCreated.getAssistance();
////                    Branch home = branchRepository.findByBranchKey(assistance.getBranch());
////                    String text = null;
////                    if (assistance.getLanguage().equals("ar"))
////                        text = smsConfiguration.getSmsTextSecondary().replace("#CUSTNAME#",assistance.getName()!=null?assistance.getName():"Customer").replace("#NAME#",home.getName()).replace("#TOKEN#",assistance.getTokenRef()).replace("#SERVICE#",assistance.getService().getName());
////                    else
////                        text = smsConfiguration.getSmsTextEnglish().replace("#CUSTNAME#",assistance.getName()!=null?assistance.getName():"Customer").replace("#NAME#",home.getName()).replace("#TOKEN#",assistance.getTokenRef()).replace("#SERVICE#",assistance.getService().getName());
////                    send(assistance,text,smsConfiguration);
////                }
////            }
////        }catch (Exception e){
////            log.error("Error while sending SMS token");
////            e.printStackTrace();
////        }
////    }
////
////    //    @Async
////    @EventListener
////    public void onAssistance(Assistance.AssistanceCalled assistanceCalled){
////
////        try {
////            log.info("assistance called");
////            SmsConfiguration smsConfiguration = smsConfigurationRepository.findAll().get(0);
////            if (smsConfiguration.isEnabled() && smsConfiguration.getCall()){
////                if (assistanceCalled.getAssistance().getType().equals(Type.DISPENSER) && assistanceCalled.getAssistance().getMobile() != null && !assistanceCalled.getAssistance().getMobile().isEmpty() && assistanceCalled.getAssistance().getMedium().equals(Token.Medium.SMS) && !assistanceCalled.getAssistance().isHasAppointment()){
////                    Assistance assistance = assistanceCalled.getAssistance();
////                    Branch home = branchRepository.findByBranchKey(assistance.getBranch());
////                    String counter = assistance.findLastSession().getCounter()!=null ? assistance.findLastSession().getCounter().getCode() : "";
////                    String type = assistance.findLastSession().getCounter().getType().toString();
////                    //String text =  "Dear Valued Customer, your ticket "+ token +"is called. please proceed to "+ type +" " + counter;
////                    String text = null;
////                    log.info(home.getName() + "  " + assistance.getTokenRef() + "   " + assistance.getService().getName() + "   ");
////
////                    if (assistance.getLanguage().equals("ar"))
////                        text =  smsConfiguration.getSmsCalledTextSecondary().replace("#CUSTNAME#",assistance.getName()!=null?assistance.getName():"Customer").replace("#NAME#",home.getName()).replace("#TOKEN#",assistance.getTokenRef()).replace("#SERVICE#",assistance.getService().getName()).replace("#COUNTER#",counter).replace("#TYPE#",type);
////                    else
////                        text = smsConfiguration.getSmsCalledTextEnglish().replace("#CUSTNAME#",assistance.getName()!=null?assistance.getName():"Customer").replace("#NAME#",home.getName()).replace("#TOKEN#",assistance.getTokenRef()).replace("#SERVICE#",assistance.getService().getName()).replace("#COUNTER#",counter).replace("#TYPE#",type);
////                    log.info("called sms text : "+text);
////
////                    send(assistance,text,smsConfiguration);
////                }
////            }
////        }catch (Exception e){
////            log.error("Error while sending SMS token");
////            e.printStackTrace();
////        }
////    }
////
////    //    @Async
////    @EventListener
////    public void onAssistance(Assistance.AssistanceTransferedToCounter assistanceTransferred){
////
////        try {
////            log.info("transfer counter");
////            SmsConfiguration smsConfiguration = smsConfigurationRepository.findAll().get(0);
////            if (smsConfiguration.isEnabled() && smsConfiguration.getTransfer()){
////                //if (assistanceTransferred.getAssistance().getType().equals(Type.DISPENSER) && assistanceTransferred.getAssistance().getMobile() != null && !assistanceTransferred.getAssistance().getMobile().isEmpty() && assistanceTransferred.getAssistance().getMedium().equals(Token.Medium.SMS)){
////                if (assistanceTransferred.getAssistance().getMobile() != null && !assistanceTransferred.getAssistance().getMobile().isEmpty() && assistanceTransferred.getAssistance().getMedium().equals(Token.Medium.SMS)){
////                    log.info("all cond satisfied");
////                    Assistance assistance = assistanceTransferred.getAssistance();
////                    String token = assistance.getTokenRef();
//////                    String counter = assistance.findLastSession().getCounter().getCode();
////                    String transferCounter = assistance.findLastSession().getTransferredToCounter().getCode();
//////                    log.info("  transferred to  "+ transferCounter);
////                    String type = assistance.findLastSession().getTransferredToCounter().getType().toString();
//////                    String text =  "Dear "+name+", your ticket "+ token +" to meet BOE is transfered to POD " + assistanceTransferred.getCounter().getCode() + ". Please proceed to "+ type +" " + tcounter;
////                    String text = null;
//////                    log.info("before text");
////                    Branch home = branchRepository.findByBranchKey(assistance.getBranch());
////                    if (assistance.getLanguage().equals("ar"))
////                        text =  smsConfiguration.getSmsTransferCounterTextSecondary().replace("#CUSTNAME#",assistance.getName()!=null?assistance.getName():"Customer").replace("#NAME#",home.getName()).replace("#TOKEN#",token).replace("#SERVICE#",assistance.getService().getName()).replace("#TYPE#",type).replace("#TRANSFERTO#", transferCounter);
////                    else
////                        text = smsConfiguration.getSmsTransferCounterTextEnglish().replace("#CUSTNAME#",assistance.getName()!=null?assistance.getName():"Customer").replace("#NAME#",home.getName()).replace("#TOKEN#",assistance.getTokenRef()).replace("#SERVICE#",assistance.getService().getName()).replace("#TYPE#",type).replace("#TRANSFERTO#", transferCounter);
////                    log.info("transferred sms text : "+text);
////
////                    send(assistance,text,smsConfiguration);
////                }
////            }
////        }catch (Exception e){
////            log.error("Error while sending SMS token");
////            e.printStackTrace();
////
//
////    private void send(Assistance assistance, String text, SmsConfiguration smsConfiguration) throws Exception {
////        String mobile;
////        try {
////            mobile = EncryptionUtil.decrypt(assistance.getMobile());
////            log.info("decrypt:{}", mobile);
////        } catch (Exception e){
////            mobile = assistance.getMobile();
////        }
////
////        switch (smsConfiguration.getProvider()){
////            case VECTRAMIMND        :   vectraSmsService.sendSms(mobile,text,assistance.getLanguage());
////                break;
////            case AJMAN_BANK_LOCAL   :   ajmanSmsService.sendSms(mobile,text, assistance.getLanguage());
////                break;
////            case ETISALAT           :   etisalatSmsService.sendSms(mobile,text, assistance.getLanguage());
////                break;
////            case SMS_COUNTRY        :   smsCountryService.sendSms(mobile,text, assistance.getLanguage());
////                break;
////            case ETISALAT_REST      :   etisalatRestSmsService.sendSms(mobile,text, assistance.getLanguage());
////                break;
////            case EMARAT             :   emaratSMSService.sendSms(mobile,text,assistance.getLanguage());
////                break;
////            case DP_REST            :   dpWorldSmsService.sendSms(mobile, text, assistance.getLanguage());
////                break;
////            case RTA                :   rtaSmsService.sendSms(mobile, text, assistance.getLanguage());
////                break;
////        }
////    }
////
////    private void send(Appointment appointment, String text, SmsConfiguration smsConfiguration) throws Exception {
////        String mobile;
////        log.info("mobile:{}", appointment.getMobile());
////        try {
////            mobile = EncryptionUtil.decrypt(appointment.getMobile());
////            log.info("decrypt:{}", mobile);
////        } catch (Exception e){
////            log.info(e.getMessage());
////            log.info("in catch");
////            mobile = appointment.getMobile();
////        }
////        log.info("mobile1:{}", mobile);
////        switch (smsConfiguration.getProvider()){
////            case VECTRAMIMND        :   vectraSmsService.sendSms(mobile,text,"en");
////                break;
////            case AJMAN_BANK_LOCAL   :   ajmanSmsService.sendSms(mobile,text, "en");
////                break;
////            case ETISALAT           :   etisalatSmsService.sendSms(mobile,text, "en");
////                break;
////            case SMS_COUNTRY        :   smsCountryService.sendSms(mobile,text, "en");
////                break;
////            case ETISALAT_REST      :   etisalatRestSmsService.sendSms(mobile,text, "en");
////                break;
////            case EMARAT             :   emaratSMSService.sendSms(mobile,text,"en");
////                break;
////            case DP_REST            :   dpWorldSmsService.sendSms(mobile, text,  "en");
////                break;
////            case RTA                :   rtaSmsService.sendSms(mobile, text, "en");
////                break;
////        }
////    }
////
////    public void send(String encryptMobile,String text, SmsConfiguration smsConfiguration) throws Exception {
////        String mobile;
////        try {
////            mobile = EncryptionUtil.decrypt(encryptMobile);
////            log.info("decrypt:{}", mobile);
////        } catch (Exception e){
////            mobile = encryptMobile;
////        }
////        switch (smsConfiguration.getProvider()){
////            case VECTRAMIMND        :   vectraSmsService.sendSms(mobile,text,"en");
////                break;
////            case AJMAN_BANK_LOCAL   :   ajmanSmsService.sendSms(mobile,text,"en");
////                break;
////            case ETISALAT           :   etisalatSmsService.sendSms(mobile,text,"en");
////                break;
////            case SMS_COUNTRY        :   smsCountryService.sendSms(mobile,text,"en");
////                break;
////            case ETISALAT_REST      :   etisalatRestSmsService.sendSms(mobile,text,"en");
////                break;
////            case EMARAT             :   emaratSMSService.sendSms(mobile,text,"en");
////                break;
////            case DP_REST            :   dpWorldSmsService.sendSms(mobile,text,"en");
////                break;
////            case RTA                :   rtaSmsService.sendSms(mobile,text,"en");
////                break;
////        }
////    }
//}
