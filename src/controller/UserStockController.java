package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import dao.ShopInfoDAO;
import dao.StockDAO;
import dto.ShopInfoDTO;
import dto.StockDTO;
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
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import model.Item;
import model.StockTable;

public class UserStockController implements Initializable{
	
	Stage stage;
	Scene scene;

	@FXML private AnchorPane anchorPane;
	
    @FXML private TextField searchTxt;
    @FXML private Button refreshBtn;
    @FXML private Button salesBtn;
    @FXML private Button listBtn;
    @FXML private Button infoBtn;
    @FXML private Button calcBtn;
    
    @FXML private TableView<StockTable> tableView;
    @FXML private TableColumn tcItemNum;
    @FXML private TableColumn tcQuantity;
    @FXML private TableColumn tcSize;
    @FXML private TableColumn tcGender;
    @FXML private TableColumn tcColor;
    @FXML private TableColumn tcKind;
    @FXML private TableColumn tcPrice;
    
    @FXML private Label quantityLabel;
    
    private ArrayList<StockDTO> sortList;
    private ObservableList<StockTable> stockList;
    
    public UserStockController() {
    	
    	sortList = new ArrayList<>();
		stockList = FXCollections.observableArrayList();
	}
        
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		salesBtn.setOnAction(event -> handleSalesScene(event));
		listBtn.setOnAction(event -> handleListScene(event));
		calcBtn.setOnAction(event -> handleCalcScene(event));
		infoBtn.setOnAction(event -> handleInfoDialog(event));
		rightClick();
		
		boolean refresh = false;
		
		refreshBtn.setOnAction(event -> {
			// 수량 다 더하게 해서 나와야함
			makeTable();
			makeFilter();
		});
		
		if(refresh == true) {
			
		}else {
			
			makeTable();
			makeFilter();
			refresh = true;
		}
		


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
						StockTable click = stockList.get(su);
						String item = click.getItemCol();
						clipboardContent.putString(item);
						clipboard.setContent(clipboardContent);
					});
					
					contextMenu.show(tableView, event.getScreenX(), event.getScreenY());
					
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
	

	
	public void makeFilter() {
		
		FilteredList<StockTable> filter = new FilteredList<>(stockList, s->true);
		
		searchTxt.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				int su = 0;
				filter.setPredicate(stock -> {

					if(newValue == null || newValue.isEmpty()) {
						return true;
					}
					
					String upper = newValue.toUpperCase();
					
					if(stock.getItemCol().indexOf(upper) != -1) {
						return true;
					}else if(stock.getQuantityCol().indexOf(upper) != -1) {
						return true;
					}else if(stock.getSizeCol().indexOf(upper) != -1) {
						return true;
					}else if(stock.getGenderCol().indexOf(upper) != -1) {
						return true;
					}else if(stock.getColorCol().toUpperCase().indexOf(upper) != -1) {
						return true;
					}else if(stock.getKindCol().indexOf(upper) != -1) {
						return true;
					}else if(stock.getPriceCol().indexOf(upper) != -1) {
						return true;
					}else {
						return false;
					}

				});
				// 총 수량
				su = tableView.getItems().size();
				int hap = 0;
				for(int i=0; i<su; i++) {
					String quan = (String)tcQuantity.getCellData(i);
					int quantity = Integer.parseInt(quan);
					hap += quantity;
				}
				quantityLabel.setText(String.valueOf(hap));
			}

		});
		
		SortedList<StockTable> sort = new SortedList<>(filter);
		sort.comparatorProperty().bind(tableView.comparatorProperty());
		tableView.setItems(sort);
		
	}
	


	public void makeTable() {
		
		StockDAO dao = StockDAO.getInstance();
		sortList = dao.userStockList(LoginController.myId);
		stockList.clear();
		
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
			
			if(dto.getShop_no().equals(LoginController.myId)) {
				stockList.add(st);
			}
			
		}
		
		tcItemNum = tableView.getColumns().get(0);
		tcItemNum.setCellValueFactory(new PropertyValueFactory("itemCol"));
		tcItemNum.setStyle("-fx-alignment: CENTER;");
		
		tcQuantity = tableView.getColumns().get(1);
		tcQuantity.setCellValueFactory(new PropertyValueFactory("quantityCol"));
		tcQuantity.setStyle("-fx-alignment: CENTER;");
		
		tcSize = tableView.getColumns().get(2);
		tcSize.setCellValueFactory(new PropertyValueFactory("sizeCol"));
		tcSize.setStyle("-fx-alignment: CENTER;");
		
		tcGender = tableView.getColumns().get(3);
		tcGender.setCellValueFactory(new PropertyValueFactory("genderCol"));
		tcGender.setStyle("-fx-alignment: CENTER;");
		
		tcColor = tableView.getColumns().get(4);
		tcColor.setCellValueFactory(new PropertyValueFactory("colorCol"));
		tcColor.setStyle("-fx-alignment: CENTER;");
		
		tcKind = tableView.getColumns().get(5);
		tcKind.setCellValueFactory(new PropertyValueFactory("kindCol"));
		tcKind.setStyle("-fx-alignment: CENTER;");
		
		tcPrice = tableView.getColumns().get(6);
		tcPrice.setCellValueFactory(new PropertyValueFactory<>("priceCol"));
		tcPrice.setStyle("-fx-alignment: CENTER;");
		
		tableView.setItems(stockList);
		
		// 총 수량
		int hap = 0;
		for(int i=0; i<stockList.size(); i++) {
			String quan = (String)tcQuantity.getCellData(i);
			int quantity = Integer.parseInt(quan);
			hap += quantity;
		}
		quantityLabel.setText(String.valueOf(hap));
		
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
	
	public void handleSalesScene(ActionEvent event) {
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
