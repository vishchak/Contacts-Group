package ua.kiev.prog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

// NECESSARY!!! db -> Entity -> DAO/Repos -> Services -> Controllers <- Requests

//config class
@Configuration
//scans package
@ComponentScan("ua.kiev.prog")
//spring interacting mechanism with DB
@EnableTransactionManagement
//"connection" with database.properties file
@PropertySource("classpath:database.properties")
//required annotating for spring MVC proj
@EnableWebMvc
//extends spring class so spring scans it
//class has methods that define what spring would do by default
public class AppConfig extends WebMvcConfigurerAdapter {

    //values from database.properties
    @Value("${db.driver}")
    private String dbDriver;

    @Value("${db.url}")
    private String dbUrl;

    @Value("${db.user}")
    private String dbUser;

    @Value("${db.password}")
    private String dbPassword;

    //based on entity manager
    //implements transactions (start, commit, rollback)
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    //"fabric" that creates entity managers
    //for one thread one EM
    //based on two beans
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory
    (DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
        Properties jpaProp = new Properties();
        jpaProp.put("hibernate.hbm2ddl.auto", "create-drop");

        LocalContainerEntityManagerFactoryBean entityManagerFactory =
                new LocalContainerEntityManagerFactoryBean();

        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactory.setJpaProperties(jpaProp);
        entityManagerFactory.setPackagesToScan("ua.kiev.prog");

        return entityManagerFactory;
    }

    //persistence.xml-like
    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();

        adapter.setShowSql(true);
        adapter.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");

        return adapter;
    }

    //creating data manager
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();

        ds.setDriverClassName(dbDriver);
        ds.setUrl(dbUrl);
        ds.setUsername(dbUser);
        ds.setPassword(dbPassword);

        return ds;
    }

    //method suggests spring where visual part of the app is located
    //basically returns concat. way to some page
    @Bean
    public UrlBasedViewResolver setupViewResolver() {
        UrlBasedViewResolver resolver = new UrlBasedViewResolver();
//folder with pages
        resolver.setPrefix("/dynamic/");
        resolver.setSuffix(".jsp");
        //render using jstl
        resolver.setViewClass(JstlView.class);
        resolver.setOrder(1);

        return resolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                //all the queries to the endpoint /static
                //** means anything
                .addResourceHandler("/static/**")
                //should lead to folder static
                .addResourceLocations("/static/");
    }
}
