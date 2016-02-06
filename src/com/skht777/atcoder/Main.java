package com.skht777.atcoder;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * @author skht777
 *
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader ld = new FXMLLoader(getClass().getResource("Box.fxml"));
			ld.load();
			((BoxController) ld.getController()).setStage(primaryStage);
			Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
			Scene scene = new Scene(root,436, 640);
			primaryStage.setTitle("AtCoder Code Searcher");
			primaryStage.setResizable(false);
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
