package com.ShopifyAIO.mainLayout;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
public class mainGui extends Application 
{

	@Override
	public void start(Stage primaryStage) throws IOException 
	{
		primaryStage.setTitle("TEST");
		primaryStage.show();	
	}

	public static void main(String[] args) 
	{
		launch(args);
	}
}
