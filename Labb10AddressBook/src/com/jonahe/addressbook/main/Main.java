package com.jonahe.addressbook.main;


import com.jonahe.addressbook.app.ControllerAddressBook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

	public static void main(String[] args) {
		// javafx entry point
		launch(args);
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jonahe/addressbook/ui/uiAddressBook.fxml"));
		Parent root = loader.load();
		// root.getStylesheets().add("/com/jonahe/addressbook/ui/uiAddressBook.css");
		Scene scene = new Scene(root);
		
		primaryStage.setScene(scene);
		scene.getStylesheets().clear();
		scene.getStylesheets().add("/com/jonahe/addressbook/ui/uiAddressBook.css");
		primaryStage.setTitle("Address book");
		primaryStage.show();
		ControllerAddressBook controller = loader.getController();
		controller.setOnCloseRequest();
		
		
		
		
	}
	
}
