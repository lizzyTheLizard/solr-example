package site.gutschi.solrexample.transport;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.gutschi.solrexample.input.CsvInputReader;
import site.gutschi.solrexample.model.GameRepository;
import site.gutschi.solrexample.solr.IndexService;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/initialize")
public class InitializerController {
    private final IndexService indexService;
    private final GameRepository gameRepository;
    private final CsvInputReader csvInputReader;

    @GetMapping("")
    @Transactional
    public void init() {
        gameRepository.truncate();
        final var games = csvInputReader.readCsv();
        gameRepository.saveAll(games);
        log.info("Imported " + games.size() + " games");
        indexService.reindex(games);
        log.info("Successfully updated index. Added " + games.size() + " games");
    }

    @GetMapping("/db")
    @Transactional
    public void initDB() {
        gameRepository.truncate();
        final var games = csvInputReader.readCsv();
        gameRepository.saveAll(games);
        log.info("Imported " + games.size() + " games");
    }

    @GetMapping("/index")
    public void initIndex() {
        final var games = gameRepository.findAll();
        indexService.reindex(games);
        log.info("Successfully updated index. Added " + games.size() + " games");
    }
}
