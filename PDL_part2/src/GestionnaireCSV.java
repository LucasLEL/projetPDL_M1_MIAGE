import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe GestionnaireCSV qui effectue la gestion de l'export des données dans un fichier CSV
 * @author PDL_GROUPE7
 *
 */
public class GestionnaireCSV {
	
	private String separateurCSV;
	private String newLine="\n";

	/**
	 * Constructeur de la classe
	 * @param separateurCSV : paramètre représentant le séparateur du fichier CSV (généralement ";" ou ",")
	 */
	public GestionnaireCSV(String separateurCSV){
		this.separateurCSV = separateurCSV;
	}
	

	/**
	 * Méthode createCSVFile qui créé un FileWriter et le renvoie 
	 * @param nameOfFile : nom du fichier
	 * @return le FileWriter ainsi créé
	 * @throws IOException
	 */
	public FileWriter createCSVFile(String nameOfFile) throws IOException{
		FileWriter fileWriter = new FileWriter(nameOfFile);
		return fileWriter; 
	}
	
	/**
	 * Méthode addHeaders qui ajoute la ligne des headers dans le fichier CSV
	 * @param csvFile : le FileWriter
	 * @param headersList : l'arrayList de headers sous forme de string
	 * @throws IOException
	 */
	public void addHeaders(FileWriter csvFile, ArrayList<String> headersList) throws IOException{
		for (int i = 0; i < headersList.size(); i++) {
			this.addString("\""+headersList.get(i)+"\"", csvFile);
			if(i != headersList.size()-1){
				this.addSeparator(csvFile);
			}
		}
		this.addNewLine(csvFile);
	}
	
	/**
	 * Méthode addDatas qui ajoute les données dans le fichier CSV
	 * @param csvFile : le FileWriter
	 * @param jsonArrayDatas : le JSONArray contenant les données à insérer dans le fichier CSV
	 * @param headersList : l'arrayList de headers sous forme de string
	 * @throws IOException
	 * @throws JSONException
	 */
	public void addDatas(FileWriter csvFile, JSONArray jsonArrayDatas, ArrayList<String> headersList) throws IOException, JSONException{
		
			for (int indexDatas=0; indexDatas < jsonArrayDatas.length(); indexDatas++) {
				JSONObject product = jsonArrayDatas.getJSONObject(indexDatas);
				
				for (int indexHeader = 0; indexHeader < headersList.size(); indexHeader++) {
					
					if(headersList.get(indexHeader).equals("id") || headersList.get(indexHeader).equals("product_name") || headersList.get(indexHeader).equals("brands")){
						Object theInformation = product.get(headersList.get(indexHeader));
						this.addString("\""+theInformation.toString()+"\"", csvFile);
						if(indexHeader != headersList.size()-1){
							this.addSeparator(csvFile);
						}
					}
					
					
				}
				this.addNewLine(csvFile);
			}
	}
	
	private void addString(String data, FileWriter fileWriter) throws IOException{
		fileWriter.append(data);
	}
	
	private void addSeparator(FileWriter csvFile) throws IOException{
		csvFile.append(this.separateurCSV);
	}
	
	private void addNewLine(FileWriter csvFile) throws IOException{
		csvFile.append(this.newLine);
	}
	
	public void closeCSVFile(FileWriter csvFile) throws IOException{
		csvFile.close();
	}

	public String getSeparateurCSV() {
		return separateurCSV;
	}

	public void setSeparateurCSV(String separateurCSV) {
		this.separateurCSV = separateurCSV;
	}
	
}
