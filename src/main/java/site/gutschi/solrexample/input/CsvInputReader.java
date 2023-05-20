package site.gutschi.solrexample.input;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import site.gutschi.solrexample.model.Game;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Service
@Slf4j
public class CsvInputReader {
    private static final String CLASSPATH_GAMES_CSV = "classpath:games.csv";

    public Collection<Game> readCsv() {
        try (final var csvReader = getReader()) {
            return StreamSupport.stream(csvReader.spliterator(), false)
                    .map(this::parseGame)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw ParseException.wrap(e);
        }
    }

    private CSVReader getReader() throws IOException {
        final var csvFile = ResourceUtils.getFile(CLASSPATH_GAMES_CSV);
        final var parser = new CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(false)
                .build();
        final var br = Files.newBufferedReader(csvFile.toPath());
        return new CSVReaderBuilder(br)
                .withSkipLines(1)
                .withCSVParser(parser)
                .build();
    }

    private Game parseGame(String[] line) {
        return Game.builder()
                .id(parseInt(line[0]))
                .title(line[1])
                .releaseDate(parseDate(line[2]))
                .team(parseArray(line[3]))
                .rating(parseDouble(line[4]))
                .timesListed(parseDouble(line[5]))
                .numberOfReviews(parseDouble(line[6]))
                .genres(parseArray(line[7]))
                .summary(line[8])
                .reviews(parseArray(line[9]))
                .plays(parseDouble(line[10]))
                .playing(parseDouble(line[11]))
                .backlogs(parseDouble(line[12]))
                .wishlist(parseDouble(line[13]))
                .build();
    }

    private Double parseDouble(String input) {
        if (input == null) {
            return null;
        }
        if (input.isEmpty()) {
            return null;
        }
        input = input.replaceAll("K", "000");
        input = input.replaceAll("\\.", "");
        return Double.parseDouble(input);
    }

    private Integer parseInt(String input) {
        if (input == null) {
            return null;
        }
        if (input.isEmpty()) {
            return null;
        }
        return Integer.parseInt(input);
    }

    private LocalDate parseDate(String input) {
        if (input == null) {
            return null;
        }
        if (input.isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("LLL dd, uuuu", Locale.US);
            return LocalDate.parse(input, formatter);
        } catch (Exception e) {
            log.info("Cannot parse date " + input + ", ignore it");
            return null;
        }
    }

    private Collection<String> parseArray(String input) {
        if (input.isEmpty()) {
            return List.of();
        }
        final var objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        final var type = new TypeReference<List<String>>() {
        };
        try {
            return objectMapper.readValue(input, type);
        } catch (Exception e) {
            throw ParseException.couldNotParseArray(e);
        }
    }
}