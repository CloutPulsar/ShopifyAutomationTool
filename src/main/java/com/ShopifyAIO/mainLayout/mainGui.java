package com.ShopifyAIO.mainLayout;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
public class mainGui extends Application 
{

	@Override
	public void start(Stage primaryStage) throws IOException 
	{
		
		primaryStage.show();	
	}

	public static void main(String[] args) 
	{
		launch(args);
	}
}
