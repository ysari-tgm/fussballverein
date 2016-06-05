package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InsertThread implements Runnable {
	private Connection con;

	public InsertThread(Connection conn) {
		this.con = conn;
		try {
			con.setAutoCommit(true);
		} catch (SQLException e) {
			System.out.println("Fehler bei der Verbindung.");
			e.printStackTrace();
		}
	}

	@Override
	public void run() {

		String insert = "INSERT INTO person VALUES (?,?,?)";
		int anzahl = 10000;
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

}
