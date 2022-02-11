package util;

import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

import static util.HibernateUtil.configureProperties;

@UtilityClass
public class HibernateTestUtil {

    /**
     * Создаем docker контейнер с версией
     */
    private static final PostgreSQLContainer<?> postrges = new PostgreSQLContainer<>("postgres:13");

    /*
      При запуске теста стартуем контейнер
     */
    static {
        postrges.start();
    }

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
        configureProperties(configuration);
        configuration.setProperty("hibernate.connection.url", postrges.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", postrges.getUsername());
        configuration.setProperty("hibernate.connection.password", postrges.getPassword());
        configuration.configure();
        return configuration.buildSessionFactory();
    }
}
