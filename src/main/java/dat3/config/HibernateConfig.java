package dat3.config;

import dat3.model.Post;
import dat3.model.Role;
import dat3.model.User;
import dat3.model.Visibility;
import jakarta.persistence.EntityManagerFactory;
import lombok.NoArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class HibernateConfig {
    private static EntityManagerFactory entityManagerFactory;
    private static Boolean isTest = false;

    private static EntityManagerFactory buildEntityFactoryConfigDev() {
        try {
            Configuration configuration = new Configuration();

            Properties props = new Properties();

            boolean isDeployed = (System.getenv("DEPLOYED") != null);

            if(isDeployed) {
                String DB_USERNAME = System.getenv("DB_USERNAME");
                String DB_PASSWORD = System.getenv("DB_PASSWORD");
                String CONNECTION_STR = System.getenv("CONNECTION_STR") + ApplicationConfig.getProperty("db.name");
                props.setProperty("hibernate.connection.url", CONNECTION_STR);
                props.setProperty("hibernate.connection.username", DB_USERNAME);
                props.setProperty("hibernate.connection.password", DB_PASSWORD);
            } else {
                props.put("hibernate.connection.url", ApplicationConfig.getProperty("db.connection.string") + ApplicationConfig.getProperty("db.name"));
                props.put("hibernate.connection.username", ApplicationConfig.getProperty("db.username"));
                props.put("hibernate.connection.password", ApplicationConfig.getProperty("db.password"));
                props.put("hibernate.show_sql", "true"); // show sql in console
                props.put("hibernate.format_sql", "true"); // format sql in console
                props.put("hibernate.use_sql_comments", "true"); // show sql comments in console
            }
            props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect"); // dialect for postgresql
            props.put("hibernate.connection.driver_class", "org.postgresql.Driver"); // driver class for postgresql
            props.put("hibernate.archive.autodetection", "class"); // hibernate scans for annotated classes
            props.put("hibernate.current_session_context_class", "thread"); // hibernate current session context
            props.put("hibernate.hbm2ddl.auto", "update"); // hibernate creates tables based on entities

            // Hibernate Default Pool Configuration
            // https://www.mastertheboss.com/hibernate-jpa/hibernate-configuration/configure-a-connection-pool-with-hibernate/
            props.put("hibernate.connection.provider_class", "org.hibernate.hikaricp.internal.HikariCPConnectionProvider");// Maximum waiting time for a connection from the pool
            props.put("hibernate.hikari.connectionTimeout", "10000"); // Minimum number of ideal connections in the pool
            props.put("hibernate.hikari.minimumIdle", "5"); // Maximum number of actual connection in the pool
            props.put("hibernate.hikari.maximumPoolSize", "20"); // Maximum time that a connection is allowed to sit ideal in the pool
            props.put("hibernate.hikari.idleTimeout", "200000"); // Maximum size of statements that has been prepared

            return getEntityManagerFactory(configuration, props);
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static EntityManagerFactory buildEntityFactoryConfigTest() {
        try {
            Configuration configuration = new Configuration();

            Properties props = new Properties();
            props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            props.put("hibernate.connection.driver_class", "org.testcontainers.jdbc.ContainerDatabaseDriver");
            props.put("hibernate.connection.url", "jdbc:tc:postgresql:15.3-alpine3.18:///test_db");
            props.put("hibernate.connection.username", "postgres");
            props.put("hibernate.connection.password", "postgres");
            props.put("hibernate.archive.autodetection", "class");
            props.put("hibernate.show_sql", "true");
            props.put("hibernate.hbm2ddl.auto", "create-drop");

            return getEntityManagerFactory(configuration, props);
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static EntityManagerFactory getEntityManagerFactory(Configuration configuration, Properties props) {
        configuration.setProperties(props);

        getAnnotationConfiguration(configuration);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        System.out.println("Hibernate Java Config serviceRegistry created");

        SessionFactory sf = configuration.buildSessionFactory(serviceRegistry);
        return sf.unwrap(EntityManagerFactory.class);
    }


    private static void getAnnotationConfiguration(Configuration configuration) {
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Role.class);
        configuration.addAnnotatedClass(Post.class);
        configuration.addAnnotatedClass(Visibility.class);
    }

    private static EntityManagerFactory getEntityManagerFactoryConfigDev() {
        if (entityManagerFactory == null) entityManagerFactory = buildEntityFactoryConfigDev();
        return entityManagerFactory;
    }

    private static EntityManagerFactory getEntityManagerFactoryConfigTest() {
        if (entityManagerFactory == null) entityManagerFactory = buildEntityFactoryConfigTest();
        return entityManagerFactory;
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        if (isTest) return getEntityManagerFactoryConfigTest();
        return getEntityManagerFactoryConfigDev();
    }

    public static void setTest(Boolean test) {
        isTest = test;
    }

    public static Boolean getTest() {
        return isTest;
    }
}