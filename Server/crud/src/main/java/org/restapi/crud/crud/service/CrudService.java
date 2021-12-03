package org.restapi.crud.crud.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.restapi.crud.crud.model.*;

public class CrudService {
	// Déclaration de l'objet "Connection JDBC"
	Connection con;
		
	// On crée une méthode centralisée pour écrire le mot de passe facilement en dur
	public static Connection getMySQLConnection() throws SQLException,
	ClassNotFoundException {
    
		// Déclaration des variables pour la connexion SQL
		
		String hostName = "localhost";
		String dbName = "entries";
		String userName = "user";
		String password = "StrongPassword123$*";
		
		return getMySQLConnection(hostName, dbName, userName, password);
	}
	
	// 2021-11-30
	// Params : hostName, dbName, userName, password
	// Description: créer la connection JDBC avec les params entrés
	public static Connection getMySQLConnection(
			String hostName, 
			String dbName,
			String userName, 
			String password) throws SQLException, ClassNotFoundException {
		// Declare the class Driver for MySQL DB
		// This is necessary with Java 5 (or older)
		// Java6 (or newer) automatically find the appropriate driver.
		// If you use Java> 5, then this line is not needed.
		Class.forName("com.mysql.cj.jdbc.Driver");

		// Déclaration d'une variable intitulée "connectionURL"
        // On affecte à cette variable une certaine valeur
        // Il s'agit en réalité d'une simple concaténation
        // On concatène les variables recues en entrée de la méthod
		String connectionURL = "jdbc:mysql://" + hostName + ":3306/" + dbName;

		
		Connection conn = DriverManager.getConnection(connectionURL, userName, password);
		
		return conn;
	}
	
	// 2021-11-30
	// Params : rien
	// Description : Constructeur de la classe
	public CrudService() {
		try {
			// Get a Connection object
			this.con = getMySQLConnection();			
		}
		catch (Exception e) {
			System.out.println("ERROR SERVICE ! ["+e.toString()+"]");
		}
	}
	
	// 2021-11-30
	// Params : String word
	// Description : Cherche dans la base de donnée le mot à rechercher entré en param.
	public CrudModel getEntries(String word) {
		System.out.println("HERE ["+word+"]");
		
		CrudModel result = new CrudModel();
		result.setWord(word);
		
		String select = "select wordtype, definition from entries where word = ?;";
		
		try {
			PreparedStatement ps = con.prepareStatement(select);
			ps.setString(1, word);
			
			ResultSet res = ps.executeQuery();
			System.out.println("request executed");
			if(res.next()){
				result.setType(res.getString(1));
				result.setDefinition(res.getString(2));
				System.out.println(res.getString("wordtype")+" "+res.getString("definition"));
			} else {
				result=null;
			}
			
		}
		catch(Exception e) {
			System.out.println("ERROR SELECT ENTRIES ["+e.toString()+"]");
		}

		return result;
	}
	
}
