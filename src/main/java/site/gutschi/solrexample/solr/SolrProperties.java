package site.gutschi.solrexample.solr;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "site.gutschi.solrexample.solr")
@Data
public class SolrProperties {
    private String url;
}
