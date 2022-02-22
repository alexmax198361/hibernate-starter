import lombok.Cleanup;
import org.hibernate.SessionFactory;
import util.HibernateUtil;


public class HibernateRunner {

    public static void main(String[] args) {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();


    }
}
