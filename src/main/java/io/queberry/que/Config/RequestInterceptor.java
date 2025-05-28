//package io.queberry.que.Config;
//
////import io.queberry.que.jwt.JwtRequestFilter;
////import io.queberry.que.jwt.JwtRequestFilter;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Profile;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.ModelAndView;
//import java.util.ArrayList;
//import java.util.Arrays;
//
///**
// * Created by suman.das on 7/23/19.
// */
//@Slf4j
//@Component
//@Profile("dev")
//public class RequestInterceptor extends HandlerInterceptorAdapter {
//
////    @Autowired
////    private LicenseBean license;
//
//    ArrayList<String> tenantList= new ArrayList<>(Arrays.asList("queberry","economic_courier_and_freight","govt_district_headquarters","govt_rajaji_hospital","govt_vellore_medical_college", "institute_of_child_health",
//            "queberry_dubai", "queberry_india"));
//
//
//    @Override
//    public boolean preHandle(HttpServletRequest request,
//                             HttpServletResponse response, Object object) throws Exception {
//
//
//            response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
//            String requestURI = request.getRequestURI();
//            String tenantID = "";
//
//            if (request.getMethod().equals("OPTIONS") && request.getHeader("X-TenantID") == null) {
//                tenantID = "queberry";
//            } else {
//                tenantID = request.getHeader("X-TenantID");
//            }
//            if (requestURI.contains("/api/download/")) {
//                tenantID = "queberry";
//            }
//            log.info("RequestURI::" + requestURI + " || Search for X-TenantID  :: " + tenantID);
//            if (tenantID == null) {
//                response.getWriter().write("X-TenantID not present in the Request Header");
//                response.setStatus(400);
//                return false;
//            }
//
//            String branchKey = tenantID;
//            TenantContext.setBranchKey(branchKey); // branch key
////            String tenantInfo = tenantID.substring(0,9);
//            TenantContext.setCurrentTenant("queberry"); // org key only for onprem
//            return true;
//    }
//
//// for cloud
////    @Override
////    public boolean preHandle(HttpServletRequest request,
////                             HttpServletResponse response, Object object) throws Exception {
////
////
////        response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
////        String requestURI = request.getRequestURI();
////        String tenantID = "";
////
////        if (request.getMethod().equals("OPTIONS") && request.getHeader("X-TenantID") == null) {
////            tenantID = "queberry";
////        } else {
////            tenantID = request.getHeader("X-TenantID");
////        }
////        if (requestURI.contains("/api/download/")) {
////            tenantID = "queberry";
////        }
////        log.info("RequestURI::" + requestURI + " || Search for X-TenantID  :: " + tenantID);
////        if (tenantID == null) {
////            response.getWriter().write("X-TenantID not present in the Request Header");
////            response.setStatus(400);
////            return false;
////        }
////        if(tenantList.contains(tenantID)){
////            TenantContext.setCurrentTenant(tenantID);
////            TenantContext.setBranchKey(tenantID);
////        }
////        else{
////            String branchKey = tenantID;
////            TenantContext.setBranchKey(branchKey); // branch key
////            String tenantInfo = tenantID.substring(0,9);
////            TenantContext.setCurrentTenant(tenantInfo); // org key
////        }
////        return true;
////    }
//
//
//    @Override
//    public void postHandle(
//            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
//            throws Exception {
////        log.info("in post handle");
//        TenantContext.clear();
//    }
//
//    @Override
//    public void afterCompletion
//            (HttpServletRequest request, HttpServletResponse response, Object
//                    handler, Exception exception) throws Exception {
//
////        log.info("Request and Response is completed");
//    }
//
//}
