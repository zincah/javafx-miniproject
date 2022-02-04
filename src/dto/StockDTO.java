package dto;

import java.util.Date;

public class StockDTO {

	private int seq;
	private String item_no;
	private int quantity;
	private int price;
	private String shop_no;
	private Date day;
	
	public StockDTO() {}
	
	public StockDTO(String item_no, int quantity, int price, String shop_no) {
		super();
		this.item_no = item_no;
		this.quantity = quantity;
		this.price = price;
		this.shop_no = shop_no;
	}

	public StockDTO(int seq, String item_no, int quantity, int price, String shop_no, Date day) {
		super();
		this.seq = seq;
		this.item_no = item_no;
		this.quantity = quantity;
		this.price = price;
		this.shop_no = shop_no;
		this.day = day;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getItem_no() {
		return item_no;
	}

	public void setItem_no(String item_no) {
		this.item_no = item_no;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public int getPrice() {
		return price;
	}


	public void setPrice(int price) {
		this.price = price;
	}


	public String getShop_no() {
		return shop_no;
	}

	public void setShop_no(String shop_no) {
		this.shop_no = shop_no;
	}


	public Date getDay() {
		return day;
	}


	public void setDay(Date day) {
		this.day = day;
	}
	
	
	
	
}
