package main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import dbpediaobjects.DBOntologiesManager;
import org.json.simple.parser.ParseException;

import serverlink.ChildAndParent;
import serverlink.JSONReader;
import statistics.DBOntologiesStatistics;
import dbpediaobjects.DBOntology;

/**
 * Crawler of the DBPedia ontology classes
 * Also contains a main method to just test the crawler (no comparison)
 * 
 * @author Thomas Herbeth
 * @author Pierre Monnin
 *
 */
public class DBOntologiesCrawler {

    private HashMap<String, DBOntology> dbontologies;

    /**
     * Main method to test the crawler 
     * @param args not used
     * @throws IOException thrown when server is unavailable
     * @throws ParseException thrown when JSON is not valid
     */
    public static void main(String[] args) throws IOException, ParseException {
        System.out.println("== START MAIN DB ONTOLOGIES CRAWLER ==");
        DBOntologiesCrawler crawler = new DBOntologiesCrawler();
        crawler.computeOntologiesHierarchy();
        DBOntologiesStatistics stats = new DBOntologiesStatistics(crawler.dbontologies);
        stats.computeStatistics();
        stats.displayStatistics();
    }

    /**
     * Getter on the parsed ontology classes
     * @return the parsed ontology classes as a manager object
     */
    public DBOntologiesManager getDBOntologiesManager() {
        return new DBOntologiesManager(this.dbontologies);
    }

    /**
     * Method computing the DBPedia ontology classes hierarchy
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @throws ParseException
     */
    public void computeOntologiesHierarchy() throws IOException, ParseException {
    	// Ask for all the ontology classes
        List<ChildAndParent> childrenAndParents = JSONReader.getChildrenAndParents(URLEncoder.encode(
                "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
                + "PREFIX owl:<http://www.w3.org/2002/07/owl#> "
                + "select distinct ?child ?parent where {"
                + "?child rdf:type owl:Class ."
                + "FILTER (REGEX(STR(?child), \"http://dbpedia.org/ontology\", \"i\")) ."
                + "OPTIONAL {"
                + "?child rdfs:subClassOf ?parent . "
                + "FILTER (REGEX(STR(?parent), \"http://dbpedia.org/ontology\", \"i\"))"
                + "}}", "UTF-8"));

        this.dbontologies = new HashMap<>();
        for (ChildAndParent childAndParent : childrenAndParents) {
            String child = childAndParent.getChild().getValue();

            if(this.dbontologies.get(child) == null) {
            	this.dbontologies.put(child, new DBOntology(child));
            }
        }
        
        // Children relationship creation
        for(ChildAndParent childAndParent : childrenAndParents) {
            DBOntology child = this.dbontologies.get(childAndParent.getChild().getValue());
            DBOntology parent = (childAndParent.getParent() == null) ? null : this.dbontologies.get(childAndParent.getParent().getValue());

            if(child != null && parent != null) {
                child.addParent(parent);
                parent.addChild(child);
            }
        }

        childrenAndParents.clear();
    }
}
