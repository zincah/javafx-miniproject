package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dao.OrderDAO;
import dto.OrderDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.OrderTable;

public class AdminCalcController implements Initializable{
	
	Stage stage;
	Scene scene;
	
    @FXML private Button stockBtn;
    @FXML private Button inputBtn;
    @FXML private Button infoBtn;

    @FXML private DatePicker firstDayPick;
    @FXML private DatePicker lastDayPick;
    @FXML private TableView<OrderTable> salesTableView;
    @FXML private TextField totalSalesTxt;
    
    private String salesFDay;
    private String salesLDay;
    private ArrayList<OrderDTO> salesList;
    private ObservableList<OrderTable> salesTableList;
    
    @FXML private TextField searchItemTxt;
    @FXML private DatePicker fDayPick;
    @FXML private DatePicker lDayPick;
    @FXML private TableView<OrderTable> itemTableView;
    @FXML private TextField quantityTxt;
    @FXML private TextField priceTxt;
    
    private String item;
    private String itemFDay;
    private String itemLDay;
    private ArrayList<OrderDTO> salesItemList;
    private ObservableList<OrderTable> salesItemTableList;
    
    public AdminCalcController() {
		salesList = new ArrayList<>();
		salesTableList = FXCollections.observableArrayList();
		
		salesItemList = new ArrayList<>();
		salesItemTableList = FXCollections.observableArrayList();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		stockBtn.setOnAction(event -> handleStockScene(event));
		inputBtn.setOnAction(event -> handleInputScene(event));
		infoBtn.setOnAction(event -> handleInfoScene(event));
		
		lastDayPick.setOnAction(event -> salesOfDatePick(event));
		lDayPick.setOnAction(event -> itemSaleDatePick(event));
	}
	
	public void itemSaleDatePick(ActionEvent event) {
		item = searchItemTxt.getText().toUpperCase();
		itemFDay = fDayPick.getValue().toString();
		itemLDay = lDayPick.getValue().toString();
		
		makeItemSaleTable();
	}
	
	public void makeItemSaleTable() {
		OrderDAO dao = OrderDAO.getInstance();
		salesItemList = dao.getItemSales(item, itemFDay, itemLDay);
		salesItemTableList.clear();
		
		int totalQuantity = 0;
		int totalPrice = 0;
		
		for(int i=0; i<salesItemList.size(); i++) {
			OrderDTO dto = salesItemList.get(i);
			String shop_no = dto.getShop_no();
			int quantity = dto.getQuantity();
			int price = dto.getPrice();
			
			totalQuantity += quantity;
			totalPrice += price;
			
			OrderTable ot = new OrderTable(shop_no, String.valueOf(quantity), String.valueOf(price));
			salesItemTableList.add(ot);
		}
		
		TableColumn tcShopNum = itemTableView.getColumns().get(0);
		tcShopNum.setCellValueFactory(new PropertyValueFactory("orderNumCol"));
		tcShopNum.setStyle("-fx-alignment: CENTER;");
		
		TableColumn tcQuantity = itemTableView.getColumns().get(1);
		tcQuantity.setCellValueFactory(new PropertyValueFactory("quantityCol"));
		tcQuantity.setStyle("-fx-alignment: CENTER;");
		
		TableColumn tcPrice = itemTableView.getColumns().get(2);
		tcPrice.setCellValueFactory(new PropertyValueFactory("priceCol"));
		tcPrice.setStyle("-fx-alignment: CENTER;");
		
		itemTableView.setItems(salesItemTableList);
		quantityTxt.setText(String.valueOf(totalQuantity));
		priceTxt.setText(String.valueOf(totalPrice));

	}
	
	public void salesOfDatePick(ActionEvent event) {
		salesFDay = firstDayPick.getValue().toString();
		salesLDay = lastDayPick.getValue().toString();
		
		makeSalesTable();
	}
	
	public void makeSalesTable(){
		
		OrderDAO dao = OrderDAO.getInstance();
		salesList = dao.getSales(salesFDay, salesLDay);
		salesTableList.clear();
		
		int totalPrice = 0;
		
		for(int i=0; i<salesList.size(); i++) {
			OrderDTO dto = salesList.get(i);
			String shop_no = dto.getShop_no();
			int quantity = dto.getQuantity();
			int price = dto.getPrice();
			
			totalPrice += price;
			
			OrderTable ot = new OrderTable(shop_no, String.valueOf(quantity), String.valueOf(price));
			salesTableList.add(ot);
		}
		
		TableColumn tcShopNum = salesTableView.getColumns().get(0);
		tcShopNum.setCellValueFactory(new PropertyValueFactory("orderNumCol"));
		tcShopNum.setStyle("-fx-alignment: CENTER;");
		
		TableColumn tcQuantity = salesTableView.getColumns().get(1);
		tcQuantity.setCellValueFactory(new PropertyValueFactory("quantityCol"));
		tcQuantity.setStyle("-fx-alignment: CENTER;");
		
		TableColumn tcPrice = salesTableView.getColumns().get(2);
		tcPrice.setCellValueFactory(new PropertyValueFactory("priceCol"));
		tcPrice.setStyle("-fx-alignment: CENTER;");
		
		salesTableView.setItems(salesTableList);
		
		totalSalesTxt.setText(String.valueOf(totalPrice));
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
	
	public void handleStockScene(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/scene/admin.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setTitle("AdminInput page");
			stage.setScene(scene);
			stage.show();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void handleInfoScene(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/scene/adminInfo.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setTitle("AdminInfo page");
			stage.setScene(scene);
			stage.show();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	

}
