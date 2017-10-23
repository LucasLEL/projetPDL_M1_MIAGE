import java.util.ArrayList;

public class GestionListeProduit {
	private ArrayList<String> listProduit = new ArrayList<String>();

	public ArrayList<String> getListProduit() {
		return listProduit;
	}
	
	public void cleanListProduit(){
		listProduit.clear();
	}

	public void setListProduit(String produit) {
//		produit = produit.substring(3, produit.length());
		this.listProduit.add(produit);
	}
	
	public int getSize(){
		return listProduit.size();
	}
	
	public String getElement(int i){
		return listProduit.get(i);
	}

	public void cleanListCategorie() {
		listProduit.clear();		
	}

}
