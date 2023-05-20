package site.gutschi.solrexample.solr;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import site.gutschi.solrexample.model.Game;

@Slf4j
public class SolrCommunicationException extends ResponseStatusException {
    private SolrCommunicationException(String message, Exception cause) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message, cause);
    }

    static SolrCommunicationException wrap(Exception cause) {
        if (cause instanceof SolrCommunicationException) {
            return (SolrCommunicationException) cause;
        }
        log.info("General Solr-Exception", cause);
        return new SolrCommunicationException("General Solr-Exception", cause);
    }

    static void cannotSearch(String query, Exception cause) {
        log.debug("Cannot execute query '" + query + "': " + cause.getMessage());
    }

    public static SolrCommunicationException couldNotAddGame(Game game, Exception cause) {
        log.warn("Could not add game " + game.toString() + ": " + cause.getMessage());
        return new SolrCommunicationException("Could not add game", cause);
    }

    public static SolrCommunicationException couldNotDeleteDocuments(Exception cause) {
        log.warn("Could not delete games: " + cause.getMessage());
        return new SolrCommunicationException("Could not delete games", cause);
    }
}
