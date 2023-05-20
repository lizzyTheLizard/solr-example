package site.gutschi.solrexample.model;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor(force = true)
@Builder
@AllArgsConstructor
public class Game {
    @Id
    private int id;
    @NonNull
    private String title;
    private LocalDate releaseDate;
    @ElementCollection
    private Collection<String> team;
    @ElementCollection
    private Collection<String> genres;
    @NonNull
    @Column(columnDefinition = "TEXT")
    private String summary;
    @ElementCollection()
    @Column(columnDefinition = "TEXT")
    private Collection<String> reviews;
    private Double rating;
    private double timesListed;
    private double numberOfReviews;
    private double wishlist;
    private double backlogs;
    private double playing;
    private double plays;
}
