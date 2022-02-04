package model;

import javafx.beans.property.SimpleStringProperty;

public class StockTable {
	
	private SimpleStringProperty seqCol;
	private SimpleStringProperty itemCol;
	private SimpleStringProperty quantityCol;
	private SimpleStringProperty sizeCol;
	private SimpleStringProperty genderCol;
	private SimpleStringProperty colorCol;
	private SimpleStringProperty kindCol;
	private SimpleStringProperty priceCol;
	private SimpleStringProperty shopCol;
	private SimpleStringProperty dateCol;
	
	public StockTable(String itemCol, String quantityCol, String sizeCol,
			String genderCol, String colorCol, String kindCol,
			String priceCol, String shopCol) {
		super();
		this.itemCol = new SimpleStringProperty(itemCol);
		this.quantityCol = new SimpleStringProperty(quantityCol);
		this.sizeCol = new SimpleStringProperty(sizeCol);
		this.genderCol = new SimpleStringProperty(genderCol);
		this.colorCol = new SimpleStringProperty(colorCol);
		this.kindCol = new SimpleStringProperty(kindCol);
		this.priceCol = new SimpleStringProperty(priceCol);
		this.shopCol = new SimpleStringProperty(shopCol);
		this.dateCol = new SimpleStringProperty("x");
	}
	
	public StockTable(String seqCol, String itemCol, String quantityCol, String sizeCol,
			String genderCol, String colorCol, String kindCol,
			String priceCol, String shopCol, String dateCol) {
		super();
		
		this.seqCol = new SimpleStringProperty(seqCol);
		this.itemCol = new SimpleStringProperty(itemCol);
		this.quantityCol = new SimpleStringProperty(quantityCol);
		this.sizeCol = new SimpleStringProperty(sizeCol);
		this.genderCol = new SimpleStringProperty(genderCol);
		this.colorCol = new SimpleStringProperty(colorCol);
		this.kindCol = new SimpleStringProperty(kindCol);
		this.priceCol = new SimpleStringProperty(priceCol);
		this.shopCol = new SimpleStringProperty(shopCol);
		this.dateCol = new SimpleStringProperty(dateCol);
	}
	
	public String getSeqCol() {
		return seqCol.get();
	}
	
	public void setSeqCol(String seqCol) {
		this.seqCol.set(seqCol);
	}

	public String getItemCol() {
		return itemCol.get();
	}
	
	public void setItemCol(String itemCol) {
		this.itemCol.set(itemCol);
	}
	
	public String getQuantityCol() {
		return quantityCol.get();
	}
	
	public void setQuantityCol(String quantityCol) {
		this.quantityCol.set(quantityCol);
	}
	
	public String getSizeCol() {
		return sizeCol.get();
	}
	
	public void setSizeCol(String sizeCol) {
		this.sizeCol.set(sizeCol);
	}
	
	public String getGenderCol() {
		return genderCol.get();
	}
	
	public void setGenderCol(String genderCol) {
		this.genderCol.set(genderCol);
	}
	
	public String getColorCol() {
		return colorCol.get();
	}
	
	public void setColorCol(String colorCol) {
		this.colorCol.set(colorCol);
	}
	
	public String getKindCol() {
		return kindCol.get();
	}
	
	public void setKindCol(String kindCol) {
		this.kindCol.set(kindCol);
	}
	
	public String getPriceCol() {
		return priceCol.get();
	}
	
	public void setPriceCol(String priceCol) {
		this.priceCol.set(priceCol);
	}
	
	public String getShopCol() {
		return shopCol.get();
	}
	
	public void setShopCol(String shopCol) {
		this.shopCol.set(shopCol);
	}
	
	public String getDateCol() {
		return dateCol.get();
	}
	
	public void setDateCol(String dateCol) {
		this.dateCol.set(dateCol);
	}

}
