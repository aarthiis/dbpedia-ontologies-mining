package serverlink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dbpediaobjects.DBCategory;

public class JsonParser {

	String stringToParse;
	
	public JsonParser(String stringToParse)
	{
		this.stringToParse = stringToParse;
	}
	
	public ArrayList<String> getResults(String nameRequested) throws ParseException
	{
		// We get the JSON parsed
		JSONParser parser = new JSONParser();
		Map map = (Map)parser.parse(stringToParse);
		// We get the results
		map = (Map) map.get("results");
		JSONArray array = (JSONArray) map.get("bindings");
		
		ArrayList<String> returnArray = new ArrayList<String>();
		
		// For each result
		int i = 0;
		for (i=0 ; i<array.size() ; i++)
		{
			// We get the value of the link
			map = (Map) array.get(i);
			map = (Map) map.get(nameRequested);
			String str = (String) map.get("value");
			
			returnArray.add(str);
		}
		return returnArray;
	}
	
	public HashMap<String, DBCategory> getDbPediaCategories()
	{
		return null;
	}
	
	public String makeRequestAtt(String link)
	{

		// With this value, we can make a new request
		// The request is :
		// SELECT DISTINCT ?att WHERE { <str> ?att ?other }
		String request = "SELECT DISTINCT ?att WHERE { <"+link+"> ?att ?other }";
		
		return request;
	}
	
	public void setStringToParse(String s)
	{
		this.stringToParse = s;
	}
	
}