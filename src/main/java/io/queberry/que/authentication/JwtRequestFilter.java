package io.queberry.que.authentication;
import io.jsonwebtoken.ExpiredJwtException;
import io.queberry.que.config.JwtTokenUtil;
import io.queberry.que.config.Tenant.TenantContext;
import io.queberry.que.customer.Customer;
import io.queberry.que.customer.CustomerRepository;
import io.queberry.que.employee.Employee;
import io.queberry.que.employee.EmployeeRepository;
import io.queberry.que.exception.QueueException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private JwtTokenUtil jwtTokenUtil;

    private EmployeeRepository employeeRepository;

    private final CustomerRepository customerRepository;

    JwtRequestFilter(JwtTokenUtil jwtTokenUtil, EmployeeRepository employeeRepository, CustomerRepository customerRepository){
        this.jwtTokenUtil = jwtTokenUtil;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        log.info("ap request:{}", request.getRequestURL());

        // Skip JWT processing for OPTIONS requests (CORS preflight)
        if (request.getMethod().equals("OPTIONS")) {
            chain.doFilter(request, response);
            return;
        }

        final String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

        log.info("token:{}",requestTokenHeader);

        // Extract JWT token from "Bearer " prefix
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            log.info("in if bearer token");
            jwtToken = requestTokenHeader.substring(7);

            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);

                log.info("username:{}", username);
                if (username != null) {
                    // 1. Check if user is an Employee
                    Optional<Employee> employeeOpt = employeeRepository.findEmployeeByUsername(username);
                    log.info(("emp rcord"));

                    if (employeeOpt.isPresent()) {
                        log.info(("emp present"));
                        Employee user = employeeOpt.get();
                        setEmployeeAuthentication(user, request);
                    }
                    // 2. Check if user is a Customer (Queberry tenant)
                    else {
                        log.info(("checking customer"));
                        TenantContext.setCurrentTenant("queberry");
                        Customer customer = customerRepository.findByUsername(username);

                        if (customer != null) {
                            log.info(("customer present"));
                            setCustomerAuthentication(customer, request);
                            TenantContext.setCurrentTenant(request.getHeader("X-TenantId"));
                        } else {
                            log.warn("User not found: {}", username);
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid credentials");
                            return;
                        }
                    }
                }
            } catch (ExpiredJwtException e) {
                handleExpiredToken(e, request, response);
                return;
            } catch (Exception e) {
                log.error("JWT processing error", e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }
        }

        // Add JWT token as HTTP-only cookie
        if (jwtToken != null) {
            addJwtCookie(response, jwtToken);
        }

        chain.doFilter(request, response);
    }

// --- Helper Methods ---

    private void setEmployeeAuthentication(Employee user, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private void setCustomerAuthentication(Customer customer, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                customer, null, Collections.emptyList());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private void handleExpiredToken(ExpiredJwtException ex, HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String isRefreshToken = request.getHeader("isRefreshToken");
        String requestURL = request.getRequestURL().toString();

        if (isRefreshToken != null && isRefreshToken.equals("true") && requestURL.contains("refreshtoken")) {
            allowForRefreshToken(ex, request);
        } else {
            log.warn("JWT token expired: {}", ex.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
        }
    }

    private void addJwtCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(30 * 60); // 30 minutes
        response.addCookie(cookie);
    }

    private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {

        // create a UsernamePasswordAuthenticationToken with null values.
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                null, null, null);
        // After setting the Authentication in the context, we specify
        // that the current user is authenticated. So it passes the
        // Spring Security Configurations successfully.
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        // Set the claims so that in controller we will be using it to create
        // new JWT
        request.setAttribute("claims", ex.getClaims());

    }
}
