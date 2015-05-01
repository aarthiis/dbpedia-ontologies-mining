package dbpediaobjects;

import java.util.ArrayList;

/**
 * DBPedia category
 * 
 * @author Thomas Herbeth
 * @author Pierre Monnin
 *
 */
public class DBCategory {
	private String name = "";
	private String uri = "";
	private ArrayList<String> parents = new ArrayList<String>();
	
	public DBCategory(String name, String uri) {
        super();
        this.name = name;
        this.uri = uri;
    }
	
	public String getName() {
		return name;
	}

	public void addParent(String parent) {
		parents.add(parent);
	}
	
	public void setParents(ArrayList<String> parents) {
		this.parents = parents;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getUri() {
		return uri;
	}
	
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public int getParentsNumber() {
		return parents.size();
	}
	
	@Override
    public String toString() {
        return "DBCategory [name=" + name + ", uri=" + uri + ", parents=" + parents + "]";
    }

    public boolean hasParent(String parent) {
        return parents.contains(parent);
    }
}
