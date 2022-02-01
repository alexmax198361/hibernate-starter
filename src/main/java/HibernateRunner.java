import entity.Company;
import entity.Role;
import entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import util.HibernateUtil;

public class HibernateRunner {

    public static void main(String[] args) {
        // создаем сущности

        var company = Company.builder()
                .name("Google")
                .build();
        var user = User.builder()
                .role(Role.ADMIN)
                .username("test@test.com")
                .company(company)
                .build();

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();


        }
    }
}
