package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dao.ShopInfoDAO;
import dao.StockDAO;
import dto.StockDTO;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Item;
import model.OrderTable;
import model.StockTable;

public class AdminInputController implements Initializable{
	
	//dto
	private StockDTO dto;
	
	Stage stage;
	Scene scene;
	
	@FXML private TableView<StockTable> tableView;
	
    @FXML private TextField itemNoTxt;
    @FXML private TextField quantityTxt;
    @FXML private TextField priceTxt;
    @FXML private ComboBox shopNumSelect;
    
    @FXML private Button uploadBtn;
    @FXML private Button commitBtn;
    @FXML private Button resetBtn;
    @FXML private Button stockSceneBtn;
    @FXML private Button salesBtn;
    @FXML private Button deleteBtn;
    @FXML private Button infoBtn;
    
    // shoplist
    private ArrayList<String> shopNo;
    private ObservableList<String> list;
    
    // stocklist
    private ArrayList<StockTable> stList;
    private ObservableList<StockTable> stockList;
    private ArrayList<StockDTO> stdto = new ArrayList<StockDTO>();
    
    
    
    public AdminInputController() {
    	
		ShopInfoDAO dao = ShopInfoDAO.getInstance();
		shopNo = dao.shopList();
		list = FXCollections.observableArrayList();
		
		for(int i=0; i<shopNo.size(); i++) {
			list.add(shopNo.get(i));
		}
		
		

	}  
    

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		stockSceneBtn.setOnAction(event -> handleStockScene(event));
		
		uploadBtn.setOnAction(event -> upload(event));
		shopNumSelect.setItems(list);
		shopNumSelect.setValue(list.get(0));
		
		resetBtn.setOnAction(event -> handleResetTable(event));
		commitBtn.setOnAction(event -> handleCommit(event));
		salesBtn.setOnAction(event -> handleSalesScene(event));
		infoBtn.setOnAction(event -> handleInfoScene(event));
		deleteCell();
	}
	
	public void deleteCell() {
		tableView.getSelectionModel().selectedItemProperty().addListener(
				new ChangeListener<StockTable>() {

				@Override
				public void changed(ObservableValue<? extends StockTable> arg0, StockTable oldValue, StockTable newValue) {
					if(newValue!=null) {
						deleteBtn.setOnAction(event -> {
							
							int su = tableView.getSelectionModel().getSelectedIndex();
							stockList.remove(su);
							tableView.refresh();

						});
					}
				}
			});
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
	
	public void handleCommit(ActionEvent event) {
		for(int i=0; i<stockList.size(); i++) {
			StockTable stock = stockList.get(i);
			String item_no = stock.getItemCol();
			int quantity = Integer.parseInt(stock.getQuantityCol());
			int price = Integer.parseInt(stock.getPriceCol());
			String shop_no = stock.getShopCol();
			
			StockDTO dto = new StockDTO(item_no, quantity, price, shop_no);
			stdto.add(dto);
			System.out.println("list 저장 완료");
			// 디비에 저장되면 됬다고 list 띄워주기
		}
		
		StockDAO dao = StockDAO.getInstance();
		int result = 0;
		
		for(int i=0; i<stdto.size(); i++) {
			StockDTO dto = stdto.get(i);
			result = dao.insert(dto);
		}
		
		if(result == 1) {
			// 정보 정상적으로 들어갔다고 다이얼 로그
			System.out.println("성공");
			handleResetTable(event);
		}
	}
	
	
	public void handleResetTable(ActionEvent event) {
		stdto.clear();
		stockList.clear();
		tableView.refresh();
		
	}
	
	
	public void upload(ActionEvent event) {
		String item_no = itemNoTxt.getText().toUpperCase();
		String items[] = itemProperty(item_no);
		int quantity = Integer.parseInt(quantityTxt.getText());
		int price = Integer.parseInt(priceTxt.getText());
		String shop_no = (String) shopNumSelect.getValue();
		
		StockTable st = new StockTable(item_no, String.valueOf(quantity), items[3], 
						items[0], items[2], items[1], String.valueOf(price),
						shop_no);
		
		if(stockList == null) {
			stockList = FXCollections.observableArrayList(st);
		}else {
			tableView.refresh();
			stockList.add(st);
		}
		
		tableView.setEditable(true);
		
		TableColumn tcItemNum = tableView.getColumns().get(0);
		tcItemNum.setCellValueFactory(new PropertyValueFactory("itemCol"));
		tcItemNum.setStyle("-fx-alignment: CENTER;");
		tcItemNum.setCellFactory(TextFieldTableCell.forTableColumn());
		tcItemNum.setOnEditCommit(new EventHandler<CellEditEvent>() {

			@Override
			public void handle(CellEditEvent event) {
				 StockTable ot = (StockTable)event.getRowValue();
				 String newItemNum = (String)event.getNewValue();
				 ot.setItemCol(newItemNum.toUpperCase());
				 String item_no = ot.getItemCol().toUpperCase();
				 String items[] = itemProperty(item_no);
				 
				 
				 ot.setSizeCol(items[3]);
				 ot.setColorCol(items[2]);
				 ot.setKindCol(items[1]);
				 ot.setGenderCol(items[0]);
				 tableView.refresh();
			}
			
		});
		
		TableColumn tcQuantity = tableView.getColumns().get(1);
		tcQuantity.setCellValueFactory(new PropertyValueFactory("quantityCol"));
		tcQuantity.setStyle("-fx-alignment: CENTER;");
		tcQuantity.setCellFactory(TextFieldTableCell.forTableColumn());
		tcQuantity.setOnEditCommit(new EventHandler<CellEditEvent>() {

			@Override
			public void handle(CellEditEvent event) {
				 StockTable ot = (StockTable)event.getRowValue();
				 ot.setQuantityCol((String)event.getNewValue());
				 tableView.refresh();
			}
			
		});
		
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
		tcPrice.setCellFactory(TextFieldTableCell.forTableColumn());
		tcPrice.setOnEditCommit(new EventHandler<CellEditEvent>() {

			@Override
			public void handle(CellEditEvent event) {
				 StockTable ot = (StockTable)event.getRowValue();
				 ot.setPriceCol((String)event.getNewValue());
				 tableView.refresh();
			}
			
		});
		
		TableColumn tcShopNum = tableView.getColumns().get(7);
		tcShopNum.setCellValueFactory(new PropertyValueFactory("shopCol"));
		tcShopNum.setStyle("-fx-alignment: CENTER;");
		
		tableView.setItems(stockList);
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
			cancel();
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
			cancel();
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
			cancel();
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
			cancel();
		}
		
		items[4] = num;
		
		return items;
	}

	
	public void cancel() {
		
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
