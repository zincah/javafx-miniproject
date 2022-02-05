package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dao.ShopInfoDAO;
import dto.ShopInfoDTO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class JoinController implements Initializable{
	
	@FXML private TextField shopNumTxt;
	@FXML private TextField nameTxt;
	@FXML private TextField pwTxt;
	@FXML private TextField rePwTxt;
	@FXML private TextField phoneTxt;
	@FXML private TextField shopTelTxt;
	@FXML private TextArea shopAddressTxt;
    @FXML private Label pwCheckLabel;
	
	@FXML private Button insertBtn;
	@FXML private Button backBtn;
	
	private Stage stage;
	private Scene scene;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		if(pwTxt!=null) {
			rePwTxt.textProperty().addListener(new ChangeListener<String>() {

				@Override
				public void changed(ObservableValue<? extends String> arg0, String oldValue, String newValue) {
					if(pwTxt.getText().equals(newValue)) {
						pwCheckLabel.setText("ok. same password~");
					}else {
						pwCheckLabel.setText("plz enter same password");
					}
				}
			});
		}

	}
	
	public void handleJoin(ActionEvent event) {
		String shop_no = shopNumTxt.getText();
		String owner_name = nameTxt.getText();
		String shop_tel = shopTelTxt.getText();
		String owner_tel = phoneTxt.getText();
		String pw = pwTxt.getText();
		String shop_address = shopAddressTxt.getText();
		
		ShopInfoDTO shop = 
				new ShopInfoDTO(shop_no, owner_name, shop_tel, owner_tel, pw, shop_address);
		
		ShopInfoDAO dao = ShopInfoDAO.getInstance();
		int result = dao.insert(shop);
		
		if (result == 1) {
			System.out.println("가입 성공");
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText(null);
			alert.setContentText("success to join! :)");
			alert.showAndWait();
			
			handleBack(event);
			
		}else {
			System.out.println("데이터 저장 실패");
		}
		
		
	}
	
	public void handleBack(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/scene/login.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setTitle("Login page");
			stage.setScene(scene);
			stage.show();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	

}
