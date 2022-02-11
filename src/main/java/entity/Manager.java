package entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@DiscriminatorValue("manager")
public class Manager extends User {

    private String projectName;

    @Builder
    public Manager(Long id, String username, Role role, String info, PersonalInfo personalInfo, Company company,
                   Profile profile, List<UserChat> userChats, String projectName) {
        super(id, username, role, info, personalInfo, company, profile, userChats);
        this.projectName = projectName;
    }

}
