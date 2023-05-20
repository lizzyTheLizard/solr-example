package site.gutschi.solrexample.input;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Slf4j

public class ParseException extends ResponseStatusException {
    private ParseException(String message, Exception cause) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message , cause);
        log.info(message, cause);
    }

    static ParseException couldNotParseArray(Exception e) {
        return new ParseException("Could not parse input array", e);
    }

    static ParseException wrap(Exception e) {
        if(e instanceof ParseException) {
            return (ParseException) e;
        }
        return new ParseException("Could not parse input", e);
    }
}
