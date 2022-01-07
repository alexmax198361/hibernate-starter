import entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import util.HibernateUtil;

public class HibernateRunner {

    public static void main(String[] args) {

        User user = User.builder()
                .username("test@test.ru")
                .firstName("test")
                .lastName("test")
                .build();

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session sessionOne = sessionFactory.openSession()) {
                /**
                 * В первой сессии сохраняем пользователя
                 * получаем два раза, убеждаемся, что происходит один раз select
                 * сетаем поле и делаем снова get
                 */
                sessionOne.getTransaction().begin();
                sessionOne.saveOrUpdate(user);
                User getUserOneWithoutUpdate = sessionOne.get(User.class, "test@test.ru");
                user.setFirstName("test2");

                user.setFirstName("test3");
                sessionOne.getTransaction().commit();


                System.out.println("");
            }
            try (Session sessionTwo = sessionFactory.openSession()) {
                sessionTwo.getTransaction().begin();
                User getUserOneWithoutUpdate = sessionTwo.get(User.class, "test@test.ru");
                sessionTwo.getTransaction().commit();
            }


        }


    }
}
