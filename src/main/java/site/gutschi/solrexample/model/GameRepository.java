package site.gutschi.solrexample.model;

import lombok.NonNull;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface GameRepository extends CrudRepository<Game, Integer> {
    @Modifying
    @Query(value = "TRUNCATE TABLE Game CASCADE", nativeQuery = true)
    void truncate();

    @NonNull Collection<Game> findAll();

    @NonNull Collection<Game> findAllById(@NonNull Iterable<Integer> integers);
}
