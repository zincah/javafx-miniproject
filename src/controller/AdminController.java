package controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.ResourceBundle;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dao.ShopInfoDAO;
import dao.StockDAO;
import dto.StockDTO;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Item;
import model.StockTable;

public class AdminController implements Initializable{
	
	@FXML Button inputSceneBtn;
	@FXML Button deleteBtn;
	@FXML Button sortTableBtn;
	@FXML Button showStockBtn;
	@FXML Button salesBtn;
	@FXML Button infoBtn;
    @FXML private Button exportBtn;
	
	@FXML TableView<StockTable> tableView;
	
	// filter1
	@FXML TextField searchBox;
	
	// filter2
	@FXML ComboBox<String> coItemNum;
	@FXML ComboBox<String> coSize;
	@FXML ComboBox<String> coGender;
	@FXML ComboBox<String> coShop;
	@FXML ComboBox<String> coKind;
	@FXML ComboBox<String> coDate;
	
	private Stage stage;
	private Scene scene;
	
	private ArrayList<StockDTO> itemList;
	private ArrayList<StockDTO> sortList;
	private ObservableList<StockTable> stockList;
	private SortedList<StockTable> sort;
	
	public boolean check;
	public boolean sorted;
	
	public AdminController() {
		
		itemList = new ArrayList<>();
		stockList = FXCollections.observableArrayList();
		
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		sorted = false;
		
		inputSceneBtn.setOnAction(event-> handleInputScene(event));
		sortTableBtn.setOnAction(event -> handleSortTable(event));
		showStockBtn.setOnAction(event -> handleStockTable(event));
		salesBtn.setOnAction(event -> handleSalesScene(event));
		infoBtn.setOnAction(event -> handleInfoScene(event));
		exportBtn.setOnAction(event -> handleExport(event));
		rightClick();
		
		// tableView
		if(stockList == null) {
			System.out.println("데이터 없음");
		}else {
			makeTable();
		}
		
		makeFilter();
		makeList();
		
		// update(); // 판매나 수정할 시 바로 반영되게 해줘야함.
		// makeStockList();

		deleteCell();

	}
	
	public void rightClick() {
		tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {

			//event.getButton() == MouseButton.SECONDAR
			@Override
			public void handle(MouseEvent event) {
				if(event.getButton() == MouseButton.SECONDARY) {
					ContextMenu contextMenu = new ContextMenu();
					MenuItem copy = new MenuItem("copy item");
					MenuItem hello = new MenuItem("hello :)");
					contextMenu.getItems().add(copy);
					contextMenu.getItems().add(hello); 
					copy.setOnAction(e -> {
						Clipboard clipboard = Clipboard.getSystemClipboard();
						ClipboardContent clipboardContent = new ClipboardContent();
						int su = tableView.getSelectionModel().getSelectedIndex();
						if(sorted == false) {
							StockTable click = stockList.get(su);
							String item = click.getItemCol();
							clipboardContent.putString(item);
							clipboard.setContent(clipboardContent);
						}else {
							StockTable sortClick = sort.get(su);
							String sortItem = sortClick.getItemCol();
							clipboardContent.putString(sortItem);
							clipboard.setContent(clipboardContent);
						}
						
					});
					
					contextMenu.show(tableView, event.getScreenX(), event.getScreenY());
					
				}
			}
		});
	}
	
	public void handleExport(ActionEvent event) {
		
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("stocks");
		XSSFRow header = sheet.createRow(0);
		header.createCell(0).setCellValue("item_no");
		header.createCell(1).setCellValue("quantity");
		header.createCell(2).setCellValue("size");
		header.createCell(3).setCellValue("gender");
		header.createCell(4).setCellValue("color");
		header.createCell(5).setCellValue("kind");
		header.createCell(6).setCellValue("price");
		header.createCell(7).setCellValue("shop_no");
		header.createCell(8).setCellValue("date");
		
		if(sorted == false) {
			
			for(int i=0; i<stockList.size(); i++) {
				StockTable st = stockList.get(i);
				XSSFRow row = sheet.createRow(i+1);
				row.createCell(0).setCellValue(st.getItemCol());
				row.createCell(1).setCellValue(st.getQuantityCol());
				row.createCell(2).setCellValue(st.getSizeCol());
				row.createCell(3).setCellValue(st.getGenderCol());
				row.createCell(4).setCellValue(st.getColorCol());
				row.createCell(5).setCellValue(st.getKindCol());
				row.createCell(6).setCellValue(st.getPriceCol());
				row.createCell(7).setCellValue(st.getShopCol());
				row.createCell(8).setCellValue(st.getDateCol());
				
			}
		}else {
			for(int i=0; i<sort.size(); i++) {
				StockTable st = sort.get(i);
				XSSFRow row = sheet.createRow(i+1);
				row.createCell(0).setCellValue(st.getItemCol());
				row.createCell(1).setCellValue(st.getQuantityCol());
				row.createCell(2).setCellValue(st.getSizeCol());
				row.createCell(3).setCellValue(st.getGenderCol());
				row.createCell(4).setCellValue(st.getColorCol());
				row.createCell(5).setCellValue(st.getKindCol());
				row.createCell(6).setCellValue(st.getPriceCol());
				row.createCell(7).setCellValue(st.getShopCol());
				row.createCell(8).setCellValue(st.getDateCol());
				
			}
		}
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(null);
		alert.setContentText("success to export excel file");
		alert.showAndWait();
		
		try {
			FileOutputStream fileout = new FileOutputStream("stockdate.xlsx");
			wb.write(fileout);
			fileout.close();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
	}
	
	
	
	public void handleStockTable(ActionEvent event) {
		sorted = false;
		stockList.clear();
		tableView.refresh();
		makeTable();
		makeFilter();
		deleteBtn.setDisable(false);
		sortTableBtn.setDisable(false);
		showStockBtn.setDisable(true);
	}
	
	public void deleteCell() {
		tableView.getSelectionModel().selectedItemProperty().addListener(
				new ChangeListener<StockTable>() {

				@Override
				public void changed(ObservableValue<? extends StockTable> arg0, StockTable oldValue, StockTable newValue) {
					if(newValue != null) {
						deleteBtn.setOnAction(event -> {
							
							// seq 를 받아와야해 date로 구별할 수 있지 않을까?
							int su = tableView.getSelectionModel().getSelectedIndex();
							StockTable st = stockList.get(su);
							int seq = Integer.parseInt(st.getSeqCol());
							
							StockDAO dao = StockDAO.getInstance();
							int result = dao.delete(seq);
							
							if(result == 1) {
								System.out.println("삭제 완료");
								stockList.remove(su);
								tableView.refresh();
							}else {
								System.out.println("삭제 실패");
							}

						});
					}
				}
				
		});
	}
	
	public void handleSortTable(ActionEvent event) {
		// 버튼 누르면 다른 버튼이 앞으로 오게끔
		showStockBtn.setDisable(false);
		sorted = false;
		
		StockDAO dao = StockDAO.getInstance();
		sortList = dao.sortItems();
		
		stockList.clear();
		tableView.refresh();
		
		for(int i=0; i<sortList.size(); i++) {
			StockDTO dto = sortList.get(i);
			String item_no = dto.getItem_no();
			String items[] = itemProperty(item_no);
			int quantity = dto.getQuantity();
			int price = dto.getPrice();
			String shop_no = dto.getShop_no();
			
			StockTable st = new StockTable(item_no, String.valueOf(quantity), items[3], 
						items[0], items[2], items[1], String.valueOf(price),
						shop_no);
			
			stockList.add(st);
		}
		
		sortedTable();
		deleteBtn.setDisable(true);
		sortTableBtn.setDisable(true);
		
	}
	
	// 정렬된 테이블 만들기
	public void sortedTable() {
		TableColumn tcItemNum = tableView.getColumns().get(0);
		tcItemNum.setCellValueFactory(new PropertyValueFactory("itemCol"));
		tcItemNum.setStyle("-fx-alignment: CENTER;");
		
		TableColumn tcQuantity = tableView.getColumns().get(1);
		tcQuantity.setCellValueFactory(new PropertyValueFactory<>("quantityCol"));
		tcQuantity.setStyle("-fx-alignment: CENTER;");
		
		TableColumn tcSize = tableView.getColumns().get(2);
		tcSize.setCellValueFactory(new PropertyValueFactory<>("sizeCol"));
		tcSize.setStyle("-fx-alignment: CENTER;");
		
		TableColumn tcGender = tableView.getColumns().get(3);
		tcGender.setCellValueFactory(new PropertyValueFactory<>("genderCol"));
		tcGender.setStyle("-fx-alignment: CENTER;");
		
		TableColumn tcColor = tableView.getColumns().get(4);
		tcColor.setCellValueFactory(new PropertyValueFactory<>("colorCol"));
		tcColor.setStyle("-fx-alignment: CENTER;");
		
		TableColumn tcKind = tableView.getColumns().get(5);
		tcKind.setCellValueFactory(new PropertyValueFactory<>("kindCol"));
		tcKind.setStyle("-fx-alignment: CENTER;");
		
		TableColumn tcPrice = tableView.getColumns().get(6);
		tcPrice.setCellValueFactory(new PropertyValueFactory<>("priceCol"));
		tcPrice.setStyle("-fx-alignment: CENTER;");
		
		TableColumn tcShopNum = tableView.getColumns().get(7);
		tcShopNum.setCellValueFactory(new PropertyValueFactory<>("shopCol"));
		tcShopNum.setStyle("-fx-alignment: CENTER;");
		
		TableColumn tcDate = tableView.getColumns().get(8);
		tcDate.setCellValueFactory(new PropertyValueFactory<>("dateCol"));
		tcDate.setStyle("-fx-alignment: CENTER;");
		
		
		tableView.setItems(stockList);
		makeFilter();
	}
	
	// 상단 combobox
	public void makeList() {
		ObservableList<String> gen = FXCollections.observableArrayList();
		gen.addAll("여성", "남성");
		coGender.setItems(gen);
		
		ObservableList<String> size = FXCollections.observableArrayList();
		size.addAll("090", "095", "100", "105", "110");
		coSize.setItems(size);
		
		ObservableList<String> kind = FXCollections.observableArrayList();
		kind.addAll("아우터", "상의", "하의", "신발");
		coKind.setItems(kind);
		
		ObservableList<String> itemNum = FXCollections.observableArrayList();
		ObservableList<String> shopNum = FXCollections.observableArrayList();
		ObservableList<String> day = FXCollections.observableArrayList();
		
		
		for(int i=0; i<stockList.size(); i++) {
			StockTable st = stockList.get(i);
			boolean it = true;
			
			for(int j=0; j<i; j++) {
				StockTable rst = stockList.get(j);
				if(st.getItemCol().equals(rst.getItemCol())){
					it = false;
					break;
				}
			}

			if(it == true) {
				itemNum.add(st.getItemCol());
			}
		}
		
		coItemNum.setItems(itemNum);
		
		for(int i=0; i<stockList.size(); i++) {
			StockTable st = stockList.get(i);
			boolean it = true;
			
			for(int j=0; j<i; j++) {
				StockTable rst = stockList.get(j);
				if(st.getShopCol().equals(rst.getShopCol())){
					it = false;
					break;
				}
			}

			if(it == true) {
				shopNum.add(st.getShopCol());
			}
		}
		
		coShop.setItems(shopNum);
		
		
		for(int i=0; i<stockList.size(); i++) {
			StockTable st = stockList.get(i);
			boolean it = true;
			
			if (st.getDateCol() != null) {
				for(int j=0; j<i; j++) {
					StockTable rst = stockList.get(j);
					if(st.getDateCol().equals(rst.getDateCol())){
						it = false;
						break;
					}
				}
	
				if(it == true) {
					day.add(st.getDateCol());
				}
			}
		}
		
		if(day != null) {
			coDate.setItems(day);
		}
		
	}
	
	
	public void makeFilter() {
		FilteredList<StockTable> filter = new FilteredList<>(stockList, s->true);
		searchBox.textProperty().addListener((observable, oldValue, newValue)->{
			
			filter.setPredicate(stock -> {
				
				if(newValue == null || newValue.isEmpty()) {
					sorted = false;
					return true;
				}
				
				String upper = newValue.toUpperCase();
				
				if(stock.getItemCol().indexOf(upper) != -1) {
					sorted = true;
					return true;
				}else if(stock.getGenderCol().indexOf(upper) != -1){
					sorted = true;
					return true;
				}else if(stock.getColorCol().toUpperCase().indexOf(upper) != -1) {
					sorted = true;
					return true;
				}else if(stock.getKindCol().indexOf(upper) != -1) {	
					sorted = true;
					return true;
				}else if(stock.getShopCol().indexOf(upper) != -1) {
					sorted = true;
					return true;
				}else if(stock.getDateCol().indexOf(upper) != -1) {
					sorted = true;
					return true;
				}else {
					sorted = true;
					return false;
				}
				
			});
		});
		
		sort = new SortedList<>(filter);
		sort.comparatorProperty().bind(tableView.comparatorProperty());
		tableView.setItems(sort);
		
	}
	
	
	public void makeTable() {
		
		StockDAO dao = StockDAO.getInstance();
		itemList = dao.getItems();
		
		for(int i=0; i<itemList.size(); i++) {
			StockDTO dto = itemList.get(i);
			int seq = dto.getSeq();
			String item_no = dto.getItem_no();
			String items[] = itemProperty(item_no);
			int quantity = dto.getQuantity();
			int price = dto.getPrice();
			String shop_no = dto.getShop_no();
			Date day = dto.getDay();
			
			StockTable st = new StockTable(String.valueOf(seq), item_no, String.valueOf(quantity), items[3], 
						items[0], items[2], items[1], String.valueOf(price),
						shop_no, String.valueOf(day));
			
			stockList.add(st);
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
		
		TableColumn tcShopNum = tableView.getColumns().get(7);
		tcShopNum.setCellValueFactory(new PropertyValueFactory("shopCol"));
		tcShopNum.setStyle("-fx-alignment: CENTER;");
		
		TableColumn tcDate = tableView.getColumns().get(8);
		tcDate.setCellValueFactory(new PropertyValueFactory("dateCol"));
		tcDate.setStyle("-fx-alignment: CENTER;");
		tcDate.setSortType(TableColumn.SortType.DESCENDING);
		
		
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
		}
		
		if(kind.equals("OU")) {
			items[1] = Item.OU;
		}else if(kind.equals("TO")) {
			items[1] = Item.TO;
		}else if(kind.equals("BO")) {
			items[1] = Item.BO;
		}else if(kind.equals("FO")) {
			items[1] = Item.FO;
		}
		
		if(color.equals("BK")) {
			items[2] = Item.BK;
		}else if(color.equals("WH")) {
			items[2] = Item.WH;
		}else if(color.equals("BG")) {
			items[2] = Item.BG;
		}else if(color.equals("GY")) {
			items[2] = Item.GY;
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
		}
		
		items[4] = num;
		
		return items;
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
