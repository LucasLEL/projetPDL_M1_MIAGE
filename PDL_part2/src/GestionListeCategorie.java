import java.util.ArrayList;

public class GestionListeCategorie {
	private ArrayList<String> listCategories = new ArrayList<String>();

	public ArrayList<String> getListCategories() {
		return listCategories;
	}
	
	public void cleanListCategorie(){
		listCategories.clear();
	}

	public void setListCategories(String categorie) {
		categorie = categorie.substring(3, categorie.length());
		this.listCategories.add(categorie);
	}

}
