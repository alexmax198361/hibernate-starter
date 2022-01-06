package converter;

import entity.BirthDay;

import javax.persistence.AttributeConverter;
import java.sql.Date;

import java.util.Optional;

public class BirthDayConverter implements AttributeConverter<BirthDay, Date> {


    @Override
    public Date convertToDatabaseColumn(BirthDay attribute) {
        return Optional.ofNullable(attribute).map(BirthDay::getBirthDay)
                .map(java.sql.Date::valueOf).orElse(null);
    }

    @Override
    public BirthDay convertToEntityAttribute(Date dbData) {
        return Optional.ofNullable(dbData)
                .map(Date::toLocalDate)
                .map(BirthDay::new)
                .orElse(null);
    }
}
