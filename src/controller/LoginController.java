package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dao.ShopInfoDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements Initializable{
	
	@FXML TextField idText;
	@FXML PasswordField pwText;
	
	@FXML Button loginBtn;
	@FXML Button joinBtn;
	
	Stage stage;
	Scene scene;
	
	static String myId;
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		joinBtn.setOnAction(event -> handleJoinScene(event));
		loginBtn.setOnAction(event-> handleLogin(event));
		
	}
	
	public void handleLogin(ActionEvent event){
		
		String shop_no = idText.getText();
		String pw = pwText.getText();
		
		if(shop_no.equals("admin") && pw.equals("admin")) {
			adminScene(event);
		}else {
			ShopInfoDAO dao = ShopInfoDAO.getInstance();
			boolean check = dao.checkUser(shop_no, pw);
			
			if(check) {
				mainScene(event);
				myId = shop_no;
			}
		}
	}
	
	public void adminScene(ActionEvent event) {
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/scene/admin.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setTitle("Admin page");
			stage.setScene(scene);
			stage.show();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void handleJoinScene(ActionEvent event) {
		try {
			
			Parent root = FXMLLoader.load(getClass().getResource("/scene/joinForm.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setTitle("Join page");
			stage.setScene(scene);
			stage.show();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void mainScene(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/scene/main.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setTitle("Main page");
			stage.setScene(scene);
			stage.show();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	

}
