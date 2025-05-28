package io.queberry.que.config;//package io.queberry.que.config;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.queberry.que.entity.Employee;
//import io.queberry.que.entity.EmployeeSessions;
//import io.queberry.que.repository.EmployeeRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
////import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//import javax.security.sasl.AuthenticationException;
//import java.io.Serializable;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//import java.util.function.Function;
//
//@Slf4j
//@Component
//public class JwtTokenUtil {
//
////	private static final long serialVersionUID = -2550185165626007488L;
//
//    //	public static final long JWT_TOKEN_VALIDITY = 1800000; // 30 mins
//    public static final long JWT_TOKEN_VALIDITY = 3600000; // 60 mins
//    //    public static final long JWT_TOKEN_VALIDITY = 60000; // 1 min
//    private String secret = "enterprise";
//
//    @Autowired
//    private EmployeeRepository employeeRepository;
//    //retrieve username from jwt token
//    public String getUsernameFromToken(String token) {
////		log.info("in jwt token util get username");
////		 return getAllClaimsFromToken(token);
//        return getClaimFromToken(token, Claims::getSubject);
//    }
//
//    public String getTokenId(String token) {
////		log.info("in jwt token util get username");
////		 return getAllClaimsFromToken(token);
//        return getClaimFromToken(token, Claims::getId);
//    }
//
//    //retrieve expiration date from jwt token
//    public Date getExpirationDateFromToken(String token) {
//        //	log.info("in jwt token util get exp date");
//        return getClaimFromToken(token, Claims::getExpiration);
//    }
//
//    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = getAllClaimsFromToken(token);
////		log.info("in jwt token util get claim");
////		log.info("claims {}", claims);
//        return claimsResolver.apply(claims);
//    }
//    //for retrieveing any information from token we will need the secret key
//    private Claims getAllClaimsFromToken(String token) {
////		log.info("in jwt token util get all claims");
//        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
//    }
//
//    //check if the token has expired
//    public Boolean isTokenExpired(String token) {
//        final Date expiration = getExpirationDateFromToken(token);
//        //log.info("in jwt token util check exp");
//        return expiration.before(new Date());
//    }
//
//    //generate token for user
//    public String generateToken(Employee user, EmployeeSessions employeeSessions) {
//        Map<String, Object> claims = new HashMap<>();
////		log.info("in jwt token util generate token");
////		claims.put("userId",employeeSessions.getId());
//        return doGenerateToken(claims, user.getUsername(), employeeSessions.getId());
//    }
////    public String generateToken(Employee user, EmployeeSessions employeeSessions) {
////        Map<String, Object> claims = new HashMap<>();
//////		log.info("in jwt token util generate token");
//////		claims.put("userId",employeeSessions.getId());
////        return doGenerateToken(claims, user.getUsername(), employeeSessions.getId());
////    }
//
//    //while creating the token -
//    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
//    //2. Sign the JWT using the HS512 algorithm and secret key.
//    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
//    //   compaction of the JWT to a URL-safe string
//    private String doGenerateToken(Map<String, Object> claims, String subject, String sessId) {
//
//        //log.info("in jwt token util do generate token");
//        return 	Jwts.builder().setClaims(claims).setSubject(subject)
//                .setId(sessId)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY ))  //18000 * 10 = 3mins
//                .signWith(SignatureAlgorithm.HS512, secret).compact();
//    }
//
//    public String doGenerateRefreshToken(Map<String, Object> claims, String subject) {
//
//        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
//                .signWith(SignatureAlgorithm.HS512, secret).compact();
//
//    }
//
//    //validate token
//    public Boolean validateToken(String token, String user) {
//        Employee usr = employeeRepository.findByUsername(user);
//        final String username = getUsernameFromToken(token);
//        //log.info("in jwt token util validate token");
//        if(username.equals(usr.getUsername()) && !isTokenExpired(token)){
//            return true;
//        }else{
//            return false;
//        }
//
//
//
//    }
//}
//
