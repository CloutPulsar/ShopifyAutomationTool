package com.ShopifyAIO.mainLayout;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class mainController
{

	// define your offsets here
	private double xOffset = 0;
	private double yOffset = 0;
	private Stage stage;

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
	private void handleActivateButton(ActionEvent event)
	{
		if (authField.getText().isEmpty())
		{
			responseLabel.setText("Invalid Key! Please Try Again");
			responseLabel.setTextAlignment(TextAlignment.CENTER);
			responseLabel.setTextFill(Color.web("#fc0303"));
			responseLabel.setVisible(true);
		} else
		{
			responseLabel.setVisible(false);
			System.out.println("Success");
		}
	}

	@FXML
	void handleCloseBtn(ActionEvent event)
	{
		stage = (Stage) closeBtn.getScene().getWindow();
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
