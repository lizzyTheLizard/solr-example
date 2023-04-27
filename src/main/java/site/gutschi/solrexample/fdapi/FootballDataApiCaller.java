package site.gutschi.solrexample.fdapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import site.gutschi.solrexample.model.Area;
import site.gutschi.solrexample.model.Competition;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FootballDataApiCaller {
    private final RestTemplate restTemplate = new RestTemplate();
    private final static String BASE_URL = "https://api.football-data.org/v4";

    public List<Area> getAreas(){
        final var typeRef = new ParameterizedTypeReference<FootballDataApiResult<Area>>() {};
        final var result = restTemplate
                .exchange(BASE_URL + "/areas", HttpMethod.GET, null, typeRef)
                .getBody();
        if(result == null){
            throw new RuntimeException("No result from Football-Data API");
        }
        return result.getItems();
    }

    public List<Competition> getCompetitions(){
        final var typeRef = new ParameterizedTypeReference<FootballDataApiResult<Competition>>() {};
        final var result = restTemplate
                .exchange(BASE_URL + "/competitions", HttpMethod.GET, null, typeRef)
                .getBody();
        if(result == null){
            throw new RuntimeException("No result from Football-Data API");
        }
        return result.getItems();
    }
}
