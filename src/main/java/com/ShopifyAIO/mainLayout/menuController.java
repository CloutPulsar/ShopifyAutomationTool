package com.ShopifyAIO.mainLayout;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class menuController {

	@FXML
	private AnchorPane taskPane;

	@FXML
	private AnchorPane rootPane;
	@FXML
	private ImageView logoIcon;
	@FXML
	private Pane menuPane;

	@FXML
	private Button userBtn;

	@FXML
	private Button homeBtn;

	@FXML
	private Button securityBtn;

	@FXML
	private Button settingsBtn;

	@FXML
	private Button proxyBtn;

	@FXML
	private TextField monitorDelayField;

	@FXML
	private TextField checkoutDelayField;

	@FXML
	private Button saveBtn;

	@FXML
	private Button checkUpdateBtn;

	@FXML
	private Button startTaskBtn;

	@FXML
	private Button stopAllTaskBtn;

	@FXML
	private Button clearAllTaskBtn;

	@FXML
	private Button EditAllTaskBtn;

	@FXML
	private Button CaptchaBtn;

	@FXML
	private Button closeBtn;

	@FXML
	private Button minimizeBtn;
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

	@FXML
	private AnchorPane taskListPane;
	
    @FXML
    private Pane defaultPane;

	
	private Stage stage;
	private mainModel model = new mainModel();

	private String defaultStyle = null;
	@FXML
	void handleCloseBtn(ActionEvent event) throws UnsupportedEncodingException, UnknownHostException, SocketException
	{
		stage = (Stage) closeBtn.getScene().getWindow();
		model.restoreKey(model.getAuthKey());
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
	@FXML
	void handleCreatTskBtn(ActionEvent event) throws IOException 
	{
		stage = new Stage();
		stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/taskcreator.fxml"))));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setX(rootPane.getScene().getWindow().getX()+(rootPane.getScene().getWindow().getWidth() / 3));
		stage.setY(rootPane.getScene().getWindow().getY()+(rootPane.getScene().getWindow().getHeight() / 5));
		model.appInfoInitialize();
		stage.showAndWait();
	}
	@FXML
	private void onMouseDragged(MouseEvent event)
	{
		stage = (Stage) rootPane.getScene().getWindow();
		stage.setX(event.getScreenX() - model.getxOffset());
		stage.setY(event.getScreenY() - model.getyOffset());
	}

	@FXML
	private void onMousePressed(MouseEvent event)
	{
		model.setxOffset(event.getSceneX());
		model.setyOffset(event.getSceneY());
	}
	@FXML
	void handleAccountList(ActionEvent event) {
		AccountList.setPromptText(AccountList.getValue().toString());
	}

	@FXML
	void handleCreateTskBtnPopUp(ActionEvent event) {
		stage = (Stage)createTaskBtnPopUp.getScene().getWindow();
		for(int i = 0; i <  model.getTaskQuantity(); i++)
		{
			Pane tmp = new Pane();
			tmp.setStyle(value);
			taskListPane.getChildren().add(tmp);
		}
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
		proxiesPopUp.setItems(FXCollections.observableList(model.getAppInfoProfile()));
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
