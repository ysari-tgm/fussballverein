package application;
	
import java.sql.Connection;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			final DBConnector conn = new DBConnector("192.168.15.134",5432,"schokofabrik","postgres","postgres");
			
			final CRUD crud = new CRUD(conn.getConnection());
			
			Label readt = new Label("Read liest die Person mit der ID 1 aus.");
			Label insertt = new Label("Insert generiert 10 Datensätze und fügt diese in die Datenbank hinzu.");
			Label updatet = new Label("Update ändert den Vornamen und Nachnamen von der Person mit der ID 2.");
			Label deletet = new Label("Delete löscht den Datensatz mit der ID 2.");
			Label insert2t = new Label("Insert genertiert 10000 Datensätze, die in einem Thread abgearbeitet werden.");
			
			Button readb = new Button("Read");
			Button insertb = new Button("Insert");
			Button updateb = new Button("Update");
			Button deleteb = new Button("Delete");
			Button insert2b = new Button("Insert with Thread");
			
			readb.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent arg0) {
					crud.read(1);
				}
			});
			
			insertb.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent arg0) {
					crud.create(1000);
				}
			});
			
			updateb.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent arg0) {
					crud.update("Yunus", "Sari", 2);
				}
			});
			
			deleteb.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent arg0) {
					crud.delete(2);
				}
			});
			
			insert2b.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent arg0) {
					(new Thread(new InsertThread(conn.getConnection()))).start();
				}
			});
			
			VBox root = new VBox();
			root.getChildren().addAll(readt, insertt, updatet, deletet, insert2t);
			root.getChildren().addAll(readb, insertb, updateb, deleteb, insert2b);
			
			Scene scene = new Scene(root,450,250);
			
			/*
			cli.closeConnection();
			*/
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
