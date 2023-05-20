package site.gutschi.solrexample.solr;

import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;
import site.gutschi.solrexample.model.Game;

import java.time.format.DateTimeFormatter;
import java.util.Collection;


@Service
@Slf4j
public class IndexService {
    private final SolrClient solr;

    IndexService(SolrProperties solrProperties) {
        this.solr = new Http2SolrClient.Builder(solrProperties.getUrl()).build();
    }

    public void reindex(Collection<Game> games) {
        try {
            log.debug("Start reindex");
            deleteAllDocuments();
            games.forEach(this::insertDocument);
            log.debug("Commit changes");
            solr.commit();
        } catch (Exception e) {
            rollbackAndFail(e);
        }
    }

    private void deleteAllDocuments() {
        log.debug("Delete all games");
        try {
            solr.deleteByQuery("*.*");
        } catch (Exception e) {
            throw SolrCommunicationException.couldNotDeleteDocuments(e);
        }
    }

    private void insertDocument(Game game) {
        log.debug("Insert game " + game.getTitle());
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", game.getId());
        document.addField("title", game.getTitle());
        if (game.getReleaseDate() != null) {
            final var dateStr = game.getReleaseDate()
                    .format(DateTimeFormatter.ISO_DATE)
                    + "T00:00:00Z";
            document.addField("releaseDate", dateStr);
        }
        game.getTeam().forEach(t -> document.addField("team", t));
        game.getGenres().forEach(g -> document.addField("genre", g));
        document.addField("summary", game.getSummary());
        game.getReviews().forEach(r -> document.addField("review", r));
        if (game.getRating() != null) {
            document.addField("rating", game.getRating());
        }
        document.addField("timesListed", game.getTimesListed());
        document.addField("wishlist", game.getWishlist());
        document.addField("backlogs", game.getBacklogs());
        document.addField("playing", game.getPlaying());
        document.addField("plays", game.getPlays());
        document.addField("numberOfReviews", game.getNumberOfReviews());
        try {
            solr.add(document);
        } catch (Exception e) {
            throw SolrCommunicationException.couldNotAddGame(game, e);
        }
    }

    private void rollbackAndFail(Exception commitException) {
        try {
            solr.rollback();
        } catch (Exception e) {
            log.error("Could not rollback after exception. Rollback-Exception was " + e.getMessage(), e);
        }
        throw SolrCommunicationException.wrap(commitException);
    }
}
