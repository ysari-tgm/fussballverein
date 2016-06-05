package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Diese Klasse wurde von der Aufgabe Prepared Statement übernommen.
 * 
 * Die CRUD Befehle werden mithilfe von Prepared Statements vorbereitet und in
 * der Main Klasse ausgefuehrt. Tabelle, die modifiziert wird: Person;
 * 
 * 
 * @author Yunus Sari, Eren Sefer
 * @veresion 2016-05-21
 *
 */
public class CRUD {
	private String insert = "INSERT INTO person VALUES (?,?,?)";
	private String update = "UPDATE person SET vorname = ?, nachname = ? WHERE nummer = ?";
	private String read = "SELECT * FROM person WHERE nummer = ?";
	private String delete = "DELETE FROM person WHERE nummer = ?";
	private Connection con;

	/**
	 * Die Verbindung mit der Datenbank wird dieser Klasse uebergeben.
	 * 
	 * @param conn
	 *            Die Verbindung mit der Datenbank (Falls CLIParser erstellt
	 *            ist: getConnection() verwenden)
	 */
	public CRUD(Connection conn) {
		this.con = conn;
		try {
			con.setAutoCommit(true);
		} catch (SQLException e) {
			System.out.println("Fehler bei der Verbindung.");
			e.printStackTrace();
		}
	}

	/**
	 * Fuer die Tabelle Person werden 12000 Inserts erstellt. Fuer die erste
	 * Nummer wird die maximale Nummer der Tabelle herausgefunden. Der Vorname
	 * ist derselbe wie der Nachname. Diese Namen beginnen von "aaaaa" und
	 * werden immer um einen Buchstaben erhoeht. (aaaaa -> aaaab -> aaaac)
	 * 
	 * @param anzahl
	 *            Anzahl, der Datensaetze, die erstellt werden sollen
	 */
	public void create(int anzahl) {
		PreparedStatement ps;
		String name = "";
		char[] alphabet = "abcdefghijklmnopqrstuvwxyz ".toCharArray();
		int[] nameint = { 0, 0, 0, 0, 0 };

		int maxnummer = 0;
		try {
			Statement stmt = con.createStatement();
			ResultSet ts = stmt.executeQuery("SELECT max(nummer) FROM person;");
			while (ts.next()) {
				maxnummer = (ts.getInt(1));
			}

			ps = con.prepareStatement(insert);
			System.out.println("Datensaetze werden erstellt.");

			for (int i = 0; i < anzahl; i++) {
				name = "" + alphabet[nameint[0]] + alphabet[nameint[1]] + alphabet[nameint[2]] + alphabet[nameint[3]]
						+ alphabet[nameint[4]];

				ps.setInt(1, (maxnummer + i + 1));
				ps.setString(2, name);
				ps.setString(3, name);
				ps.executeUpdate();

				nameint[4]++;

				for (int j = nameint.length; j > 0; j--) {
					if (nameint[j - 1] == 24) {
						nameint[j - 1] = 0;
						if (j != 0) {
							nameint[j - 2] = nameint[j - 2] + 1;
						}
					}
				}
			}
			System.out.println("Datensaetze wurden erstellt.");
		} catch (SQLException e) {
			System.out.println("Fehler beim Erstellen.");
			e.printStackTrace();
		}
	}

	/**
	 * Ein Datensatz wird ausgegeben.
	 * 
	 * @param nummer
	 *            Nummer des Datensatzes, der ausgelesen werden soll.
	 */
	public void read(int nummer) {
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(read);
			ps.setInt(1, nummer);
			ResultSet tmp = ps.executeQuery();
			while (tmp.next()) {
				System.out.print("Nummer: " + tmp.getInt(1));
				System.out.print(", Vorname: " + tmp.getString(2));
				System.out.print(", Nachname: " + tmp.getString(3) + "\n");
			}
			ps.close();
		} catch (SQLException e) {
			System.out.println("Fehler beim Lesevorgang.");
			e.printStackTrace();
		}
	}

	/**
	 * Ein bestimmter Datensatz der Tabelle Person wird geaendert. Der Datensatz
	 * wird mit der uebergebenen Nummer bestimmt und die zu aendernden Vornamen
	 * und Nachname werden genauso wie die Nummer durch Parameter angegeben.
	 * 
	 * @param vorname
	 *            Der Vorname der Person
	 * @param nachname
	 *            Der Nachname der Person
	 * @param nummer
	 *            Die Nummer, die geaendert werden soll
	 */
	public void update(String vorname, String nachname, int nummer) {
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(update);
			ps.setString(1, vorname);
			ps.setString(2, nachname);
			ps.setInt(3, nummer);
			ps.executeUpdate();
			read(2);
			ps.close();
		} catch (SQLException e) {
			System.out.println("Fehler beim Update.");
			e.printStackTrace();
		}
	}

	/**
	 * Loescht eine Row in der Tabelle Person.
	 * 
	 * @param Nummer
	 *            des Datensatzes
	 */
	public void delete(int nummer) {
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(delete);
			ps.setInt(1, nummer);
			ps.executeUpdate();
			System.out.println("Datensatz wurde Erfolgreich geloescht.");
		} catch (SQLException e) {
			System.out.println("Fehler beim Loeschen.");
			e.printStackTrace();
		}
	}
}
