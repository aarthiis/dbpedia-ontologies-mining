package statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import dbpediaobjects.DBOntology;

/**
 * Class computing statistics over DB Ontology classes hierarchy
 * 
 * @author Pierre Monnin
 *
 */
public class DBOntologiesStatistics {
	private HashMap<String, DBOntology> ontologies;
	private int ontologiesNumber;
	private int orphansNumber;
	private int depth;
	private int directSubsumptions;
	private int inferredSubsumptions;
	
	public DBOntologiesStatistics(HashMap<String, DBOntology> ontologies) {
		this.ontologies = ontologies;
		this.ontologiesNumber = 0;
		this.orphansNumber = 0;
		this.depth = 0;
		this.directSubsumptions = 0;
		this.inferredSubsumptions = 0;
	}
	
	public void computeStatistics() {
		// Ontologies number
		this.ontologiesNumber = this.ontologies.size();
		
		// Orphans number, direct subsumptions number
		ArrayList<String> orphans = new ArrayList<String>();
		for(String key : this.ontologies.keySet()) {
			// Orphans
			if(this.ontologies.get(key).getParentsNumber() == 0) {
				this.orphansNumber++;
				orphans.add(key);
			}
		
			// Direct subsumptions
			this.directSubsumptions += this.ontologies.get(key).getParentsNumber();
		}
		
		// Depth
		computeDepth(orphans);
		
		// Inferred subsumptions
		computeInferredSubsumptions();
	}
	
	public void displayStatistics() {
		System.out.println("== DB Pedia Ontology classes statistics ==");
		System.out.println("Classes number: " + this.ontologiesNumber);
		System.out.println("Direct subsumptions number: " + this.directSubsumptions);
		System.out.println("Inferred subsumptions number: " + this.inferredSubsumptions);
		System.out.println("Orphans: " + this.orphansNumber);
		System.out.println("Depth: " + this.depth);
	}
	
	private void computeDepth(ArrayList<String> orphans) {
		LinkedList<String> queue = new LinkedList<String>();
		this.depth = 1;
		
		// Algorithm initialization
		for(String ontology : this.ontologies.keySet()) {
			this.ontologies.get(ontology).setDepth(-1);
		}
		
		for(String orphan : orphans) {
			this.ontologies.get(orphan).setDepth(1);
			queue.add(orphan);
		}
		
		// Algorithm computation
		while(!queue.isEmpty()) {
			String onto = queue.pollFirst();
			DBOntology dbOnto = this.ontologies.get(onto);
			
			if(dbOnto != null) {
				for(String child : dbOnto.getChildren()) {
					DBOntology childClass = this.ontologies.get(child);
					
					if(childClass != null && childClass.getDepth() == -1) {
						childClass.setDepth(dbOnto.getDepth() + 1);
						queue.add(child);
					}
				}
				
				if(dbOnto.getDepth() > this.depth) {
					this.depth = dbOnto.getDepth();
				}
				
			}
		}
	}
	
	private void computeInferredSubsumptions() {
		for(String key : this.ontologies.keySet()) {
			
			// Algorithm initialization
			for(String onto : this.ontologies.keySet()) {
				this.ontologies.get(onto).setSeen(false);
			}
			
			LinkedList<String> queue = new LinkedList<String>();
			this.ontologies.get(key).setSeen(true);
			for(String parent : this.ontologies.get(key).getParents()) {
				DBOntology onto = this.ontologies.get(parent);
				
				if(onto != null) {
					onto.setSeen(true);
					queue.add(parent);
				}
			}
			
			// Algorithm computation
			while(!queue.isEmpty()) {
				String onto = queue.pollFirst();
				DBOntology dbOnto = this.ontologies.get(onto);
				
				if(dbOnto != null) {
					for(String parent : dbOnto.getParents()) {
						DBOntology dbParent = this.ontologies.get(parent);
						
						if(dbParent != null && !dbParent.getSeen()) {
							dbParent.setSeen(true);
							queue.add(parent);
							this.inferredSubsumptions++;
						}
					}
				}
			}
		}
	}
}