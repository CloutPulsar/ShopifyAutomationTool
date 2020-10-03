package com.ShopifyAIO.mainLayout;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class mainView extends Application
{

	@Override
	public void start(Stage primaryStage) throws IOException
	{
		Parent login = FXMLLoader.load(getClass().getResource("/mainFXML.fxml"));
		primaryStage.setScene(new Scene(login));
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.show();
	}
	
	public static void main(String[] args) 
	{
		launch(args);
	}
}
