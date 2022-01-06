import entity.User;
import org.junit.jupiter.api.Test;

import javax.persistence.Column;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

class HibernateRunnerTest {

    @Test
    public void checkAnatation() throws NoSuchFieldException {
        User user = User.builder()
                .username("ivan@gmail.com")
                .firstName("Ivan")
                .lastName("Ivanov")
                .birthday(LocalDate.of(1982, 1, 23))
                .age(39)
                .build();

        String sql = """
                INSERT INTO %s
                (%s)
                VALUES
                (%s)
                """;
        String tableName = Optional.ofNullable(user.getClass().getAnnotation(Table.class))
                .map(table -> table.schema() + "." + table.name()).orElse(user.getClass().getName());
        String columns = Arrays.stream(user.getClass().getDeclaredFields()).map(
                field -> field.isAnnotationPresent(Column.class) ?
                        field.getAnnotation(Column.class).name() : field.getName()
        ).collect(Collectors.joining(", "));
        System.out.println("");

    }

}