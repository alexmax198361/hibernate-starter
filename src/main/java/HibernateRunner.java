import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import converter.BirthDayConverter;
import entity.BirthDay;
import entity.Role;
import entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;

public class HibernateRunner {

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.addAttributeConverter(new BirthDayConverter(), true);
        configuration.registerTypeOverride(new JsonBinaryType());
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory(); Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            User user = User.builder()
                    .username("ivan123@gmail.com")
                    .firstName("Ivan")
                    .lastName("Ivanov")
                    .role(Role.ADMIN)
                    .birthday(new BirthDay(LocalDate.of(2011, 1, 1)))
                    .info("""
                            {
                                "name": "Alex",
                                "lastname": "Sazanovich"
                            }
                            """)
                    .build();
            session.save(user);
            session.getTransaction().commit();


        }


    }
}
