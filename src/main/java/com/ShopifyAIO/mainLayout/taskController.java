package com.ShopifyAIO.mainLayout;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class taskController {
	@FXML
	private AnchorPane taskPane;

	@FXML
	private ComboBox<Object> sizePopUp;

	@FXML
	private ComboBox<Object> proxiesPopUp;

	@FXML
	private TextField productNamePopUP;

	@FXML
	private TextField siteURLBtnPopUp;

	@FXML
	private ComboBox<Object> profilePopUp;

	@FXML
	private ComboBox<Object> storeBtnPopUp;

	@FXML
	private ComboBox<Object> AccountList;

	@FXML
	private Button createTaskBtnPopUp;

	@FXML
	private ImageView plusBtnPopUp;

	@FXML
	private ImageView minusBtnPopUp;

	@FXML
	private Button cancelBtnPopUp;

	@FXML
	private TextField quantityBtnPopUp;

	private Stage stage;
	private mainModel model = new mainModel();
	private static menuController menu = null;
	private static AnchorPane updatedTaskList;
	private String defaultStyle = null;
	
	public static AnchorPane getMenuControllerUpdated()
	{
		return updatedTaskList;
	}
    @FXML
	void handleAccountList(ActionEvent event) {
		AccountList.setPromptText(AccountList.getValue().toString());
	}

	@FXML
	void handleCreateTskBtnPopUp(ActionEvent event) throws IOException {
		stage = (Stage)createTaskBtnPopUp.getScene().getWindow();
		if(menu == null)
		{
			FXMLLoader load = new FXMLLoader(getClass().getResource("/mainmenu.fxml"));
			AnchorPane main = load.load();
			menu = load.getController();
		}
		double test = menu.getDefaultPane().getHeight();
		for(int i = 1; i <=  model.getTaskQuantity(); i++)
		{
			List<Label> list = new LinkedList<Label>();
			Pane tmp = new Pane();
			list.add(new Label(Integer.toString(model.getTaskCounter()+i)));
			list.add(new Label(storeBtnPopUp.getValue().toString()));
			list.add(new Label(productNamePopUP.getText()));
			list.add(new Label(profilePopUp.getValue().toString()));
			list.add(new Label (proxiesPopUp.getValue().toString()));
			list.add(new Label(sizePopUp.getValue().toString()));
			Label status = new Label("Connecting...");
			Label action = new Label("None");
			status.setTextFill(Color.CYAN);
			status.setFont(Font.font("System",FontWeight.BOLD, 12));
			tmp.setLayoutY((model.getTaskCounter()+i)*52);
			tmp.setPrefHeight(menu.getDefaultPane().getPrefHeight());
			tmp.setPrefWidth(menu.getDefaultPane().getPrefWidth());
			tmp.setStyle(menu.getDefaultPane().getStyle());
			for (int j = 0; j < list.size(); j++)
			{
				if(j == 1 || j == 3 || j==4)
					list.get(j).setFont(Font.font("System", 13));
				else if(j== 0 || j == 2 || j == 5)
					list.get(j).setFont(Font.font("System",FontWeight.BOLD, 12));
				if(j < 6)
					list.get(j).setTextFill(Color.WHITE);
				list.get(j).setLayoutX(menu.getDefaultPane().getChildren().get(j).getLayoutX());
				list.get(j).setLayoutY(menu.getDefaultPane().getChildren().get(j).getLayoutY());
				list.get(j).setCache(true);
				tmp.getChildren().add(list.get(j));
			}
			menu.getTaskListPane().getChildren().add(tmp);
		}
		model.setTaskCounter(model.getTaskQuantity() + model.getTaskCounter());
		updatedTaskList = menu.getTaskListPane();
		stage.close();
		
	}
	@FXML
	void handleStoreBtnPopUpClick(MouseEvent event) {
		storeBtnPopUp.setItems(FXCollections.observableArrayList(model.getAppInfoStore()));
	}
	@FXML
	void handleProxiesPopUpClick(MouseEvent event) {
		proxiesPopUp.setItems(FXCollections.observableList(model.getAppInfoProxies()));
	}
	@FXML
	void handleSizePopUpClick(MouseEvent event) {
		sizePopUp.setItems(FXCollections.observableList(model.getAppInfoSize()));

	}
	@FXML
	void handleprofilePopUpClick(MouseEvent event) {
		profilePopUp.setItems(FXCollections.observableList(model.getAppInfoProfile()));
	}
	@FXML
	void handleAccountListClick(MouseEvent event) {
		AccountList.setItems(FXCollections.observableList(model.getAppInfoAccount()));
	}
	@FXML
	void handleMinusBtnPopUp(MouseEvent event) {
		model.setTaskQuantity((model.getTaskQuantity() <= 1) ? 1 : model.getTaskQuantity() - 1);
		quantityBtnPopUp.setText(Integer.toString(model.getTaskQuantity()));
	}

	@FXML
	void handlePlusBtnPopUp(MouseEvent event) {
		model.setTaskQuantity((model.getTaskQuantity() >= 100000) ? 100000 : model.getTaskQuantity() + 1);
		quantityBtnPopUp.setText(Integer.toString(model.getTaskQuantity()));
	}

	@FXML
	void handleProxiesPopUp(ActionEvent event) {
		proxiesPopUp.setPromptText(proxiesPopUp.getValue().toString());
	}

	@FXML
	void handleQuantityBtn(ActionEvent event) {
		quantityBtnPopUp.setText(quantityBtnPopUp.getText());
		model.setTaskQuantity(Integer.parseInt(quantityBtnPopUp.getText()));
	}

	@FXML
	void handleSizePopUp(ActionEvent event) {
		sizePopUp.setPromptText(sizePopUp.getValue().toString());
	}

	@SuppressWarnings("unchecked")
	@FXML
	void handleStoreBtnPopUp(ActionEvent event) {
		siteURLBtnPopUp.setPromptText(model.getAppInfoUrl().get(model.getAppInfoStore().indexOf(storeBtnPopUp.getValue())).toString());
	}

	@FXML
	void handleproductNamePopUp(ActionEvent event) {
		productNamePopUP.setText(productNamePopUP.getText());
	}

	@FXML
	void handleprofilePopUp(ActionEvent event) {

	}
	@FXML
	void handleCancelPopUp(ActionEvent event) {
		stage = (Stage)cancelBtnPopUp.getScene().getWindow();
		stage.close();
	}
	@FXML
	void handletaskPane(MouseEvent event) {
		defaultStyle = taskPane.getStyle();
		taskPane.setStyle(defaultStyle.replace("-fx-border-color: #FF000000", "-fx-border-color: WHITE"));
	}
	@FXML
	void onMouseExitedTaskPopup(MouseEvent event) {
		if(defaultStyle == null)
			defaultStyle = taskPane.getStyle();
		taskPane.setStyle(defaultStyle.replace("-fx-border-color: WHITE", "-fx-border-color: #FF000000"));
	}
}