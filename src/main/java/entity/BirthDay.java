package entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.YEARS;

@Getter
@Setter
public class BirthDay {

    private LocalDate birthDay;

    public BirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    /**
     * Вычисление возраста
     *
     * @return Возраст пользователя
     */
    public Long getAge() {
        return YEARS.between(birthDay, LocalDate.now());
    }
}
