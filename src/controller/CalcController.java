package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.OrderTable;

public class CalcController implements Initializable{
	
	Stage stage;
	Scene scene;
 	
    @FXML private Button salesBtn;
    @FXML private Button userStockBtn;
    @FXML private Button soldListBtn;
    @FXML private Button infoBtn;

    @FXML private DatePicker firstDayPick;
    @FXML private DatePicker lastDayPick;
    
    @FXML private TextField searchTxt;
    @FXML private TableView<OrderTable> itemTableView;
    @FXML private Label priceLabel;
    @FXML private Label quantityLabel;
    
    @FXML private TableView<OrderTable> saleDayTableView;

    @FXML private LineChart<String, Number> saleChart;
    
    @FXML private TextField quantityTxt;
    @FXML private TextField priceTxt;
    
    private String firstDay;
    private String lastDay;
    private String fDay; 
    private String lDay;
    
    private ArrayList<OrderDTO> soldItemList;
    private ObservableList<OrderTable> soldItemTableList;
    
    private ArrayList<OrderDTO> soldDayList;
    private ObservableList<OrderTable> soldDayTableList;
    
    private ObservableList<String> yearList;
    private ObservableList<String> monthList;
    @FXML private ComboBox<String> selectYear;
    @FXML private ComboBox<String> selectMonth;
    
    private ObservableList chartList;
    
    public CalcController() {
    	soldItemList = new ArrayList<>();
    	soldItemTableList = FXCollections.observableArrayList();
    	
    	soldDayList = new ArrayList<>();
    	soldDayTableList = FXCollections.observableArrayList();
    	
    	selectYear = new ComboBox<>();
    	yearList = FXCollections.observableArrayList("2022", "2023", "2024", "2025");
    	
    	selectMonth = new ComboBox<>();
    	String[] months = {"1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"};
    	monthList = FXCollections.observableArrayList(months);
    	
    	chartList = FXCollections.observableArrayList();
    	
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		selectYear.setItems(yearList);
		selectYear.setValue("2022");
		selectMonth.setItems(monthList);
		
		salesBtn.setOnAction(event -> handleSaleScene(event));
		userStockBtn.setOnAction(event -> handleStockScene(event));
		soldListBtn.setOnAction(event -> handleListScene(event));
		infoBtn.setOnAction(event -> handleInfoDialog(event));
		
		lastDayPick.setOnAction(event -> datePick(event));
		
		selectMonth.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String oldValue, String newValue) {
				
				daySet(newValue);
				makeSoldOfMonthTable();
				makeChart();
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
	
	public void makeChart() {
		
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		
		if(saleChart.getData()!=null) {
			saleChart.getData().clear();
			series.getData().clear();
		}
		
		for(int i=0; i<soldDayTableList.size(); i++) {
			OrderTable ot = soldDayTableList.get(i);
			String x = ot.getDateCol().substring(ot.getDateCol().length()-2);
			Number y = Integer.parseInt(ot.getPriceCol())/10000;
			series.getData().add(new XYChart.Data<String, Number>(x, y));
		}
		series.setName("sale line chart");
		
		saleChart.getData().add(series);
	}
	
	public void makeSoldOfMonthTable() {
		OrderDAO dao = OrderDAO.getInstance();
		soldDayList = dao.soldOfMonth(LoginController.myId, fDay, lDay);
		soldDayTableList.clear();
		
		for(int i=0; i<soldDayList.size(); i++) {
			OrderDTO dto = soldDayList.get(i);
			Date day = dto.getDay();
			int price = dto.getPrice();
			int order_no = dto.getOrder_no();
			int quantity = dto.getQuantity();
			
			OrderTable ot = new OrderTable(String.valueOf(order_no), String.valueOf(quantity),
											String.valueOf(price), String.valueOf(day));
			
			soldDayTableList.add(ot);
		}
		
		TableColumn tcDate = saleDayTableView.getColumns().get(0);
		tcDate.setCellValueFactory(new PropertyValueFactory("dateCol"));
		tcDate.setStyle("-fx-alignment: CENTER;");
		
		TableColumn tcPrice = saleDayTableView.getColumns().get(1);
		tcPrice.setCellValueFactory(new PropertyValueFactory("priceCol"));
		tcPrice.setStyle("-fx-alignment: CENTER;");
		
		TableColumn tcOrderNum = saleDayTableView.getColumns().get(2);
		tcOrderNum.setCellValueFactory(new PropertyValueFactory("orderNumCol"));
		tcOrderNum.setStyle("-fx-alignment: CENTER;");
		
		TableColumn tcQuantity = saleDayTableView.getColumns().get(3);
		tcQuantity.setCellValueFactory(new PropertyValueFactory("quantityCol"));
		tcQuantity.setStyle("-fx-alignment: CENTER;");
		
		saleDayTableView.setItems(soldDayTableList);
		
		int p = 0;
		int q = 0;
		
		for(int i=0; i<soldDayTableList.size(); i++) {
			OrderTable ot = soldDayTableList.get(i);
			p += Integer.parseInt(ot.getPriceCol());
			q += Integer.parseInt(ot.getQuantityCol());
		}
		
		quantityTxt.setText(String.valueOf(q));
		priceTxt.setText(String.valueOf(p));
		
	}
	
	public void daySet(String newValue) {
		Calendar c = Calendar.getInstance();
		int month = Integer.parseInt(newValue.substring(0, newValue.lastIndexOf("월")));
		
		c.set(Integer.parseInt(selectYear.getValue()), month-1, 1);
		int finalday = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		System.out.println(finalday);
		
		fDay = selectYear.getValue() + "-" + 
					String.format("%02d", month) + "-01";
		
		lDay = selectYear.getValue() + "-" + 
				String.format("%02d", month) + "-" + String.valueOf(finalday);

	}
	
	
	public void datePick(ActionEvent event) {
		String search = searchTxt.getText().toUpperCase();
		firstDay = firstDayPick.getValue().toString();
		lastDay = lastDayPick.getValue().toString();
		
		System.out.println(firstDay + " " + lastDay);
		makeSoldItemTable(search);
	}
	
	public void makeSoldItemTable(String search) {
		OrderDAO dao = OrderDAO.getInstance();
		soldItemList = dao.itemSoldList(search, LoginController.myId, firstDay, lastDay);
		soldItemTableList.clear();
		
		int totalQuantity = 0;
		int totalPrice = 0;
		
		for(int i=0; i<soldItemList.size(); i++) {
			OrderDTO dto = soldItemList.get(i);
			String item_no = dto.getItem_no();
			int quantity = dto.getQuantity();
			int price = dto.getPrice();
			Date day = dto.getDay();
			
			totalQuantity += quantity;
			totalPrice += price;
			
			OrderTable ot = new OrderTable("order", item_no, String.valueOf(quantity), 
					String.valueOf(price), String.valueOf(day));
			
			soldItemTableList.add(ot);
		}
		
		TableColumn tcDate = itemTableView.getColumns().get(0);
		tcDate.setCellValueFactory(new PropertyValueFactory("dateCol"));
		tcDate.setStyle("-fx-alignment: CENTER;");
		
		TableColumn tcQuantity = itemTableView.getColumns().get(1);
		tcQuantity.setCellValueFactory(new PropertyValueFactory("quantityCol"));
		tcQuantity.setStyle("-fx-alignment: CENTER;");
		
		TableColumn tcPrice = itemTableView.getColumns().get(2);
		tcPrice.setCellValueFactory(new PropertyValueFactory("priceCol"));
		tcPrice.setStyle("-fx-alignment: CENTER;");
		
		itemTableView.setItems(soldItemTableList);
		
		
		priceLabel.setText(String.valueOf(totalPrice));
		quantityLabel.setText(String.valueOf(totalQuantity));
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
