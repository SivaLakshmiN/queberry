//package io.queberry.que.role;
//
//import io.queberry.que.assistance.Assistance;
//import io.queberry.que.assistance.AssistanceRepository;
//import io.queberry.que.config.ConfigurationResource;
//import io.queberry.que.config.ConfigurationService;
//import io.queberry.que.config.Queue.QueueConfigurationRepository;
//import io.queberry.que.counter.Counter;
//import io.queberry.que.counter.CounterRepository;
//import io.queberry.que.employee.Employee;
//import io.queberry.que.employee.EmployeeRepository;
//import io.queberry.que.home.HomeRepository;
//import io.queberry.que.service.ServiceRepository;
//import io.queberry.que.subTransaction.SubTransactionRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.poi.ss.usermodel.*;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Service;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class ReportDownloadService {
//
//    private final CounterRepository counterRepository;
//
//    private final ServiceRepository serviceRepository;
//
//    private final SubTransactionRepository subServiceRepository;
//
//    private final EmployeeRepository employeeRepository;
//
//    private final ReportingService reportingService;
//
//    private final HomeRepository homeRepository;
//    private final QueueConfigurationRepository queueConfigurationRepository;
//
//    private final ConfigurationService configurationService;
//
//    private ConfigurationResource configuration;
//    private final AssistanceRepository assistanceRepository;
//
//    private Long totalLogTime=0L;
//
//    private Long totalBreakTime=0L;
//    private int breakCount;
//
////    @PostConstruct
////    @Scheduled(fixedRate = 300000)
////    public void onInit(){
////        try {
////            configuration = configurationService.getConfiguration();
////        } catch (Exception e){
////            e.printStackTrace();
////            log.info("Will try again in 5 Mins");
////            //onInit();
////        }
////    }
//
//    public ByteArrayOutputStream download(ReportRequest reportRequest){
//
////        if (configuration == null)
////            onInit();
//
//        switch (reportRequest.getType()){
//
//            case DAILY_LIVE_REPORT: return this.exportReportToExcel(reportRequest);
//
//            case COUNTER:   return this.exportCounterReportToExcel(reportRequest);
//
//            case SERVICE:   return this.exportServiceReportToExcel(reportRequest);
//
//            case EMPLOYEE:  return this.exportEmployeeReportToExcel(reportRequest);
//
//            case SUB_SERVICE:   return this.exportSubServiceReportToExcel(reportRequest);
//
//            case TOKEN: return  this.exportTokenReportToExcel(reportRequest);
//
//        }
//
//        return null;
//    }
//
//    public ByteArrayOutputStream exportTokenReportToExcel(ReportRequest reportRequest){
//
//        XSSFWorkbook workbook = null;
//        try {
//            workbook = new XSSFWorkbook(Objects.requireNonNull(ReportDownloadService.class.getResourceAsStream("/tokenreport.xlsx")));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        XSSFSheet sheet = workbook.getSheetAt(0);
//
//        //Heading
//        Row heading = sheet.getRow(0);
//        Cell headingCell = heading.getCell(0);
//        headingCell.setCellValue("Token Report");
//
//        //Entity
//        Row entity = sheet.getRow(1);
//        Cell entityCell = entity.getCell(0);
////        entityCell.setCellValue(configuration.getHome().getName());
//        entityCell.setCellValue(homeRepository.findAll().get(0).getName());
//
//        //Date Row
//        Row date = sheet.getRow(2);
//        Cell dateCell = date.getCell(0);
//        dateCell.setCellValue("From : "+reportRequest.getFrom()+" To : "+reportRequest.getTo());
//
//        int rowNum = 4;
//
//        LocalDateTime start = LocalDateTime.of(reportRequest.getFrom(), LocalTime.MIN);
//        LocalDateTime end = LocalDateTime.of(reportRequest.getTo(), LocalTime.MAX);
//        Set<Assistance> assistances = assistanceRepository.findByCreatedAtBetweenAndBranch(start,end, reportRequest.getBranch().getBranchKey(), Sort.by(Sort.Order.asc("createdAt")));
//
//        Report report;
//        for(Assistance assistance: assistances) {
//            report = new Report(assistance);
//            Row reportRow = sheet.createRow(rowNum++);
//            reportRow = createTokenRow(reportRow,report);
//        }
//
//        sheet.autoSizeColumn(0);
//        sheet.autoSizeColumn(1);
//        sheet.autoSizeColumn(2);
//        sheet.autoSizeColumn(3);
//        sheet.autoSizeColumn(4);
//        sheet.autoSizeColumn(5);
//        sheet.autoSizeColumn(6);
//        sheet.autoSizeColumn(7);
//        sheet.autoSizeColumn(8);
//        sheet.autoSizeColumn(9);
//        sheet.autoSizeColumn(10);
//        sheet.autoSizeColumn(11);
//        sheet.autoSizeColumn(12);
//        sheet.autoSizeColumn(13);
//        sheet.autoSizeColumn(14);
//        sheet.autoSizeColumn(15);
//        sheet.autoSizeColumn(16);
//        sheet.autoSizeColumn(17);
//
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        try {
//            workbook.write(stream);
//            workbook.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return stream;
//    }
//
//    public ByteArrayOutputStream exportCounterReportToExcel(ReportRequest reportRequest){
//
//        List<Counter> counters = new ArrayList<>(0);
//        if (reportRequest.getParties().isAll())
//            counters.addAll(counterRepository.findAllByBranchId(reportRequest.getBranch().getId()));
//        else
//            counters.addAll(counterRepository.findByIdInAndBranchId(reportRequest.getParties().getIds(), reportRequest.getBranch().getId()));
//
//        XSSFWorkbook workbook = null;
//        try {
//            workbook = new XSSFWorkbook(ReportDownloadService.class.getResourceAsStream("/report.xlsx"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        XSSFSheet sheet = workbook.getSheetAt(0);
//
//        //Heading
//        Row heading = sheet.getRow(0);
//        Cell headingCell = heading.getCell(0);
//        headingCell.setCellValue("Counter Report");
//
//        //Entity
//        Row entity = sheet.getRow(1);
//        Cell entityCell = entity.getCell(0);
////        entityCell.setCellValue(configuration.getHome().getName());
////        entityCell.setCellValue(homeRepository.findAll().get(0).getName());
//        entityCell.setCellValue(reportRequest.getBranch().getName());
//
//        //Date Row
//        Row date = sheet.getRow(2);
//        Cell dateCell = date.getCell(0);
//        dateCell.setCellValue("From : "+reportRequest.getFrom()+" To : "+reportRequest.getTo());
//
//        //Headers
//        Row headers = sheet.getRow(3);
//        Cell headersCell = headers.getCell(0);
//        headersCell.setCellValue("Counter");
//
//        Set<Report> reports = new HashSet<>(0);
//
//        int rowNum = 4;
//
//        for (Counter counter : counters){
//            Report report = reportingService.getCounterReportByBranch(counter,reportRequest.getFrom(),reportRequest.getTo(), reportRequest.getBranch().getBranchKey());
//            report.setServingSla(queueConfigurationRepository.findAll().get(0).getSlaServe());
//            report.setWaitingSla(queueConfigurationRepository.findAll().get(0).getSlaWait());
////            report.setServingSla(configuration.getQueue().getSlaServe());
////            report.setWaitingSla(configuration.getQueue().getSlaWait());
//            Row reportRow = sheet.createRow(rowNum++);
//            reportRow = createDataRow(reportRow,report,counter.getName());
//            reports.add(report);
//        }
//
//        Report total = new Report(reports.stream().map(rep -> rep.getAssistances()).flatMap(assistances -> assistances.stream()).collect(Collectors.toSet()), null,null,null,"Total");
//        total.setServingSla(queueConfigurationRepository.findAll().get(0).getSlaServe());
//        total.setWaitingSla(queueConfigurationRepository.findAll().get(0).getSlaWait());
////        total.setServingSla(configuration.getQueue().getSlaServe());
////        total.setWaitingSla(configuration.getQueue().getSlaWait());
//        Row totalRow = sheet.createRow(rowNum++);
//        totalRow = createDataRow(totalRow,total,"Total");
//
//        CellStyle style = workbook.createCellStyle();//Create style
//        XSSFFont font = workbook.createFont();//Create font
//        font.setBold(true);//Make font bold
//        style.setFont(font);//set it to bold
//
//        for(int i = 0; i < totalRow.getLastCellNum(); i++){//For each cell in the row
//            totalRow.getCell(i).setCellStyle(style);//Set the style
//        }
//
//
//
//        sheet.autoSizeColumn(0);
//        sheet.autoSizeColumn(1);
//        sheet.autoSizeColumn(2);
//        sheet.autoSizeColumn(3);
//        sheet.autoSizeColumn(4);
//        sheet.autoSizeColumn(5);
//        sheet.autoSizeColumn(6);
//        sheet.autoSizeColumn(7);
//        sheet.autoSizeColumn(8);
//        sheet.autoSizeColumn(9);
//        sheet.autoSizeColumn(10);
//        sheet.autoSizeColumn(11);
//
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        try {
//            workbook.write(stream);
//            workbook.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return stream;
//    }
//
//    public ByteArrayOutputStream exportServiceReportToExcel(ReportRequest reportRequest){
//
//        List<io.queberry.que.service.Service> services = new ArrayList<>(0);
//        if (reportRequest.getParties().isAll()) {
//            services.addAll(serviceRepository.findAllByRegion(reportRequest.getRegion()));
//        }
//        else {
//            services.addAll(serviceRepository.findByIdInAndRegion(reportRequest.getParties().getIds(), reportRequest.getRegion()));
//        }
//
//        XSSFWorkbook workbook = null;
//        try {
//            workbook = new XSSFWorkbook(ReportDownloadService.class.getResourceAsStream("/report.xlsx"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        XSSFSheet sheet = workbook.getSheetAt(0);
//
//        //Heading
//        Row heading = sheet.getRow(0);
//        Cell headingCell = heading.getCell(0);
//        headingCell.setCellValue("Service Report");
//
//        //Entity
//        Row entity = sheet.getRow(1);
//        Cell entityCell = entity.getCell(0);
////        entityCell.setCellValue(configuration.getHome().getName());
////        entityCell.setCellValue(homeRepository.findAll().get(0).getName());
//        entityCell.setCellValue(reportRequest.getBranch().getName());
//
//        //Date Row
//        Row date = sheet.getRow(2);
//        Cell dateCell = date.getCell(0);
//        dateCell.setCellValue("From : "+reportRequest.getFrom()+" To : "+reportRequest.getTo());
//
//        //Headers
//        Row headers = sheet.getRow(3);
//        Cell headersCell = headers.getCell(0);
//        headersCell.setCellValue("Service");
//
//        Set<Report> reports = new HashSet<>(0);
//
//
//        int rowNum = 4;
//
//        for (io.queberry.que.service.Service service : services){
//            Report report = reportingService.getServiceReportByBranch(service,reportRequest.getFrom(),reportRequest.getTo(), reportRequest.getBranch().getBranchKey());
////            report.setServingSla(configuration.getQueue().getSlaServe());
////            report.setWaitingSla(configuration.getQueue().getSlaWait());
//            report.setServingSla(queueConfigurationRepository.findAll().get(0).getSlaServe());
//            report.setWaitingSla(queueConfigurationRepository.findAll().get(0).getSlaWait());
//            Row reportRow = sheet.createRow(rowNum++);
//            reportRow = createDataRow(reportRow,report,service.getName());
//            reports.add(report);
//        }
//
//        Report total = new Report(reports.stream().map(rep -> rep.getAssistances()).flatMap(assistances -> assistances.stream()).collect(Collectors.toSet()), null,null,null,"Total");
////        total.setServingSla(configuration.getQueue().getSlaServe());
////        total.setWaitingSla(configuration.getQueue().getSlaWait());
//        total.setServingSla(queueConfigurationRepository.findAll().get(0).getSlaServe());
//        total.setWaitingSla(queueConfigurationRepository.findAll().get(0).getSlaWait());
//        Row totalRow = sheet.createRow(rowNum++);
//        totalRow = createDataRow(totalRow,total,"Total");
//        CellStyle style = workbook.createCellStyle();//Create style
//        XSSFFont font = workbook.createFont();//Create font
//        font.setBold(true);//Make font bold
//        style.setFont(font);//set it to bold
//
//        for(int i = 0; i < totalRow.getLastCellNum(); i++){//For each cell in the row
//            totalRow.getCell(i).setCellStyle(style);//Set the style
//        }
//        sheet.autoSizeColumn(0);
//        sheet.autoSizeColumn(1);
//        sheet.autoSizeColumn(2);
//        sheet.autoSizeColumn(3);
//        sheet.autoSizeColumn(4);
//        sheet.autoSizeColumn(5);
//        sheet.autoSizeColumn(6);
//        sheet.autoSizeColumn(7);
//        sheet.autoSizeColumn(8);
//        sheet.autoSizeColumn(9);
//        sheet.autoSizeColumn(10);
//        sheet.autoSizeColumn(11);
//
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        try {
//            workbook.write(stream);
//            workbook.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return stream;
//    }
//
//    public ByteArrayOutputStream exportEmployeeReportToExcel(ReportRequest reportRequest){
//
//        List<Employee> employees = new ArrayList<>(0);
//        if (reportRequest.getParties().isAll())
//            employees.addAll(employeeRepository.findAll());
//        else
//            employees.addAll(employeeRepository.findAllById(reportRequest.getParties().getIds()));
//
//        XSSFWorkbook workbook = null;
//        try {
//            workbook = new XSSFWorkbook(ReportDownloadService.class.getResourceAsStream("/report2.xlsx"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        XSSFSheet sheet = workbook.getSheetAt(0);
//
//        //Heading
//        Row heading = sheet.getRow(0);
//        Cell headingCell = heading.getCell(0);
//        headingCell.setCellValue("User Report");
//
//        //Entity
//        Row entity = sheet.getRow(1);
//        Cell entityCell = entity.getCell(0);
////        entityCell.setCellValue(configuration.getHome().getName());
//        entityCell.setCellValue(reportRequest.getBranch().getName());
//
//        //Date Row
//        Row date = sheet.getRow(2);
//        Cell dateCell = date.getCell(0);
//        dateCell.setCellValue("From : "+reportRequest.getFrom()+" To : "+reportRequest.getTo());
//
//        //Headers
//        Row headers = sheet.getRow(3);
//        Cell headersCell = headers.getCell(0);
//        headersCell.setCellValue("User");
//
//        Set<Report> reports = new HashSet<>(0);
//
//        int rowNum = 4;
//
//        for (Employee employee : employees){
//            Report report = reportingService.getEmployeeReportByBranch(employee,reportRequest.getFrom(),reportRequest.getTo(),reportRequest.getBranch().getBranchKey());
////            log.info("report:{}, {}, {}",report.getIdleTime(), report.getServeTime(), report.getIdleTime());
////            report.setServingSla(configuration.getQueue().getSlaServe());
////            report.setWaitingSla(configuration.getQueue().getSlaWait());
//            report.setServingSla(queueConfigurationRepository.findAll().get(0).getSlaServe());
//            report.setWaitingSla(queueConfigurationRepository.findAll().get(0).getSlaWait());
//            Row reportRow = sheet.createRow(rowNum++);
//            reportRow = createEDataRow(reportRow,report,employee.getUsername());
//            reports.add(report);
//            totalLogTime+= report.getTotalTime();
//            totalBreakTime = report.getBreakTime();
//            breakCount =  report.getBreakCount();
//        }
//
//        Report total = new Report(reports.stream().map(Report::getAssistances).flatMap(assistances -> assistances.stream()).collect(Collectors.toSet()), null,null,null,"Total",totalLogTime, totalBreakTime, breakCount);
////        total.setServingSla(configuration.getQueue().getSlaServe());
////        total.setWaitingSla(configuration.getQueue().getSlaWait());
//        total.setServingSla(queueConfigurationRepository.findAll().get(0).getSlaServe());
//        total.setWaitingSla(queueConfigurationRepository.findAll().get(0).getSlaWait());
//        Row totalRow = sheet.createRow(rowNum++);
//        totalRow = createEDataRow(totalRow,total,"Total");
//
//        CellStyle style = workbook.createCellStyle();//Create style
//        XSSFFont font = workbook.createFont();//Create font
//        font.setBold(true);//Make font bold
//        style.setFont(font);//set it to bold
//
//        for(int i = 0; i < totalRow.getLastCellNum(); i++){//For each cell in the row
//            totalRow.getCell(i).setCellStyle(style);//Set the style
//        }
//
//
//        sheet.autoSizeColumn(0);
//        sheet.autoSizeColumn(1);
//        sheet.autoSizeColumn(2);
//        sheet.autoSizeColumn(3);
//        sheet.autoSizeColumn(4);
//        sheet.autoSizeColumn(5);
//        sheet.autoSizeColumn(6);
//        sheet.autoSizeColumn(7);
//        sheet.autoSizeColumn(8);
//        sheet.autoSizeColumn(9);
//        sheet.autoSizeColumn(10);
//        sheet.autoSizeColumn(11);
//        sheet.autoSizeColumn(12);
//        sheet.autoSizeColumn(13);
//
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        try {
//            workbook.write(stream);
//            workbook.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return stream;
//    }
//
//    public ByteArrayOutputStream exportSubServiceReportToExcel(ReportRequest reportRequest){
//
//        List<SubTransaction> subServices = new ArrayList<>(0);
//        if (reportRequest.getParties().isAll())
//            subServices.addAll(subServiceRepository.findAll());
//        else
//            subServices.addAll(subServiceRepository.findByIdIn(reportRequest.getParties().getIds()));
//
//        XSSFWorkbook workbook = null;
//        try {
//            workbook = new XSSFWorkbook(ReportDownloadService.class.getResourceAsStream("/report.xlsx"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        XSSFSheet sheet = workbook.getSheetAt(0);
//
//        //Heading
//        Row heading = sheet.getRow(0);
//        Cell headingCell = heading.getCell(0);
//        headingCell.setCellValue("Sub Service Report");
//
//        //Entity
//        Row entity = sheet.getRow(1);
//        Cell entityCell = entity.getCell(0);
////        entityCell.setCellValue(configuration.getHome().getName());
//        entityCell.setCellValue(homeRepository.findAll().get(0).getName());
//
//        //Date Row
//        Row date = sheet.getRow(2);
//        Cell dateCell = date.getCell(0);
//        dateCell.setCellValue("From : "+reportRequest.getFrom()+" To : "+reportRequest.getTo());
//
//        //Headers
//        Row headers = sheet.getRow(3);
//        Cell headersCell = headers.getCell(0);
//        headersCell.setCellValue("Sub Service");
//
//        Set<Report> reports = new HashSet<>(0);
//
//
//        int rowNum = 4;
//
//        for (SubTransaction subService : subServices){
//            Report report = reportingService.getSubServiceReportByBranch(subService.getId(),reportRequest.getFrom(),reportRequest.getTo(),reportRequest.getBranch().getBranchKey());
////            report.setServingSla(configuration.getQueue().getSlaServe());
////            report.setWaitingSla(configuration.getQueue().getSlaWait());
//            report.setServingSla(queueConfigurationRepository.findAll().get(0).getSlaServe());
//            report.setWaitingSla(queueConfigurationRepository.findAll().get(0).getSlaWait());
//
//            Row reportRow = sheet.createRow(rowNum++);
//            reportRow = createDataRow(reportRow,report,subService.getName());
//            reports.add(report);
//        }
//
//        Report total = new Report(reports.stream().map(rep -> rep.getAssistances()).flatMap(assistances -> assistances.stream()).collect(Collectors.toSet()), null,null,null,"Total");
////        total.setServingSla(configuration.getQueue().getSlaServe());
////        total.setWaitingSla(configuration.getQueue().getSlaWait());
//        total.setServingSla(queueConfigurationRepository.findAll().get(0).getSlaServe());
//        total.setWaitingSla(queueConfigurationRepository.findAll().get(0).getSlaWait());
//
//        Row totalRow = sheet.createRow(rowNum++);
//        totalRow = createDataRow(totalRow,total,"Total");
//
//        CellStyle style = workbook.createCellStyle();//Create style
//        XSSFFont font = workbook.createFont();//Create font
//        font.setBold(true);//Make font bold
//        style.setFont(font);//set it to bold
//
//        for(int i = 0; i < totalRow.getLastCellNum(); i++){//For each cell in the row
//            totalRow.getCell(i).setCellStyle(style);//Set the style
//        }
//
//        sheet.autoSizeColumn(0);
//        sheet.autoSizeColumn(1);
//        sheet.autoSizeColumn(2);
//        sheet.autoSizeColumn(3);
//        sheet.autoSizeColumn(4);
//        sheet.autoSizeColumn(5);
//        sheet.autoSizeColumn(6);
//        sheet.autoSizeColumn(7);
//        sheet.autoSizeColumn(8);
//        sheet.autoSizeColumn(9);
//        sheet.autoSizeColumn(10);
//        sheet.autoSizeColumn(11);
//
//
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        try {
//            workbook.write(stream);
//            workbook.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return stream;
//    }
//
//    public ByteArrayOutputStream exportReportToExcel(ReportRequest reportRequest){
//
////        XSSFWorkbook workbook = new XSSFWorkbook();
// //       XSSFSheet sheet = workbook.createSheet("Branch Report");
//
//        XSSFWorkbook workbook = null;
//        try {
////            workbook = new XSSFWorkbook(ReportDownloadService.class.getResourceAsStream("/report.xlsx"));
////            log.info("bfore workbook");
//            InputStream is = this.getClass().getResourceAsStream("/report.xlsx");
////            log.info("input strea: {}",is.read());
//            workbook = new XSSFWorkbook(ReportDownloadService.class.getResourceAsStream("/report.xlsx"));
////            log.info("workbook set");
//           // log.info("workbook: {}",workbook.getName("0"));
////            workbook = new XSSFWorkbook(String.valueOf(new ClassPathResource("report.xlsx")));
//        } catch (Exception e) {
//            log.info("{}",e.toString());
//        }
//        XSSFSheet sheet = workbook.getSheetAt(0);
//        //XSSFSheet sheet = workbook.createSheet("report");
//        //log.info("sheet created");
//        CellStyle style = workbook.createCellStyle();//Create style
//        XSSFFont font = workbook.createFont();//Create font
//        font.setBold(true);//Make font bold
//        style.setFont(font);//set it to bold
//
//       /* int rowNum = 0;
//
//        CellStyle style = workbook.createCellStyle();//Create style
//        Font font = workbook.createFont();//Create font
//        font.setBold(true);//Make font bold
//        font.setFontHeightInPoints((short) 30);
//        style.setFont(font);//set it to bold
//        style.setAlignment(HorizontalAlignment.CENTER);
//        style.setFillForegroundColor(IndexedColors.BLUE.index);
//        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        style.setBorderTop(BorderStyle.MEDIUM);
//        style.setBorderLeft(BorderStyle.MEDIUM);
//        style.setBorderRight(BorderStyle.MEDIUM);
//        style.setBorderBottom(BorderStyle.MEDIUM);
//
//
//        //Heading
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0,10));
//
//        Row heading = sheet.createRow(rowNum++);
//        Cell headingCell = heading.createCell(0);
//        headingCell.setCellValue("Branch Report");
//  //      headingCell.setCellStyle(style);
//
//        //Entity
//        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0,10));
//        Row entity = sheet.createRow(rowNum++);
//        Cell entityCell = entity.createCell(0);
//        entityCell.setCellValue(configuration.getHome().getName());
//    //    entityCell.setCellStyle(style);
//
//        //Date Row
//        Row date = sheet.createRow(rowNum++);
//        //date = createDateRow(workbook,date,reportRequest);
//
//        //Headers
//        Row row = sheet.createRow(rowNum++);
//        row = createExcelReportHeader(workbook,row,null);
//
//        Report report = reportingService.getHomeReport(reportRequest.getFrom(),reportRequest.getTo());
//        Row totalRow = sheet.createRow(rowNum++);
//        log.info(""+report.totalServedInSla());
//
//        totalRow = createDataRow(totalRow,report,null);
//
//        CellStyle style = workbook.createCellStyle();//Create style
//        Font font = workbook.createFont();//Create font
//        font.setBold(true);//Make font bold
//        style.setFont(font);//set it to bold
//
//        for(int i = 0; i < totalRow.getLastCellNum(); i++){//For each cell in the row
//            totalRow.getCell(i).setCellStyle(style);//Set the style
//        }
//
//
//        CellStyle totalStyle = workbook.createCellStyle();//Create style
//        Font totalFont = workbook.createFont();//Create font
//        totalFont.setBold(true);//Make font bold
//        totalStyle.setFont(totalFont);//set it to bold
//
//        /*for(int i = 0; i < totalRow.getLastCellNum(); i++){//For each cell in the row
//            totalRow.getCell(i).setCellStyle(style);//Set the style
//        }*
//
//        sheet.autoSizeColumn(0);
//        sheet.autoSizeColumn(1);
//        sheet.autoSizeColumn(2);
//        sheet.autoSizeColumn(3);
//        sheet.autoSizeColumn(4);
//        sheet.autoSizeColumn(5);
//        sheet.autoSizeColumn(6);
//        sheet.autoSizeColumn(7);
//        sheet.autoSizeColumn(8);
//        sheet.autoSizeColumn(9);
//
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        try {
//            workbook.write(stream);
//            workbook.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }*/
//
//        Row heading = sheet.getRow(0);
//        Cell headingCell = heading.getCell(0);
//        headingCell.setCellValue("Branch Report");
//
//        //Entity
//        Row entity = sheet.getRow(1);
//        Cell entityCell = entity.getCell(0);
////        entityCell.setCellValue(configuration.getHome().getName());
////        entityCell.setCellValue(homeRepository.findAll().get(0).getName());
//        entityCell.setCellValue(reportRequest.getBranch().getName());
//
//        //Date Row
//        Row date = sheet.getRow(2);
//        Cell dateCell = date.getCell(0);
//        dateCell.setCellValue("From : "+reportRequest.getFrom()+" To : "+reportRequest.getTo());
//
//        //Headers
//        Row headers = sheet.getRow(3);
//        Cell headersCell = headers.getCell(0);
//        headersCell.setCellValue("");
//
//        Set<Report> reports = new HashSet<>(0);
//
//        int rowNum = 4;
//
//        Report report = reportingService.getHomeReportByBranch(reportRequest.getFrom(),reportRequest.getTo(),reportRequest.getBranch().getBranchKey());
////        report.setServingSla(configuration.getQueue().getSlaServe());
////        report.setWaitingSla(configuration.getQueue().getSlaWait());
//        report.setServingSla(queueConfigurationRepository.findAll().get(0).getSlaServe());
//        report.setWaitingSla(queueConfigurationRepository.findAll().get(0).getSlaWait());
//
//        Row reportRow = sheet.createRow(rowNum);
//        reportRow = createDataRow(reportRow,report,"");
//        reports.add(report);
//
//        for(int i = 0; i < reportRow.getLastCellNum(); i++){//For each cell in the row
//            reportRow.getCell(i).setCellStyle(style);//Set the style
//        }
//
//        sheet.autoSizeColumn(0);
//        sheet.autoSizeColumn(1);
//        sheet.autoSizeColumn(2);
//        sheet.autoSizeColumn(3);
//        sheet.autoSizeColumn(4);
//        sheet.autoSizeColumn(5);
//        sheet.autoSizeColumn(6);
//        sheet.autoSizeColumn(7);
//        sheet.autoSizeColumn(8);
//        sheet.autoSizeColumn(9);
//        sheet.autoSizeColumn(10);
//        sheet.autoSizeColumn(11);
//
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        try {
//            workbook.write(stream);
//            workbook.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return stream;
//    }
//
//    private Row createExcelReportHeader(Workbook wb,Row row,String partyName){
//
//        CellStyle style = wb.createCellStyle();//Create style
//        Font font = wb.createFont();//Create font
//        font.setBold(true);//Make font bold
//
//        style.setFont(font);//set it to bold
//
//        int colNum = 0;
//
//
//        if (partyName != null) {
//            Cell party = row.createCell(colNum++);
//            party.setCellValue(partyName);
//        }
//
//        Cell avgWaitTime = row.createCell(colNum++);
//        avgWaitTime.setCellValue("Avg Wait Time");
//
//        Cell avgServeTime = row.createCell(colNum++);
//        avgServeTime.setCellValue("Avg Serve Time");
//
//        Cell total = row.createCell(colNum++);
//        total.setCellValue("Total Tokens");
//
//        Cell transferred = row.createCell(colNum++);
//        transferred.setCellValue("Transferred");
//
//        Cell parked = row.createCell(colNum++);
//        parked.setCellValue("Parked");
//
//        Cell noShow = row.createCell(colNum++);
//        noShow.setCellValue("No Show");
//
//        Cell expired = row.createCell(colNum++);
//        expired.setCellValue("Expired");
//
//        for(int i = 0; i < row.getLastCellNum(); i++){//For each cell in the row
//            Cell cell = row.getCell(i);
//            if (cell != null)
//                cell.setCellStyle(style);//Set the sty;e
//        }
//
//        return row;
//    }
//
//    private Row createDataRow(Row reportRow,Report report,String partyname){
//
//        if (partyname != null){
//            Cell party = reportRow.createCell(0);
//            party.setCellValue(partyname);
//        }
//
//        Cell avgWaitTime = reportRow.createCell(2);
//        //avgWaitTime.setCellValue(Math.ceil(report.getAvgWaitTime()/60));
//        avgWaitTime.setCellValue(secConvert(report.getAvgWaitTime().longValue()));
//
//        Cell avgServeTime = reportRow.createCell(3);
//        //avgServeTime.setCellValue(Math.ceil(report.getAvgServeTime()/60));
//        avgServeTime.setCellValue(secConvert(report.getAvgServeTime().longValue()));
//
//        Cell total = reportRow.createCell(1);
//        total.setCellValue(report.getTotalTokens());
//
//        Cell transferred = reportRow.createCell(4);
//        transferred.setCellValue(report.getNoOfTransffered());
//
//        Cell parked = reportRow.createCell(5);
//        parked.setCellValue(report.getNoOfParked());
//
//        Cell noShow = reportRow.createCell(6);
//        noShow.setCellValue(report.getNoOfNoShow());
//
//        Cell expired = reportRow.createCell(7);
//        expired.setCellValue(report.getNoOfExpired());
//
//        Cell serveSlaIn = reportRow.createCell(8);
//        serveSlaIn.setCellValue(report.totalServedInSla());
//
//        Cell serveSlaOut = reportRow.createCell(9);
//        serveSlaOut.setCellValue(report.totalServedOutsideSla());
//
//        Cell serveSlaInPercentage = reportRow.createCell(10);
//        serveSlaInPercentage.setCellValue(report.totalServedInSlaPercentage()+ "%");
//
//        Cell serveSlaOutPercentage = reportRow.createCell(11);
//        serveSlaOutPercentage.setCellValue(report.totalServedOutsideSlaPercentage()+ "%");
//
//        Cell waitSlaIn = reportRow.createCell(12);
//        waitSlaIn.setCellValue(report.totalWaitingInSla());
//
//        Cell waitSlaOut = reportRow.createCell(13);
//        waitSlaOut.setCellValue(report.totalWaitingOutsideSla());
//
//        Cell waitSlaInPercentage = reportRow.createCell(14);
//        waitSlaInPercentage.setCellValue(report.totalWaitingInSlaPercentage()+ "%");
//
//        Cell waitSlaOutPercentage = reportRow.createCell(15);
//        waitSlaOutPercentage.setCellValue(report.totalWaitingOutsideSlaPercentage() + "%");
//
//        return reportRow;
//    }
//
//    private Row createEDataRow(Row reportRow,Report report,String partyname){
//
//        if (partyname != null){
//            Cell party = reportRow.createCell(0);
//            party.setCellValue(partyname);
//        }
//
//        Cell avgWaitTime = reportRow.createCell(2);
//        //avgWaitTime.setCellValue(Math.ceil(report.getAvgWaitTime()/60));
//        avgWaitTime.setCellValue(secConvert(report.getAvgWaitTime().longValue()));
//
//        Cell avgServeTime = reportRow.createCell(3);
//        //avgServeTime.setCellValue(Math.ceil(report.getAvgServeTime()/60));
//        avgServeTime.setCellValue(secConvert(report.getAvgServeTime().longValue()));
//
//        Cell total = reportRow.createCell(1);
//        total.setCellValue(report.getTotalTokens());
//
//        Cell transferred = reportRow.createCell(4);
//        transferred.setCellValue(report.getNoOfTransffered());
//
//        Cell parked = reportRow.createCell(5);
//        parked.setCellValue(report.getNoOfParked());
//
//        Cell noShow = reportRow.createCell(6);
//        noShow.setCellValue(report.getNoOfNoShow());
//
//        Cell expired = reportRow.createCell(7);
//        expired.setCellValue(report.getNoOfExpired());
//
//        Cell serveSlaIn = reportRow.createCell(8);
//        serveSlaIn.setCellValue(report.totalServedInSla());
//
//        Cell serveSlaOut = reportRow.createCell(9);
//        serveSlaOut.setCellValue(report.totalServedOutsideSla());
//
//        Cell serveSlaInPercentage = reportRow.createCell(10);
//        serveSlaInPercentage.setCellValue(report.totalServedInSlaPercentage()+ "%");
//
//        Cell serveSlaOutPercentage = reportRow.createCell(11);
//        serveSlaOutPercentage.setCellValue(report.totalServedOutsideSlaPercentage()+ "%");
//
//        Cell waitSlaIn = reportRow.createCell(12);
//        waitSlaIn.setCellValue(report.totalWaitingInSla());
//
//        Cell waitSlaOut = reportRow.createCell(13);
//        waitSlaOut.setCellValue(report.totalWaitingOutsideSla());
//
//        Cell waitSlaInPercentage = reportRow.createCell(14);
//        waitSlaInPercentage.setCellValue(report.totalWaitingInSlaPercentage()+ "%");
//
//        Cell waitSlaOutPercentage = reportRow.createCell(15);
//        waitSlaOutPercentage.setCellValue(report.totalWaitingOutsideSlaPercentage() + "%");
//
////        log.info("total time: {}", report.getTotalTime());
//
//        Cell totalTime = reportRow.createCell(16);
//        totalTime.setCellValue(secConvert(report.getTotalTime()));
////        log.info("total time: {}", report.getIdleTime());
//        Cell idleTime = reportRow.createCell(17);
//        idleTime.setCellValue(secConvert(report.getIdleTime()));
////        log.info("total time: {}", report.getServeTime());
//        Cell serveTime = reportRow.createCell(18);
//        serveTime.setCellValue(secConvert(report.getServeTime()));
//        Cell breakCount = reportRow.createCell(19);
//        breakCount.setCellValue(report.getBreakCount());
//        Cell breakTime = reportRow.createCell(20);
//        breakTime.setCellValue(secConvert(report.getBreakTime()));
//
//        return reportRow;
//    }
//
//    private Row createTokenRow(Row reportRow,Report report) {
//
//        Cell token = reportRow.createCell(0);
//        token.setCellValue(report.getToken());
//
//        Cell name = reportRow.createCell(1);
//        name.setCellValue(report.getName());
//
//        Cell age = reportRow.createCell(2);
//        age.setCellValue(report.getAge());
//
//        Cell gender = reportRow.createCell(3);
//        gender.setCellValue(report.getGender());
//
//        Cell mobile = reportRow.createCell(4);
//        mobile.setCellValue(report.getMobile());
//
//        Cell email = reportRow.createCell(5);
//        email.setCellValue(report.getEmailId());
//
//        Cell acctid = reportRow.createCell(6);
//        acctid.setCellValue(report.getAccountId());
//
//        Cell acctName = reportRow.createCell(7);
//        acctName.setCellValue(report.getAccountName());
//
//        Cell acctNo = reportRow.createCell(8);
//        acctNo.setCellValue(report.getAccountNo());
//
//        Cell bookingid = reportRow.createCell(9);
//        bookingid.setCellValue(report.getBookingIds().toString());
//
//        Cell status = reportRow.createCell(10);
//        status.setCellValue(report.getTknStatus());
//
//        Cell service = reportRow.createCell(11);
//        service.setCellValue(report.getServiceName());
//
//        Cell created = reportRow.createCell(12);
//        created.setCellValue(report.getTknCreated().toString());
//
//        Cell wait = reportRow.createCell(13);
//        wait.setCellValue(secConvert(report.getWaitTimeInQueue()));
//
//        Cell serve = reportRow.createCell(14);
//        serve.setCellValue(secConvert(report.getServeTime()));
//
//        Cell counter = reportRow.createCell(15);
//        counter.setCellValue(report.getCounterName());
//
//        Cell employ = reportRow.createCell(16);
//        if (report.getEmploy().isEmpty()) {
//            employ.setCellValue("");
//        } else {
//            Employee employee = employeeRepository.findEmployeeById(report.getEmploy()).get();
//            employ.setCellValue(employee.getUsername());
//        }
//        Cell completed = reportRow.createCell(17);
//        completed.setCellValue(report.getTknCompletedAt() != null ? report.getTknCompletedAt().toString() : "");
//
//        return reportRow;
//    }
//
//
//    private String secConvert(long sec)
//    {
//        long HH = sec / 3600;
//        long MM= (sec % 3600) / 60;
//        long SS = sec % 60;
//        //log.info("%02"+HH + ":" + MM + ":" + SS);
////        log.info(String.format("%02d",HH) + ":" + String.format("%02d",MM) + ":" + String.format("%02d",SS));
//        return (String.format("%02d",HH) + ":" + String.format("%02d",MM) + ":" + String.format("%02d",SS));
//    }
//}
