package controller;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import dao.OrderDAO;
import dao.ShopInfoDAO;
import dto.OrderDTO;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.OrderTable;
import model.StockTable;

public class ListController implements Initializable{
	
	@FXML private Button saleBtn;
	@FXML private Button userStockBtn;
	@FXML private Button infoBtn;
	@FXML private Button calcBtn;
	
	@FXML private TableView<OrderTable> tableView;
	
	@FXML private DatePicker datePicker;
	@FXML private VBox soldListPane;
	@FXML private VBox pricePane;
	
	@FXML private Label shopLabel;
	@FXML private Label addressLabel;
	@FXML private Label priceLabel;
	@FXML private Label shopTelLabel;
	
    @FXML private TextField totalPriceTxt;
	
	
	private ArrayList<OrderDTO> dayList;
	private ObservableList<OrderTable> list;
	private ObservableList<OrderTable> tableList;
	
	private Date today;
	private String date;
	private int orderNum;
	
	Stage stage;
	Scene scene;
	
	
	public ListController() {
		
		dayList = new ArrayList<>();
		tableList = FXCollections.observableArrayList();
		list = FXCollections.observableArrayList();
		
		// 오늘 날짜 받기
		today = new Date();
		SimpleDateFormat trans = new SimpleDateFormat("yyyy-MM-dd");
		date = trans.format(today);
		

	}
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		saleBtn.setOnAction(event -> handleSaleScene(event));
		userStockBtn.setOnAction(event -> handleStockScene(event));
		calcBtn.setOnAction(event -> handleCalcScene(event));
		infoBtn.setOnAction(event -> handleInfoDialog(event));
		
		makeTable();
		datePicker.setOnAction(event -> datePick(event));
		
		// 더블 클릭 이벤트
		tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(event.getClickCount() > 1) {
					showReceipt();
				}
			}
		});
		
	}
	
	private Stage primaryStage;
	
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	public void handleInfoDialog(ActionEvent event){
		
		ShopInfoDAO dao = ShopInfoDAO.getInstance();
		ShopInfoDTO dto = dao.findShop(LoginController.myId);
		
		try {
			Stage dialog = new Stage(StageStyle.DECORATED);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(primaryStage);
			dialog.setTitle("shop info");
			
			BorderPane pane = (BorderPane)FXMLLoader.load(getClass().getResource("/scene/infoDialog.fxml"));
			Label numberLabel = (Label)pane.lookup("#numberLabel");
			numberLabel.setText(LoginController.myId);
			Label nameLabel = (Label)pane.lookup("#nameLabel");
			nameLabel.setText(dto.getOwner_name());
			Label shopTelLabel = (Label)pane.lookup("#shopTelLabel");
			shopTelLabel.setText(dto.getShop_tel());
			Label ownerTelLabel = (Label)pane.lookup("#ownerTelLabel");
			ownerTelLabel.setText(dto.getOwner_tel());
			Label addressLabel = (Label)pane.lookup("#addressLabel");
			addressLabel.setText(dto.getShop_address());
			
			Button okBtn = (Button)pane.lookup("#okBtn");
			okBtn.setOnAction(e -> dialog.close());
			
			Scene scene = new Scene(pane);
			dialog.setScene(scene);
			dialog.setResizable(false);
			dialog.show();
			
		}catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	public void showReceipt() {
		
		ObservableList<OrderTable> getRow = tableView.getSelectionModel().getSelectedItems();
		OrderTable getOrder = getRow.get(0);
		orderNum = Integer.parseInt(getOrder.getOrderNumCol());
		
		soldListPane.getChildren().clear();
		pricePane.getChildren().clear();
		shopLabel.setText(LoginController.myId);
		ShopInfoDAO dao = ShopInfoDAO.getInstance();
		ShopInfoDTO dto = dao.findShop(LoginController.myId);
		
		addressLabel.setText("address : " + dto.getShop_address());
		shopTelLabel.setText("tel : "+ dto.getShop_tel());
		
		//soldListPane
		
		int totalPrice = 0;
		
		for(int i=0; i<dayList.size(); i++) {
			
			OrderDTO order = dayList.get(i);
			if(order.getOrder_no() == orderNum) {
				Label itemLabel = new Label();
				itemLabel.setText(order.getQuantity() +"x  "+ order.getItem_no());
				soldListPane.getChildren().add(itemLabel);
				
				Label priceLabel = new Label();
				priceLabel.setText(order.getPrice() +" 원");
				pricePane.getChildren().add(priceLabel);
				
				totalPrice += order.getPrice();
			}
		}
		
		priceLabel.setText(totalPrice + "");
		
	}
	
	public void datePick(ActionEvent event) {
		
		date = datePicker.getValue().toString();
		makeList();
		makeTable();
	}
	
	public void makeList() {
		OrderDAO dao = OrderDAO.getInstance();
		dayList = dao.dayList(date, LoginController.myId);
		
		for(int i=0; i<dayList.size(); i++) {
			
			OrderDTO dto = dayList.get(i);
			int order_no = dto.getOrder_no();
			int quantity = dto.getQuantity();
			int price = dto.getPrice();
			
			OrderTable order = new OrderTable(String.valueOf(order_no), 
					String.valueOf(quantity), String.valueOf(price));
			
			list.add(order);
		}
	}
	
	public void makeTable() {
		
		OrderDAO dao = OrderDAO.getInstance();
		dayList = dao.dayList(date, LoginController.myId);
		tableList.clear();
		
		int alphaQuantity = 0;
		int alphaPrice = 0;
		
		for(int i=0; i<dayList.size(); i++) {
			
			OrderDTO fdto = dayList.get(i);
			boolean check = true;
			
			if(i>=1) {
				for(int j=i-1; j<i; j++) {
					OrderDTO adto = dayList.get(j);
					if(fdto.getOrder_no() == adto.getOrder_no()) {
						alphaQuantity += adto.getQuantity();
						alphaPrice += adto.getPrice();
						check = false;
						break;
					}else if(fdto.getOrder_no() != adto.getOrder_no()) {
						alphaQuantity = 0;
						alphaPrice = 0;
						check = true;
						break;
					} // 어떠케하징..
				}
			}
			int order_no = fdto.getOrder_no();
			int quantity = fdto.getQuantity() + alphaQuantity; // 총 수량
			int price = fdto.getPrice() + alphaPrice; // 총 가격

			OrderTable ot = new OrderTable(String.valueOf(order_no), 
					String.valueOf(quantity), String.valueOf(price));
			
			tableList.add(ot);
			
		}
		
		for(int i=0; i<tableList.size(); i++) {
			OrderTable a = tableList.get(i);
			if(i>=1) {
				for(int j=i-1; j<i; j++) {
					OrderTable b = tableList.get(j);
					if(a.getOrderNumCol().equals(b.getOrderNumCol())) {
						tableList.remove(j);
						i--;
					}
				}
			}
		}
		

		TableColumn tcOrderNum = tableView.getColumns().get(0);
		tcOrderNum.setCellValueFactory(new PropertyValueFactory("orderNumCol"));
		tcOrderNum.setStyle("-fx-alignment: CENTER;");
		
		TableColumn tcQuantity = tableView.getColumns().get(1);
		tcQuantity.setCellValueFactory(new PropertyValueFactory("quantityCol"));
		tcQuantity.setStyle("-fx-alignment: CENTER;");
		
		TableColumn tcPrice = tableView.getColumns().get(2);
		tcPrice.setCellValueFactory(new PropertyValueFactory("priceCol"));
		tcPrice.setStyle("-fx-alignment: CENTER;");
		
		tableView.setItems(tableList);
		int totalPrice = 0;
		for(int i=0; i<tableList.size(); i++) {
			OrderTable getPrice = tableList.get(i);
			int rowPrice = Integer.parseInt(getPrice.getPriceCol());
			totalPrice += rowPrice;
		}

		totalPriceTxt.setText(String.valueOf(totalPrice));
		
	}
	
	public void handleSaleScene(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/scene/main.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setTitle("userStock page");
			stage.setScene(scene);
			stage.show();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void handleStockScene(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/scene/userStock.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setTitle("userStock page");
			stage.setScene(scene);
			stage.show();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void handleCalcScene(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/scene/calc.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setTitle("userStock page");
			stage.setScene(scene);
			stage.show();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	

}
