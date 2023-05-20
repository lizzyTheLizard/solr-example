package site.gutschi.solrexample.solr;

import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class SearchService {
    private final SolrClient solr;

    SearchService(SolrProperties solrProperties) {
        this.solr = new Http2SolrClient.Builder(solrProperties.getUrl()).build();
    }

    public Collection<Integer> search(String input) {
        try {
            final var solrQuery = new SolrQuery();
            solrQuery.set("q", input);
            solrQuery.set("fl", "id");
            solrQuery.setRows(1000);
            final var response = solr.query(solrQuery);
            return response.getResults().stream()
                    .map(s -> (String) s.getFieldValue("id"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            SolrCommunicationException.cannotSearch(input, e);
            return List.of();
        }
    }


    public Collection<AutocompleteResult> autocomplete(String input) {
        input = input.replaceAll("[^\\w*?]", "");
        final var solrQuery = new SolrQuery();
        solrQuery.set("q", "title:" + input + "* team:" + input + "* genre:" + input + "*");
        solrQuery.set("fl", "none");
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("*");
        solrQuery.setRows(1000);
        try {
            return solr.query(solrQuery).getHighlighting().entrySet().stream()
                    .flatMap(e1 -> {
                        final var id = Integer.parseInt(e1.getKey());
                        return e1.getValue().entrySet().stream().flatMap(e2 -> {
                            final var field = e2.getKey();
                            return e2.getValue().stream()
                                    .filter(s -> !s.isBlank())
                                    .map(s -> new AutocompleteResult(field.equals("title") ? id : null, field, s));
                        });
                    })
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            SolrCommunicationException.cannotSearch(input, e);
            return List.of();
        }
    }
}
