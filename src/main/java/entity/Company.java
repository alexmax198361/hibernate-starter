package entity;

import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
@Builder
@EqualsAndHashCode(exclude = "users")
@ToString(exclude = "users")
@BatchSize(size = 3)
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    @Builder.Default
    @SortNatural
    @MapKeyColumn(name = "username")
    private Map<String, User> users = new TreeMap<>();

    @ElementCollection
    @CollectionTable(name = "company_locale")
    @Builder.Default
    private List<CompanyLocale> locale = new ArrayList<>();

    public void addUser(User user) {
        users.put(user.getUsername(), user);
        user.setCompany(this);
    }

}
