package site.gutschi.solrexample.transport;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import site.gutschi.solrexample.model.Competition;
import site.gutschi.solrexample.model.CompetitionRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CompetitionController {
    private final CompetitionRepository competitionRepository;

    @GetMapping("/competitions")
    public List<Competition> getAll(){
        return competitionRepository.findAll();
    }

    @GetMapping("/competitions/{id}")
    public Competition getAll(@PathVariable("id") int id){
        return competitionRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
