import entity.Company;
import entity.Profile;
import entity.Role;
import entity.User;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import util.HibernateUtil;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

class HibernateRunnerTest {

    @Test
    public void checkOneToOne() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        User user = User.builder()
                .username("a.sazanovich")
                .role(Role.USER)
                .build();

        Profile profile = Profile.builder()
                .street("Партизана Германа")
                .language("RU")
                .build();
        session.save(user);
        profile.setUser(user);


        session.getTransaction().commit();
    }

    @Test
    public void checkAnatation() throws IllegalAccessException, SQLException {
        User user = User.builder()
                .username("ivan@gmail.com")
                .build();

        String sql = """
                INSERT INTO %s
                (%s)
                VALUES
                (%s)
                """;
        String tableName = ofNullable(user.getClass().getAnnotation(Table.class))
                .map(table -> table.schema() + "." + table.name()).orElse(user.getClass().getName());
        String columns = stream(user.getClass().getDeclaredFields()).map(
                field -> field.isAnnotationPresent(Column.class) ?
                        field.getAnnotation(Column.class).name() : field.getName()
        ).collect(joining(", "));

        String columnValues = stream(user.getClass().getDeclaredFields())
                .map(field -> "?").collect(joining(", "));

        Connection connection = null;
        PreparedStatement preparedStatement = connection.prepareStatement(sql.formatted(tableName, columns, columnValues));
        for (Field field : user.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            preparedStatement.setObject(1, field.get(user));
        }
    }

    @Test
    public void oneToMany() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Company company = session.get(Company.class, 3L);
        System.out.println("Получили компманию");
        ;
        session.getTransaction().commit();
    }

}