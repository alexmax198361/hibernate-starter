import entity.*;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import util.HibernateTestUtil;
import util.HibernateUtil;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

class HibernateRunnerTest {

    @Test
    public void createCompanyTest() {
        @Cleanup SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Company yandex = Company.builder()
                .name("Yandex")
                .build();
        session.save(yandex);

        Programmer sazanovich = Programmer.builder()
                .company(yandex)
                .role(Role.USER)
                .username("sazanovich")
                .language(Language.JAVA)
                .build();
        session.save(sazanovich);

        Manager minaeva = Manager.builder()
                .company(yandex)
                .projectName("EXON")
                .username("minaeva")
                .role(Role.USER)
                .build();
        session.save(minaeva);
        session.flush();

        Programmer programmer = session.get(Programmer.class, 1L);
        User user = session.get(User.class, 2L);
        System.out.println();

        session.getTransaction().commit();
    }

    @Test
    public void fillDatabase() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();

        Company getCompany = session.get(Company.class, 94L);
        if (getCompany == null) {
            getCompany = Company.builder()
                    .name("Google")
                    .build();
            getCompany.getLocale().add(CompanyLocale.of("en", "Google"));
            getCompany.getLocale().add(CompanyLocale.of("ru", "Гугл"));
            session.persist(getCompany);
        }

        User userSazanovich = Programmer.builder()
                .username("sazanovich83av")
                .role(Role.ADMIN)
                .build();
        userSazanovich.setPersonalInfo(PersonalInfo.builder()
                .lastName("Сазанович")
                .firstName("Александр")
                .birthday(new BirthDay(LocalDate.of(1983, 11, 15)))
                .build());

        User userMandrik = Programmer.builder()
                .username("lasthero1987")
                .role(Role.USER)
                .build();

        userMandrik.setPersonalInfo(PersonalInfo.builder()
                .lastName("Мандрик")
                .firstName("Антон")
                .birthday(new BirthDay(LocalDate.of(1987, 9, 14)))
                .build());

        User userMinaeva = Manager.builder()
                .username("minaevaMarina")

                .personalInfo(PersonalInfo.builder()
                        .lastName("Минаева")
                        .firstName("Марина")
                        .birthday(new BirthDay(LocalDate.of(1997, 7, 20)))
                        .build())
                .role(Role.USER)
                .build();
        getCompany.addUser(userSazanovich);
        getCompany.addUser(userMandrik);
        getCompany.addUser(userMinaeva);


        session.getTransaction().commit();
    }

    @Test
    public void sortCompany() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Company company = session.get(Company.class, 94L);
        company.getUsers().forEach((s, user) -> System.out.println(s));
        session.getTransaction().commit();
    }

    @Test
    public void collectionTableTest() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();

        session.getTransaction().begin();
        Company company = session.get(Company.class, 93L);
        CompanyLocale localeEn = CompanyLocale.of("en", "Yandex");
        CompanyLocale localeRu = CompanyLocale.of("ru", "Яндекс");
        company.getLocale().addAll(Arrays.asList(localeEn, localeRu));

        session.getTransaction().commit();
    }

    @Test
    public void collectionTableRemoveElement() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();

        session.getTransaction().begin();
        Company company = session.get(Company.class, 93L);
        company.getLocale().removeIf(companyLocale -> companyLocale.getLang().equals("en"));

        session.getTransaction().commit();
    }

    @Test
    public void checkManyToMany() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();

        User user = session.get(User.class, 15L);
        Chat chat = session.get(Chat.class, 12L);
        UserChat userChat = new UserChat();
        userChat.setChat(chat);
        userChat.setUser(user);

        session.save(user);


        session.getTransaction().commit();
    }

    @Test
    public void checkOneToOne() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();
      /*  User user = User.builder()
                .username("a.sazanovich1")
                .role(Role.USER)
                .build();

        Profile profile = Profile.builder()
                .street("Партизана Германа")
                .language("RU")
                .build();
        session.save(user);


        profile.setUser(user);*/
        User user = session.get(User.class, 10L);


        session.getTransaction().commit();
    }

    @Test
    public void checkAnatation() throws IllegalAccessException, SQLException {
        User user = null;

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