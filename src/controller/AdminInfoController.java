package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dao.ShopInfoDAO;
import dto.ShopInfoDTO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.ShopTable;

public class AdminInfoController implements Initializable{
	
	Stage stage;
	Scene scene;
	
    @FXML private Button stockBtn;
    @FXML private Button inputBtn;
    @FXML private Button salesBtn;

    @FXML private TableView<ShopTable> tableView;

    @FXML private TextField shopQuantityTxt;
    @FXML private TextField shopNumTxt;
    @FXML private TextField nameTxt;
    @FXML private TextField shopTelTxt;
    @FXML private TextField ownerTelTxt;
    @FXML private TextArea shopAddressTxt;
    
    @FXML private Button modifyBtn;
    @FXML private Button deleteBtn;
    
    private ArrayList<ShopInfoDTO> shopList;
    private ObservableList<ShopTable> tableList;
    
    public AdminInfoController() {
		shopList = new ArrayList<>();
		tableList = FXCollections.observableArrayList();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		stockBtn.setOnAction(event -> handleStockScene(event));
		inputBtn.setOnAction(event -> handleInputScene(event));
		salesBtn.setOnAction(event -> handleSalesScene(event));
		
		makeTable();
		showShopInfo();
		
		modifyBtn.setOnAction(event -> handleUpdateInfo(event));
		//deleteBtn.setOnAction(event -> handleDeleteInfo(event));
		
		
	}
	
	public void handleDeleteInfo(ActionEvent event) {
		String shop_no = shopNumTxt.getText();
		
	}
	
	public void handleUpdateInfo(ActionEvent event) {
		
		String shop_no = shopNumTxt.getText();
		String name = nameTxt.getText();
		String shop_tel = shopTelTxt.getText();
		String owner_tel = ownerTelTxt.getText();
		String address = shopAddressTxt.getText();
		
		ShopInfoDTO dto = new ShopInfoDTO(shop_no, name, shop_tel, owner_tel, address);
		ShopInfoDAO dao = ShopInfoDAO.getInstance();
		int result = dao.update(dto);
		
		if(result == 1) {
			System.out.println("정보 수정 성공 다이얼로그 띄우기");
			makeTable();
		}
	}
	
	public void showShopInfo() {
		tableView.getSelectionModel().selectedItemProperty().addListener(
			new ChangeListener<ShopTable>() {
				
				@Override
				public void changed(ObservableValue<? extends ShopTable> arg0, ShopTable oldValue, ShopTable newValue) {
					
					if(tableView.getSelectionModel().getSelectedIndex() > -1) {
						int su = tableView.getSelectionModel().getSelectedIndex();
						ShopTable shop = tableList.get(su);
						shopNumTxt.setText(shop.getShopNumCol());
						shopNumTxt.setDisable(true);
						nameTxt.setText(shop.getNameCol());
						shopTelTxt.setText(shop.getShopTelCol());
						ownerTelTxt.setText(shop.getOwnerTelCol());
						shopAddressTxt.setText(shop.getAddressCol());
					}
					
				}
			});
	}
	
	
	public void makeTable() {
		
		ShopInfoDAO dao = ShopInfoDAO.getInstance();
		shopList = dao.shopInfoList();
		
		tableList.clear();
		
		for(int i=0; i<shopList.size(); i++) {
			ShopInfoDTO dto = shopList.get(i);
			String shop_no= dto.getShop_no();
			String name = dto.getOwner_name();
			String shop_tel = dto.getShop_tel();
			String owner_tel = dto.getOwner_tel();
			String pw = dto.getPw();
			String address = dto.getShop_address();
			
			ShopTable st = new ShopTable(shop_no, name, shop_tel, owner_tel, pw, address);
			tableList.add(st);
		}
		
		TableColumn tcShopNum = tableView.getColumns().get(0);
		tcShopNum.setCellValueFactory(new PropertyValueFactory<>("shopNumCol"));
		tcShopNum.setStyle("-fx-alignment: CENTER;");
		
		TableColumn tcOwnerName = tableView.getColumns().get(1);
		tcOwnerName.setCellValueFactory(new PropertyValueFactory<>("nameCol"));
		tcOwnerName.setStyle("-fx-alignment: CENTER;");
		
		TableColumn tcPw = tableView.getColumns().get(2);
		tcPw.setCellValueFactory(new PropertyValueFactory<>("pwCol"));
		tcPw.setStyle("-fx-alignment: CENTER;");
		
		tableView.setItems(tableList);
		shopQuantityTxt.setText(String.valueOf(tableList.size()));
		
	}
	
	
	
	public void handleStockScene(ActionEvent event) {

		try {
			Parent root = FXMLLoader.load(getClass().getResource("/scene/admin.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setTitle("AdminStock page");
			stage.setScene(scene);
			stage.show();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void handleInputScene(ActionEvent event) {
		
		
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/scene/adminInput.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setTitle("AdminInput page");
			stage.setScene(scene);
			stage.show();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void handleSalesScene(ActionEvent event) {
		
		
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/scene/adminCalc.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setTitle("AdminSales page");
			stage.setScene(scene);
			stage.show();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

}
