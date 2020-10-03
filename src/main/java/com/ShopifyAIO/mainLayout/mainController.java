package com.ShopifyAIO.mainLayout;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import org.apache.http.client.ClientProtocolException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class mainController
{

	// define your offsets here
	private double xOffset = 0;
	private double yOffset = 0;
	private Stage stage;
	private mainModel model = new mainModel();
	private static StringBuilder authKey = new StringBuilder();

	@FXML
	private TextField authField;

	@FXML
	private ImageView logoIcon;

	@FXML
	private Button acitvateBtn;

	@FXML
	private Button closeBtn;

	@FXML
	private Button minimizeBtn;

	@FXML
	private Label responseLabel;

	@FXML
	private void onauthFieldClick(MouseEvent event)
	{
		responseLabel.setVisible(false);
	}
	@FXML
	private void onMouseDragged(MouseEvent event)
	{
		stage = (Stage) logoIcon.getScene().getWindow();
		stage.setX(event.getScreenX() - xOffset);
		stage.setY(event.getScreenY() - yOffset);
	}

	@FXML
	private void onMousePressed(MouseEvent event)
	{
		xOffset = event.getSceneX();
		yOffset = event.getSceneY();
	}

	@FXML
	private void handleActivateButton(ActionEvent event) throws ClientProtocolException, IOException, InterruptedException, ClassNotFoundException
	{
		responseLabel.setVisible(true);
		if (authField.getText().isEmpty())
		{
			responseLabel.setText("Invalid Key! Please Try Again");
			responseLabel.setTextAlignment(TextAlignment.CENTER);
			responseLabel.setTextFill(Color.web("#fc0303"));
		} else
		{
			responseLabel.setText("Connecting to database...");
			responseLabel.setTextFill(Color.web("#03fcf4"));
			authKey.append(authField.getText());
			if(model.sendandrecvAuth(authKey.toString()) == true)
			{
				responseLabel.setText("Authentication Successful!");
				responseLabel.setTextFill(Color.web("#03fc03"));
				Thread.sleep(1500);
				Stage stage = (Stage) responseLabel.getScene().getWindow();
				stage.setScene(model.getNextWindow());
				
			}else
			{
				responseLabel.setText("Authentication Unsuccessful!");
				responseLabel.setTextFill(Color.web("#fc6703"));
				authKey.setLength(0);
			}
		}
	}

	@FXML
	void handleCloseBtn(ActionEvent event) throws UnsupportedEncodingException, UnknownHostException, SocketException
	{
		stage = (Stage) closeBtn.getScene().getWindow();
		model.restoreKey(authKey.toString());
		stage.close();
	}

	@FXML
    void handleMinimizeBtn(ActionEvent event)
	{
		stage = (Stage) minimizeBtn.getScene().getWindow();
		stage.setIconified(true);
	}
	@FXML
	void handleCloseBtnEntered(MouseEvent event)
	{
		closeBtn.setStyle("-fx-background-color: #ff6363; -fx-background-radius: 100; ");
	}

	@FXML
	void handleCloseBtnExited(MouseEvent event)
	{
		closeBtn.setStyle("-fx-background-color:transparent;");
	}
	@FXML
	void handleMinimizeBtnEntered(MouseEvent event)
	{
		minimizeBtn.setStyle("-fx-background-color: #ffdb63; -fx-background-radius: 100;");
	}

	@FXML
	void handleMinimizeBtnExited(MouseEvent event)
	{
		minimizeBtn.setStyle("-fx-background-color:transparent;");
	}

}
