import java.util.ArrayList;

public class GestionListeCategorie {
	private ArrayList<String> listCategories = new ArrayList<String>();
	//private String nomAvantSub = ""; 
	
//	public String getNomAvantSub(){
//		return nomAvantSub;
//	}

	public ArrayList<String> getListCategories() {
		return listCategories;
	}
	
	public void cleanListCategorie(){
		listCategories.clear();
	}

	public void setListCategories(String categorie) {
		//FAUX : 
		//this.nomAvantSub = categorie;
		//categorie = categorie.substring(3, categorie.length());
		this.listCategories.add(categorie);
	}
	
	public int getSize(){
		return listCategories.size();
	}

}
