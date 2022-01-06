package entity;

import converter.BirthDayConverter;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    private String username;
    private String firstName;
    private String lastName;
    @Convert(converter = BirthDayConverter.class)
    private BirthDay birthday;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Type(type = "jsonb")
    private String info;


}
