package uz.pdp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;


@Configuration
@RequiredArgsConstructor
@PropertySource(value = "classpath:/application.properties")
@EnableTransactionManagement
public class DataSourceConfig {

//    private final Environment environment;

    @Value("${spring.datasource.driver}")
    private String driver;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${hibernate.show_sql}")
    private String showSQL;

    @Value("${hibernate.dialect}")
    private String hibernateDialect;

    @Value("${jakarta.persistence.schema-generation.database.action}")
    private String schemaGenerationAction;

    @Bean
    public DataSource dataSource() {
        final var dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.show_sql", showSQL);
        properties.setProperty("hibernate.dialect", hibernateDialect);
        properties.setProperty(
                "jakarta.persistence.schema-generation.database.action",
                schemaGenerationAction
        );
        return properties;
    }

    @Bean
    public LocalSessionFactoryBean localSessionFactoryBean() {
        final var factoryBean = new LocalSessionFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan("uz.pdp");
        factoryBean.setHibernateProperties(hibernateProperties());
        return factoryBean;
    }

    @Bean
    public HibernateTransactionManager hibernateTransactionManager() {
        final var manager = new HibernateTransactionManager();
        manager.setSessionFactory(localSessionFactoryBean().getObject());
        manager.setDataSource(dataSource());
        return manager;
    }
}