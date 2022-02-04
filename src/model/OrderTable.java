package model;

import javafx.beans.property.SimpleStringProperty;

public class OrderTable {
	
	private SimpleStringProperty orderNumCol;
	private SimpleStringProperty itemNumCol;
	private SimpleStringProperty quantityCol;
	private SimpleStringProperty priceCol;
	private SimpleStringProperty dateCol;
	
	public OrderTable() {
		// TODO Auto-generated constructor stub
	}	
	
	public OrderTable(String orderNumCol, String itemNumCol, String quantityCol, String priceCol, String dateCol) {
		super();
		this.orderNumCol = new SimpleStringProperty(orderNumCol);
		this.itemNumCol = new SimpleStringProperty(itemNumCol);
		this.quantityCol = new SimpleStringProperty(quantityCol);
		this.priceCol = new SimpleStringProperty(priceCol);
		this.dateCol = new SimpleStringProperty(dateCol);
	}
	
	public OrderTable(String orderNumCol, String quantityCol, String priceCol, String dateCol) {
		super();
		this.orderNumCol = new SimpleStringProperty(orderNumCol);
		this.quantityCol = new SimpleStringProperty(quantityCol);
		this.priceCol = new SimpleStringProperty(priceCol);
		this.dateCol = new SimpleStringProperty(dateCol);
	}
	
	public OrderTable(String orderNumCol, String quantityCol, String priceCol) {
		super();
		this.orderNumCol = new SimpleStringProperty(orderNumCol);
		this.quantityCol = new SimpleStringProperty(quantityCol);
		this.priceCol = new SimpleStringProperty(priceCol);
	}
	
	public String getOrderNumCol() {
		return orderNumCol.get();
	}
	
	public void setOrderNumCol(String orderNumCol) {
		this.orderNumCol.set(orderNumCol);
	}
	
	public String getItemNumCol() {
		return itemNumCol.get();
	}
	
	public void setItemNumCol(String itemNumCol) {
		this.itemNumCol.set(itemNumCol);
	}
	
	public String getQuantityCol() {
		return quantityCol.get();
	}
	
	public void setQuantityCol(String quantityCol) {
		this.quantityCol.set(quantityCol);
	}
	
	public String getPriceCol() {
		return priceCol.get();
	}
	
	public void setPriceCol(String priceCol) {
		this.priceCol.set(priceCol);
	}
	
	public String getDateCol() {
		return dateCol.get();
	}
	
	public void setDateCol(String dateCol) {
		this.dateCol.set(dateCol);
	}
	

}
