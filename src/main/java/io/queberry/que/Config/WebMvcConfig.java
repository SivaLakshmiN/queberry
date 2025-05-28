package io.queberry.que.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author : fahadfazil
 * @since : 22/12/17
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${user.home}/queberry/halo/announcements/")
    private String announcementStore;


    /*@Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void configureObjectMapper(){
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }*/

    /**
     * When proxied with SSL, all generated links contains SSL.
     *
     * @return
     */
//    @Bean
//    public FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
//        final FilterRegistrationBean<ForwardedHeaderFilter> filterRegistrationBean = new FilterRegistrationBean<ForwardedHeaderFilter>();
//        filterRegistrationBean.setFilter(new ForwardedHeaderFilter());
//        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//        return filterRegistrationBean;
//    }

//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/login").setViewName("login");
////        registry.addViewController("/survey/takeSurvey").setViewName("forward:/survey/index.html");
////        registry.addViewController("/survey/loading").setViewName("forward:/survey/index.html");
//   }


//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor( new RequestInterceptor()).addPathPatterns("/**");
////                .excludePathPatterns("/config","/actuator/**","/login","/favicon.ico","/error","/assets/images/logos/queberry.png","/assets/fonts/Poppins/poppins.woff2","/api/download/**");
//    }

//    @Bean
//    @Profile("dev")
//    public MappedInterceptor cloudInterceptorBean(RequestInterceptor requestInterceptor) {
//        return new MappedInterceptor(new String[]{"/**"}, requestInterceptor);
//    }

    /*@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }*/

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS" ,"HEAD")
//                .allowCredentials(true)
//        ;
//    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/api/**") // Adjust the path as needed
//                .allowedOrigins("http://localhost:4200")
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowedHeaders("*")
//                .allowCredentials(true);
//    }

//    @Bean
//    public ThreadPoolTaskExecutor taskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(10); // Set the core pool size
//        executor.setMaxPoolSize(100); // Set the maximum pool size
//        executor.setQueueCapacity(10); // Set the queue capacity
//        executor.initialize();
//        return executor;
//    }



//    @Bean
//    @Profile("prod")
//    public MappedInterceptor licenseInterceptorBean(LicenseInterceptor licenseInterceptor) {
//        return new MappedInterceptor(new String[]{"/**"},new String[]{"/","/index.html","/login","/setup","/error","/api/home","/api/employee","/assets/**","/favicon.ico","/*.js","/*.css"}, licenseInterceptor);
//    }

//    @Bean
//    @Profile("enterprise")
//    public MappedInterceptor enterpriseInterceptorBean(EnterpriseInterceptor enterpriseInterceptor) {
//        return new MappedInterceptor(new String[]{"/api/config/","/api/config/**","/api/counters","/api/counters/**","/api/services","/api/services/**","/api/employees","/api/employees/**","/api/devices"}, enterpriseInterceptor);
//    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//
//        registry.addResourceHandler("/**/*")
//                .addResourceLocations("classpath:/static/")
//                .resourceChain(true)
//                .addResolver(new PathResourceResolver() {
//                                 @Override
//                                 protected Resource getResource(String resourcePath,
//                                                                Resource location) throws IOException {
//                                     Resource requestedResource = location.createRelative(resourcePath);
//                                     if (requestedResource.exists() && requestedResource.isReadable())
//                                         return requestedResource;
//                                     return requestedResource.exists() && requestedResource.isReadable() ? requestedResource
//                                             : new ClassPathResource("/static/index.html");
//                                 }
//                             }
//                );
//    }
//
//        // Resource handler for announcement
//        String announcementResourceLocation = "file:///" + announcementStore;
//        registry.addResourceHandler("/announcements/**")
//                .addResourceLocations(announcementResourceLocation);
//    }


}
