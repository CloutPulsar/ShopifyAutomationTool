package com.ShopifyAIO.mainLayout;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class menuController {

    @FXML
    private AnchorPane rootPane;

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
    void handleCloseBtn(ActionEvent event) {

    }

    @FXML
    void handleCloseBtnEntered(MouseEvent event) {

    }

    @FXML
    void handleCloseBtnExited(MouseEvent event) {

    }

    @FXML
    void handleMinimizeBtn(ActionEvent event) {

    }

    @FXML
    void handleMinimizeBtnEntered(MouseEvent event) {

    }

    @FXML
    void handleMinimizeBtnExited(MouseEvent event) {

    }

}
