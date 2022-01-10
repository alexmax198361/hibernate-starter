import entity.PersonalInfo;
import entity.Role;
import entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import util.HibernateUtil;

public class HibernateRunner {

    public static void main(String[] args) {
        User newUser = User.builder()
                .username("test@test.com")
                .role(Role.ADMIN)
                .personalInfo(PersonalInfo.builder()
                        .firstName("Alex")
                        .lastName("Sazanovich")
                        .build())
                .build();
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session sessionOne = sessionFactory.openSession()) {
                sessionOne.getTransaction().begin();
                sessionOne.saveOrUpdate(newUser);
                User user = sessionOne.get(User.class, "test@test.ru");
                sessionOne.getTransaction().commit();


            }


        }


    }
}
