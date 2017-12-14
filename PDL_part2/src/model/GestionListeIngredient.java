package model;
import java.util.ArrayList;

/**
 * Classe GestionListeIngredient - Interactions avec la liste des ingrédients 
 * @author PDL_GROUPE7
 *
 */
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
