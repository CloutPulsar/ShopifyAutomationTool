package com.ShopifyAIO.mainLayout;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.UnknownHostException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class menuController {


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
	private ScrollPane scrollPane;

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
	private AnchorPane taskListPane;
	@FXML
	private Pane defaultPane;

	private Stage stage;
	private mainModel model = new mainModel();

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
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/taskcreator.fxml"));
		stage.initOwner(rootPane.getScene().getWindow());
		stage.setScene(new Scene((Parent)loader.load()));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setX(rootPane.getScene().getWindow().getX()+(rootPane.getScene().getWindow().getWidth() / 3));
		stage.setY(rootPane.getScene().getWindow().getY()+(rootPane.getScene().getWindow().getHeight() / 5));
		stage.showAndWait();
		taskListPane.getChildren().setAll(taskController.getMenuControllerUpdated());
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

	public Pane getDefaultPane()
	{
		return defaultPane;
	}

	public void setDefaultPane(Pane defaultPane)
	{
		this.defaultPane = defaultPane;
	}

	public AnchorPane getTaskListPane()
	{
		return taskListPane;
	}

	public void setTaskListPane(AnchorPane taskListPane)
	{
		this.taskListPane = taskListPane;
	}



}
