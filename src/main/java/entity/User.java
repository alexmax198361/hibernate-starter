package entity;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString(exclude = {"company", "profile", "chats"})
@EqualsAndHashCode(of = "username")
@Table(name = "users")
public class User {

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

    @ManyToMany
    @JoinTable(name = "users_chat",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "chat_id"))
    @Builder.Default
    private Set<Chat> chats = new HashSet<>();

    public void addChat(Chat chat) {
        chat.getUsers().add(this);
        this.getChats().add(chat);
    }

}
