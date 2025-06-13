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

    private final JwtTokenUtil jwtTokenUtil;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;

    public JwtRequestFilter(JwtTokenUtil jwtTokenUtil,
                            EmployeeRepository employeeRepository,
                            CustomerRepository customerRepository) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String requestPath = request.getRequestURI();
        String header = request.getHeader("Authorization");

        log.debug("Incoming request: {} {}", request.getMethod(), requestPath);

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            String username;

            try {
                username = jwtTokenUtil.getUsernameFromToken(token);
                log.debug("Extracted username: {}", username);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    Optional<Employee> empOpt = employeeRepository.findEmployeeByUsername(username);
                    if (empOpt.isPresent()) {
                        log.debug("Employee authenticated: {}", username);
                        setEmployeeAuthentication(empOpt.get(), request);
                    } else {
                        TenantContext.setCurrentTenant("queberry");
                        Customer customer = customerRepository.findByUsername(username);
                        if (customer != null) {
                            log.debug("Customer authenticated: {}", username);
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
                log.error("JWT validation failed: {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }

            addJwtCookie(response, token); // Optional: only if you use cookie-based auth
        }

        chain.doFilter(request, response);
    }

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

    private void handleExpiredToken(ExpiredJwtException ex,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws IOException {

        String isRefreshToken = request.getHeader("isRefreshToken");
        String requestURL = request.getRequestURL().toString();

        if ("true".equals(isRefreshToken) && requestURL.contains("refreshtoken")) {
            allowForRefreshToken(ex, request);
        } else {
            log.warn("Token expired: {}", ex.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
        }
    }

    private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(null, null, null);
        SecurityContextHolder.getContext().setAuthentication(token);
        request.setAttribute("claims", ex.getClaims());
    }

    private void addJwtCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(1800); // 30 minutes
        response.addCookie(cookie);
    }
}