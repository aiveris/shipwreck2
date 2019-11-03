package com.jact.plur.springmvc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@PropertySource("classpath:application-default.properties")
public class PersistenceConfiguration {

    private static final Logger log = LoggerFactory.getLogger( PersistenceConfiguration.class );

    @Autowired
    private Environment env;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "datasource.flyway")
    @FlywayDataSource
    public DataSource flywayDataSource() {

        log.info ( "PROFILES: " + Arrays.toString( env.getActiveProfiles() ) );
        log.info ( "DEFAULTS: " + Arrays.toString( env.getDefaultProfiles()) );

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("datasource.flyway.driver-class-name"));
        dataSource.setUrl(env.getProperty("datasource.flyway.url"));
        dataSource.setUsername(env.getProperty("datasource.flyway.username"));
        dataSource.setPassword(env.getProperty("datasource.flyway.password"));
        return dataSource;
    }
}
