import entity.Company;
import entity.User;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import util.HibernateUtil;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class HibernateRunner {

    public static void main(String[] args) {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();

        Company company = session.get(Company.class, 93L);
        Set<User> userSet = Optional.ofNullable(company.getUsers()).filter(users -> !users.isEmpty()).orElse(new HashSet<>());
        userSet.removeIf(user -> user.getId().equals(6L));
        session.merge(company);
        session.getTransaction().commit();


    }
}
