package io.queberry.que.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String databaseUrl;


    @Value("${spring.datasource.username}")
    private String userName;

    @Value("${spring.datasource.password}")
    private String password;

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public String getuserName() {
        return userName;
    }

    public String getpassword() {
        return password;
    }
}
