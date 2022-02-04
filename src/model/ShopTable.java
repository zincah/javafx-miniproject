package model;

import javafx.beans.property.SimpleStringProperty;

public class ShopTable {
	
	private SimpleStringProperty shopNumCol;
	private SimpleStringProperty nameCol;
	private SimpleStringProperty shopTelCol;
	private SimpleStringProperty ownerTelCol;
	private SimpleStringProperty pwCol;
	private SimpleStringProperty addressCol;
	
	public ShopTable() {}

	public ShopTable(String shopNumCol, String nameCol, String shopTelCol, String ownerTelCol, String pwCol, String addressCol) {
		
		super();
		this.shopNumCol = new SimpleStringProperty(shopNumCol);
		this.nameCol = new SimpleStringProperty(nameCol);
		this.shopTelCol = new SimpleStringProperty(shopTelCol);
		this.ownerTelCol = new SimpleStringProperty(ownerTelCol);
		this.pwCol = new SimpleStringProperty(pwCol);
		this.addressCol = new SimpleStringProperty(addressCol);
	}

	public String getShopNumCol() {
		return shopNumCol.get();
	}

	public void setShopNumCol(String shopNumCol) {
		this.shopNumCol.set(shopNumCol);
	}

	public String getNameCol() {
		return nameCol.get();
	}

	public void setNameCol(String nameCol) {
		this.nameCol.set(nameCol);
	}

	public String getShopTelCol() {
		return shopTelCol.get();
	}

	public void setShopTelCol(String shopTelCol) {
		this.shopTelCol.set(shopTelCol);
	}

	public String getOwnerTelCol() {
		return ownerTelCol.get();
	}

	public void setOwnerTelCol(String ownerTelCol) {
		this.ownerTelCol.set(ownerTelCol);
	}

	public String getPwCol() {
		return pwCol.get();
	}

	public void setPwCol(String pwCol) {
		this.pwCol.set(pwCol);
	}

	public String getAddressCol() {
		return addressCol.get();
	}

	public void setAddressCol(String addressCol) {
		this.addressCol.set(addressCol);
	}
	
	
}
