package entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
@Builder
@EqualsAndHashCode(exclude = "users")
@ToString(exclude = "users")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<User> users = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "company_locale")
    @Builder.Default
    private List<CompanyLocale> locale = new ArrayList<>();

    public void addUser(User user) {
        users.add(user);
        user.setCompany(this);
    }

}
