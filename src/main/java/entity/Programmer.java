package entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@DiscriminatorValue("programmer")
public class Programmer extends User {

    @Enumerated(EnumType.STRING)
    private Language language;

    @Builder
    public Programmer(Long id, String username, Role role, String info, PersonalInfo personalInfo, Company company,
                      Profile profile, List<UserChat> userChats, Language language) {
        super(id, username, role, info, personalInfo, company, profile, userChats);
        this.language = language;
    }


}
