package entity;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = {"company", "profile", "userChats"})
@EqualsAndHashCode(of = "username")
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User implements Comparable<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, name = "username")
    private String username;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Type(type = "jsonb")
    @Column(name = "info")
    private String info;

    private PersonalInfo personalInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserChat> userChats = new ArrayList<>();

    @Override
    public int compareTo(User o) {
        if (personalInfo.getFirstName().compareTo(o.getPersonalInfo().getFirstName()) == 0) {
            return personalInfo.getBirthday().getBirthDay().compareTo(o.getPersonalInfo().getBirthday().getBirthDay());
        }
        return personalInfo.getFirstName().compareTo(o.getPersonalInfo().getFirstName()) > 0 ? -1 : 1;
    }

}
