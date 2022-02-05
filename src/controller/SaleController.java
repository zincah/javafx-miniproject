package controller;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import dao.StockDAO;
import dao.OrderDAO;
import dao.ShopInfoDAO;
import dto.OrderDTO;
import dto.ShopInfoDTO;
import dto.StockDTO;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Item;
import model.OrderTable;
import model.StockTable;

public class SaleController implements Initializable{
	
	Stage stage;
	Scene scene;
	
	@FXML private Button userStockBtn;
	@FXML private Button soldBtn;
	@FXML private Button refundBtn; // 환불 처리 어떠케?
	@FXML private Button soldListBtn;
	@FXML private Button calcBtn;
	@FXML private Button infoBtn; 
    @FXML private Button deleteBtn;
	
	@FXML private TextField saleItemTxt;
	
	@FXML private TableView<StockTable> tableView; 
	
	@FXML private Label totalPrice;
	@FXML private Label totalQuantity;
	
	private ArrayList<OrderDTO> soldList;
	private ArrayList<StockDTO> stockList;
	private ObservableList<StockTable> tableList;
	
	private Stage primaryStage;
	
	static int orderNum = 1;
	
	
	// refund 
	private Stage reDialog;
	private DatePicker firstDay;
	private DatePicker lastDay;
	private Button checkBtn;
    private TextField setQuantityLabel;
    private TextField priceLabel;
	private TableView<OrderTable> soldTableView;
	private ArrayList<OrderDTO> soldItemList = new ArrayList<>();
	private ObservableList<OrderTable> soldTableList = FXCollections.observableArrayList();

	
	public SaleController() {
		stockList = new ArrayList<>();
		soldList = new ArrayList<>();
		tableList = FXCollections.observableArrayList();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		userStockBtn.setOnAction(event -> handleUserStockScene(event));
		soldBtn.setOnAction(event -> handleSale(event));
		soldListBtn.setOnAction(event -> handleListScene(event));
		calcBtn.setOnAction(event -> handleCalcScene(event));
		refundBtn.setOnAction(event -> handleRefund(event));
		delete();
		
		// 판매 테이블 추가
		saleItemTxt.setOnKeyPressed(event -> enterMakeList(event));
		
		//dialog
		infoBtn.setOnAction(event -> handleInfoDialog(event));
		
	}
	
	public void delete() {
		
		tableView.getSelectionModel().selectedItemProperty().addListener(
			new ChangeListener<StockTable>() {

				@Override
				public void changed(ObservableValue<? extends StockTable> arg0, StockTable arg1, StockTable arg2) {
					
					deleteBtn.setOnAction(event -> {
						
						int su = tableView.getSelectionModel().getSelectedIndex();
						tableList.remove(su);
						tableView.refresh();
						showTotal();
					});
					
				}
			});
	}
	

	
	public void handleRefund(ActionEvent event) {

		try {
			reDialog = new Stage(StageStyle.DECORATED);
			reDialog.initModality(Modality.WINDOW_MODAL);
			reDialog.initOwner(primaryStage);
			reDialog.setTitle("sold list");
			
			BorderPane pane = (BorderPane)FXMLLoader.load(getClass().getResource("/scene/refundDialog.fxml"));
			firstDay = (DatePicker)pane.lookup("#firstDay");
			lastDay = (DatePicker)pane.lookup("#lastDay");
			soldTableView = (TableView)pane.lookup("#soldTableView");
			checkBtn = (Button)pane.lookup("#checkBtn");
			setQuantityLabel = (TextField)pane.lookup("#setQuantityLabel");
			priceLabel = (TextField)pane.lookup("#priceLabel");
			
			
			Scene scene = new Scene(pane);
			reDialog.setScene(scene);
			reDialog.setResizable(false);
			reDialog.show();
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		lastDay.setOnAction(e -> makeRefundTable(e));
		
		
	}
	
	public void makeRefundTable(ActionEvent event) {
		
		String first = firstDay.getValue().toString();
		String last = lastDay.getValue().toString();
		
		OrderDAO dao = OrderDAO.getInstance();
		soldItemList = dao.makeRefundList(LoginController.myId, first, last);
		soldTableList.clear();
		
		for(int i=0; i<soldItemList.size(); i++) {
			OrderDTO dto = soldItemList.get(i);
			if(dto.getQuantity() != 0) {
			String item_no = dto.getItem_no();
			int quantity = dto.getQuantity();
			int price = dto.getPrice()/dto.getQuantity();
			
			OrderTable ot = new OrderTable(item_no, String.valueOf(quantity), String.valueOf(price));
			soldTableList.add(ot);
			}
		}
		
		
		TableColumn tcItemNum = soldTableView.getColumns().get(0);
		tcItemNum.setCellValueFactory(new PropertyValueFactory("orderNumCol"));
		tcItemNum.setStyle("-fx-alignment: CENTER;");
		
		TableColumn tcQuantity = soldTableView.getColumns().get(1);
		tcQuantity.setCellValueFactory(new PropertyValueFactory("quantityCol"));
		tcQuantity.setStyle("-fx-alignment: CENTER;");
		
		TableColumn tcPrice = soldTableView.getColumns().get(2);
		tcPrice.setCellValueFactory(new PropertyValueFactory("priceCol"));
		tcPrice.setStyle("-fx-alignment: CENTER;");
		
		soldTableView.setItems(soldTableList);
		
		selectRow();
		
	}
	

	
	public void selectRow() {
		soldTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OrderTable>() {
			@Override
			public void changed(ObservableValue<? extends OrderTable> arg0, OrderTable oldValue, OrderTable newValue) {
				
				OrderTable check = soldTableView.getSelectionModel().getSelectedItem();
				
				setQuantityLabel.setOnKeyPressed(event -> {
					if(event.getCode() == KeyCode.ENTER) {
						int quan = Integer.parseInt(setQuantityLabel.getText());
						if(quan > Integer.parseInt(check.getQuantityCol())) {
							checkBtn.setDisable(true);
						}else {
							checkBtn.setDisable(false);
						}
						int pri = Integer.parseInt(check.getPriceCol());
						String totalPrice = String.valueOf(quan*pri);
						priceLabel.setText(totalPrice);
					}else {
						
					}
				});
				
				checkBtn.setOnAction(event -> {
					
					OrderDAO odao = OrderDAO.getInstance();
					StockDAO sdao = StockDAO.getInstance();
					checkOrderNum();
					int order_no = orderNum;
					String item_no = check.getOrderNumCol();
					int quantity = -Integer.parseInt(setQuantityLabel.getText());
					int price = -Integer.parseInt(priceLabel.getText());
					String shop_no = LoginController.myId;
					
					OrderDTO odto = new OrderDTO(order_no, item_no, quantity, price, shop_no);
					int orderRe = odao.insert(odto);
					
					StockDTO sdto = new StockDTO(item_no, -quantity, (price/quantity), shop_no);
					int stockRe = sdao.insert(sdto);
					
					if(orderRe == 1 || stockRe == 1) {
						System.out.println("성공");
						reDialog.close();
					}else {
						System.out.println("실패");
					}
					
				});
			}
		});
		
		
		
	}

	
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	// dialog
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
	

	public void enterMakeList(KeyEvent event) {
		if(event.getCode() == KeyCode.ENTER) {
			String item = saleItemTxt.getText().toUpperCase();
			StockDAO dao = StockDAO.getInstance();
			StockDTO dto = dao.findItem(item, LoginController.myId);
			
			if(dto.getItem_no()!=null) {
				if(dto.getItem_no().equals(item)) {
					//checkQuantity(); -- 수량 체크하는 메소드 재고보다 많은 양을 추가하면 안되게끔
					makeList(dto);
				}else {
					System.out.println("오류");
				}
			}else {
				System.out.println("재고 없음");
			}
		}
	}
	
	public void checkOrderNum() {
		
		Date date = new Date();
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
		
		String today = simpleDate.format(date);
		//java.sql.Date today = java.sql.Date.valueOf(formattedDate);
		
		OrderDAO dao = OrderDAO.getInstance();
		OrderDTO dto = dao.checkDate(LoginController.myId);
		
		if(dto != null) {
			String getDate = simpleDate.format(dto.getDay());
			
			if(today.equals(getDate)) {
				orderNum = dto.getOrder_no() + 1;
				System.out.println(orderNum);
			}else {
				orderNum = 1;
			}
			
		}else {
			orderNum = 1;
		}

	}
	
	public void handleSale(ActionEvent event) {
		
		checkOrderNum();
		
		OrderDAO odao = OrderDAO.getInstance();
		StockDAO sdao = StockDAO.getInstance();
		OrderDTO odto = odao.checkDate(LoginController.myId);
		
		
		if(odto!=null) {
			System.out.println(odto.getDay());
		}else {
			System.out.println("데이터 없음");
		}

		
		for(int i=0; i<tableList.size(); i++) {
			StockTable table = tableList.get(i);
			String item_no = table.getItemCol();
			int quantity = Integer.parseInt(table.getQuantityCol());
			int price = Integer.parseInt(table.getPriceCol());
			String shop_no = LoginController.myId;
			
			OrderDTO order = new OrderDTO(orderNum, item_no, quantity, price, shop_no);
			soldList.add(order);
			
			StockDTO stock = new StockDTO(item_no, -quantity, price/quantity, shop_no);
			stockList.add(stock);
		}
		
		int orderResult = 0;
		int stockResult = 0;
		
		
		for(int i=0; i<soldList.size(); i++) {
			odto = soldList.get(i);
			orderResult = odao.insert(odto);
			
			StockDTO sdto = stockList.get(i);
			stockResult = sdao.insert(sdto);
		}
		
		if(orderResult == 1 && stockResult == 1) {
			tableList.clear();
			soldList.clear();
			tableView.refresh();
			System.out.println("둘다 성공");
		}
		
		
	}
	
	
	
	public void makeList(StockDTO dto) {
		
		String item_no = dto.getItem_no();
		String items[] = itemProperty(item_no);
		int quantity = dto.getQuantity();
		int price = dto.getPrice();
		String shop_no = dto.getShop_no();

		StockTable st = new StockTable(item_no, String.valueOf(1), items[3], 
				items[0], items[2], items[1], String.valueOf(price),
				shop_no);
		
		// 같은 품번이면 수량 +1
		// 총 수량 오바되지 않게 관리
		
		tableView.refresh();
		
		boolean check = true;
		int pri = Integer.parseInt(st.getPriceCol());
		
		for(int i=0; i<tableList.size(); i++) {
			StockTable stCheck = tableList.get(i);
			if(stCheck.getItemCol().equals(item_no)) {
				int num = Integer.parseInt(stCheck.getQuantityCol());
				if(num < quantity) {
					num += 1;
				}
				stCheck.setQuantityCol(String.valueOf(num));
				stCheck.setPriceCol(String.valueOf(pri*num));
				check = false;
				break;
			}else {
				check = true;
			}
		}
		
		if(check) {
			tableList.add(st);
		}
		
		TableColumn tcItemNum = tableView.getColumns().get(0);
		tcItemNum.setCellValueFactory(new PropertyValueFactory("itemCol"));
		tcItemNum.setStyle("-fx-alignment: CENTER;");
		
		TableColumn tcQuantity = tableView.getColumns().get(1);
		tcQuantity.setCellValueFactory(new PropertyValueFactory("quantityCol"));
		tcQuantity.setStyle("-fx-alignment: CENTER;");
		
		TableColumn tcSize = tableView.getColumns().get(2);
		tcSize.setCellValueFactory(new PropertyValueFactory("sizeCol"));
		tcSize.setStyle("-fx-alignment: CENTER;");
		
		TableColumn tcGender = tableView.getColumns().get(3);
		tcGender.setCellValueFactory(new PropertyValueFactory("genderCol"));
		tcGender.setStyle("-fx-alignment: CENTER;");
		
		TableColumn tcColor = tableView.getColumns().get(4);
		tcColor.setCellValueFactory(new PropertyValueFactory("colorCol"));
		tcColor.setStyle("-fx-alignment: CENTER;");
		
		TableColumn tcKind = tableView.getColumns().get(5);
		tcKind.setCellValueFactory(new PropertyValueFactory("kindCol"));
		tcKind.setStyle("-fx-alignment: CENTER;");
		
		TableColumn tcPrice = tableView.getColumns().get(6);
		tcPrice.setCellValueFactory(new PropertyValueFactory("priceCol"));
		tcPrice.setStyle("-fx-alignment: CENTER;");
		
		tableView.setItems(tableList);
		
		showTotal();
		
	}
	
	public void showTotal() {
		int price = 0;
		int quantity = 0;
		
		for(int i=0; i<tableList.size(); i++) {
			StockTable st = tableList.get(i);
			price += Integer.parseInt(st.getPriceCol());
			quantity += Integer.parseInt(st.getQuantityCol());
		}
		
		totalPrice.setText(price + "");
		totalQuantity.setText(quantity + "");
		
	}
	
	private String[] itemProperty(String item_no) {
		
		String items[] = new String[5];
		
		String gender = item_no.substring(0,2);
		String kind = item_no.substring(2,4);
		String color = item_no.substring(4, 6);
		String size = item_no.substring(6, 9);
		String num = item_no.substring(9);
		
		if(gender.equals("LD")) {
			items[0] = Item.LD;
		}else if(gender.equals("GM")){
			items[0] = Item.GM;
		}else {
			
		}
		
		if(kind.equals("OU")) {
			items[1] = Item.OU;
		}else if(kind.equals("TO")) {
			items[1] = Item.TO;
		}else if(kind.equals("BO")) {
			items[1] = Item.BO;
		}else if(kind.equals("FO")) {
			items[1] = Item.FO;
		}else {
			// 오류 다이얼로그 띄우기
			
		}
		
		if(color.equals("BK")) {
			items[2] = Item.BK;
		}else if(color.equals("WH")) {
			items[2] = Item.WH;
		}else if(color.equals("BG")) {
			items[2] = Item.BG;
		}else if(color.equals("GY")) {
			items[2] = Item.GY;
		}else {
			
		}
		
		if(size.equals("090")) {
			items[3] = Item.S;
		}else if(size.equals("095")) {
			items[3] = Item.M;
		}else if(size.equals("100")) {
			items[3] = Item.L;
		}else if(size.equals("105")) {
			items[3] = Item.XL;
		}else if(size.equals("110")) {
			items[3] = Item.XXL;
		}else {
			
		}
		
		items[4] = num;
		
		return items;
	}
	
	public void handleUserStockScene(ActionEvent event) {
		
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

	public void handleListScene(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/scene/soldList.fxml"));
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
