import entity.User;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import util.HibernateUtil;

import java.util.List;


public class HibernateRunner {

    public static void main(String[] args) {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();

/*        User user = session.get(User.class, 1L);
        System.out.println(user.getPayments().size());
        System.out.println(user.getCompany().getName());*/

        List<User> users = session.createQuery("select u from User u", User.class).list();
        users.forEach(user -> System.out.println(user.getPayments().size()));
        users.forEach(user -> System.out.println(user.getCompany().getName()));
        session.getTransaction().commit();
    }
}
