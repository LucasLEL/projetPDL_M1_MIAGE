package model;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Classe GestionListeCategorie - Interactions avec les listes des catégories 
 * @author PDL_GROUPE7
 *
 */
public class GestionListeCategorie {
	private HashMap<String, String> mapCategories = new HashMap <String, String>();
	private ArrayList<String> listCategoriesName = new ArrayList<String>();

	public void setMapCategories(HashMap<String, String> mapCategories) {
		this.mapCategories = mapCategories;
	}

	public HashMap<String, String> getMapCategories() {
		return mapCategories;
	}
	
	public void cleanListCategorie(){
		this.mapCategories.clear();
		this.listCategoriesName.clear();
	}

	public void setListCategories(String name, String tag) {
		this.mapCategories.put(name, tag);
		this.listCategoriesName.add(name);
	}
	
	public int getSize(){
		return mapCategories.size();
	}
	
	public ArrayList<String> getListCategoriesName() {
		return listCategoriesName;
	}

	public void setListCategoriesName(ArrayList<String> listCategoriesName) {
		this.listCategoriesName = listCategoriesName;
	}
	
	public String getTagFromName(String name){
		return this.mapCategories.get(name);
	}
}
