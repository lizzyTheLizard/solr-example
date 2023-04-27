package site.gutschi.solrexample.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Area {
    @Id
    private int id;

    private String name;

    private String code;

    private String flag;
}
