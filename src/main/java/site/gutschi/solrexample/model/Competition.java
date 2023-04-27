package site.gutschi.solrexample.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Competition {
    @Id
    private int id;

    @ManyToOne
    private Area area;

    private String name;

    private String code;

    private String type;
}
