package site.gutschi.solrexample.solr;

import lombok.Value;

@Value
public class AutocompleteResult implements Comparable<AutocompleteResult> {
    Integer id;
    String field;
    String result;

    @Override
    public int compareTo(AutocompleteResult o) {
        if(!this.field.equals(o.field)){
            //First reverse field order, we want title, team, genre
            return o.field.compareTo(this.field);
        }
        return this.result.compareTo(o.result);
    }
}
