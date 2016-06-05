package application;

import java.sql.*;

/**
 * Diese Klasse wurde von der Aufgabe JDBC übernommen.
 * 
 * Diese Klasse dient zur Schnittstelle zu einer (externen) Datenbank. Dient dazu
 * eine Verbindung zu einer Datenbank zu ermoeglichen.
 * 
 * @author Eren Sefer, Yunus Sari
 * @version 2016-03-31
 */
public class DBConnector {
	private Connection c;

	/**
	 * Es wird eine Verbindung mit der Datenbank erstellt
	 * 
	 * @param host
	 *            Hostname (IP-Adresse)
	 * @param port
	 *            Portadresse des Hosts
	 * @param db
	 *            Name der Datenbank
	 * @param user
	 *            Name des Benutzers
	 * @param pass
	 *            Passwort des Benutzers
	 */
	public DBConnector(String host, int port, String db, String user, String pass) {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Fehler beim Laden vom PostgreSQL Driver.");
			e.printStackTrace();
		}
		try {
			c = DriverManager.getConnection("jdbc:postgresql://" + host + ":" + port + "/" + db, user, pass);
			c.setAutoCommit(false);

			System.out.println("Verbindung erfolgreich!");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Die Verbindung zur Datenbank wird sicher geschlossen.
	 */
	public void closeConnection() {
		try {
			c.close();
			System.out.println("Verbindung sicher geschlossen!");
		} catch (Exception e) {
			System.err.println("Verbindung gescheitert");
			e.printStackTrace();
		}
	}

	/**
	 * Die Verbindung mit der Datenbank wird uebergeben.
	 * 
	 * @return Verbindung mit der Datenbank
	 */
	public Connection getConnection() {
		return c;
	}
}