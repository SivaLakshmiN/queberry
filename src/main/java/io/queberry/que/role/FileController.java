package io.queberry.que.role;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.ByteArrayOutputStream;

@RequiredArgsConstructor
public class FileController {

//    private final ReportDownloadService downloadService;

//    private final PdfRefortService pdfRefortService;

//    @PutMapping("/reporting/report/download/excel")
//    public ResponseEntity<?> downloadExcel(@RequestBody ReportRequest reportRequest) throws Exception{
//        ByteArrayOutputStream stream = downloadService.download(reportRequest);
//        return fileToResponseEntity(stream,"QueberryReport.xlsx");
//    }

//    @PutMapping("/reporting/report/download/pdf")
//    public ResponseEntity<?> downloadPdf(@RequestBody ReportRequest reportRequest) throws Exception{
//        ByteArrayOutputStream stream = pdfRefortService.download(reportRequest);
//        return fileToResponseEntity(stream,"QueberryReport.pdf");
//    }

    private ResponseEntity fileToResponseEntity(ByteArrayOutputStream stream,String name) throws Exception{
        byte fileBytes[] = stream.toByteArray();
        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.setContentType(MediaType.parseMediaType("application/octet-stream"));
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                name);
        responseHeader.add(headerKey, headerValue);
        return new ResponseEntity(fileBytes, responseHeader, HttpStatus.OK);
    }
}
