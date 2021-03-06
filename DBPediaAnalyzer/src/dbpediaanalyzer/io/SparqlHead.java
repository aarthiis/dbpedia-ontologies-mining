package dbpediaanalyzer.io;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Header of a SPARQL response
 *
 * @author Thomas Herbeth
 * @author Pierre Monnin
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SparqlHead {
    private ArrayList<String> link;
    private ArrayList<String> vars;

    public ArrayList<String> getLink() {
        return this.link;
    }

    public void setLink(ArrayList<String> link) {
        this.link = link;
    }

    public ArrayList<String> getVars() {
        return this.vars;
    }

    public void setVars(ArrayList<String> vars) {
        this.vars = vars;
    }
}
