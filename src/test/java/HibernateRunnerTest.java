import entity.User;
import org.junit.jupiter.api.Test;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

class HibernateRunnerTest {

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

}