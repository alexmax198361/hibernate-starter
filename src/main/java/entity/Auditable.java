package entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
public class Auditable {

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "create_by")
    private String createBy;

}
