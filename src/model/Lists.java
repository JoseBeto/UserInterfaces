package model;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Properties;
import java.util.Map.Entry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Lists {
	
	private HashMap<String, HashMap<Integer, Integer>> lists = new HashMap<String, HashMap<Integer, Integer>>();
	
	public Lists(String lists) {
		setLists(lists);
	}
	
	public HashMap<String, HashMap<Integer, Integer>> getLists() {
		return lists;
	}
	
	public void setLists(String lists) {
		Properties props = new Properties();
		try {
			props.load(new StringReader(lists.substring(1, lists.length() - 1).replace("},", "}\n")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		for(Entry<Object, Object> e : props.entrySet()) {
			
			HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
			String list = (String) e.getValue();
			Properties props2 = new Properties();
			
			try {
				props2.load(new StringReader(list.substring(1, list.length() - 1).replace(",", "\n")));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			for(Entry<Object, Object> e2 : props2.entrySet()) {
			    map.put(Integer.valueOf((String) e2.getKey()), Integer.valueOf((String) e2.getValue()));
			}
			
			this.lists.put((String) e.getKey(), map);
		}
	}
	
	public ObservableList<String> getListNames() {
		ObservableList<String> listNames = FXCollections.observableArrayList();
		
		for(Entry<String, HashMap<Integer, Integer>> e : getLists().entrySet()) {
			listNames.add(e.getKey().replaceAll("_", " "));
		}
		
		return listNames;
	}
	
	public HashMap<Integer, Integer> getListWithName(String listName) {
		listName = listName.replaceAll(" ", "_");
		
		return lists.get(listName);
	}
	
	public void removeItemFromList(String listName, int itemId) {
		listName = listName.replaceAll(" ", "_");
		
		HashMap<Integer, Integer> map = lists.get(listName);
		map.remove(itemId);
	}
	
	public void addItemToList(String listName, int itemId) {
		listName = listName.replaceAll(" ", "_");
		
		HashMap<Integer, Integer> map = lists.get(listName);
		map.put(itemId, 1);
	}
	
	public void removeList(String listName) {
		listName = listName.replaceAll(" ", "_");
		
		lists.remove(listName);
	}
	
	public void createList(String listName) {
		listName = listName.replaceAll(" ", "_");
		
		lists.put(listName, new HashMap<Integer, Integer>());
	}
}
