package site.gutschi.solrexample.fdapi;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import java.util.List;

@Data
public class FootballDataApiResult<T> {
    private int count;
    @JsonAlias({"areas", "competitions"})
    private List<T> items;
}
