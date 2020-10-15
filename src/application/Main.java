package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	protected static AccountDatabase db = new AccountDatabase();
	@Override
	public void start(Stage primaryStage) {
		try {
			//AccountDatabase db = new AccountDatabase();
			BorderPane root = FXMLLoader.load(getClass().getResource("Home.fxml"));
			Scene scene = new Scene(root, 450, 500);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Transaction Manager");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
