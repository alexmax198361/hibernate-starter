import entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;

public class HibernateRunner {

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory(); Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            User user = User.builder()
                    .username("ivan@gmail.com")
                    .firstName("Ivan")
                    .lastName("Ivanov")
                    .birthday(LocalDate.of(1982, 1, 23))
                    .age(39)
                    .build();
            session.save(user);
            session.getTransaction().commit();
        }


    }
}
