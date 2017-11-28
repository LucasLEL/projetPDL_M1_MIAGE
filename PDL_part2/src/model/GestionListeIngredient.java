package model;
import java.util.ArrayList;

public class GestionListeIngredient {
	private ArrayList<String> listIngredients = new ArrayList<String>();
	
	public void cleanListIngredients(){
		listIngredients.clear();
	}

	public void setListIngredients(String ingredient) {
		this.listIngredients.add(ingredient);
	}
	
	public int getSize(){
		return listIngredients.size();
	}
	
	public String getElement(int i){
		return listIngredients.get(i);
	}
}
