package site.gutschi.solrexample.transport;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import site.gutschi.solrexample.fdapi.FootballDataApiCaller;
import site.gutschi.solrexample.model.AreaRepository;
import site.gutschi.solrexample.model.CompetitionRepository;


@RestController
@RequiredArgsConstructor
@Slf4j
public class InitializerController {
    private final CompetitionRepository competitionRepository;
    private final AreaRepository areaRepository;
    private final FootballDataApiCaller footballDataApiCaller;

    @GetMapping("/initDB")
    public void initDB(){
        competitionRepository.deleteAll();
        areaRepository.deleteAll();
        final var areas = footballDataApiCaller.getAreas();
        areaRepository.saveAll(areas);
        final var competitions = footballDataApiCaller.getCompetitions();
        competitionRepository.saveAll(competitions);
        log.info("Imported " + areas.size() + " areas and " + competitions.size() + " competitions");
    }

    @GetMapping("/initIndex")
    public void initIndex(){
        //TODO
    }
}
