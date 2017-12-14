package model;
import java.io.IOException;
import java.util.Properties;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;



/**
 * Classe MongoConnect, qui se charge de la connexion à la base de données MongoDB
 * Cette classe récupère des paramètres de configuration, dans le fichier indiqué via la constante "PATH_FILE_CONFIG"
 * @author PDL_GROUPE7
 */
public class MongoConnect {
		
	    private final String PATH_FILE_CONFIG="/config.properties";
		private Properties prop;
		private String host;
		private String port;
		private String database_name;
		private String username;
		private String password;
		private MongoClient mongoClient;
		private MongoDatabase database;
		
		/**
		 * Constructeur de la classe MongoConnect qui ne prend aucun paramètre
		 */
		public MongoConnect(){
			this.prop = new Properties();
			this.host = "";
			this.port = "";
			this.database_name= "";
			this.username = "";
			this.password = "";
			this.mongoClient = null;
			this.database = null;
		}
		
		/**
		 * Méthode connectToDataBase, qui effectue la connexion à la base de données MongoDB
		 * @return database : Cette méthode renvoie la database "off" de la base MongoDB
		 * @throws IOException : Exception concernant le fichier de configuration 
		 */
		public MongoDatabase connectToDataBase() throws IOException{
			this.loadFileProperties();
			this.mongoClient = new MongoClient(new MongoClientURI("mongodb://" + username + ":" + password + "@" + host + ":" + port + "/" + database_name));
			this.database = mongoClient.getDatabase("off");
			return database;
		}
		
		/**
		 * Méthode loadFileProperties, qui lit le fichier de configuration
		 * Après la lecture de ce fichier, la fonction initialise les attributs de connexion à la Base MongoDB
		 * @throws IOException
		 */
		private void loadFileProperties() throws IOException{
			this.prop.load(MongoConnect.class.getResourceAsStream(PATH_FILE_CONFIG));
			this.host = prop.getProperty("HOST");
			this.port = prop.getProperty("PORT");
			this.database_name = prop.getProperty("DATABASE_NAME");
			this.username = prop.getProperty("USERNAME");
			this.password = prop.getProperty("PASSWORD");	
		}

		public Properties getProp() {
			return prop;
		}

		public void setProp(Properties prop) {
			this.prop = prop;
		}

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public String getPort() {
			return port;
		}

		public void setPort(String port) {
			this.port = port;
		}

		public String getDatabase_name() {
			return database_name;
		}

		public void setDatabase_name(String database_name) {
			this.database_name = database_name;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public MongoClient getMongoClient() {
			return mongoClient;
		}

		public void setMongoClient(MongoClient mongoClient) {
			this.mongoClient = mongoClient;
		}

		public MongoDatabase getDatabase() {
			return database;
		}

		public void setDatabase(MongoDatabase database) {
			this.database = database;
		}
		
}
