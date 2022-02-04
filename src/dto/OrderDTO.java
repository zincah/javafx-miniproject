package dto;

import java.util.Date;

public class OrderDTO {
	
	private int seq;
	private int order_no;
	private String item_no;
	private int quantity;
	private int price;
	private String shop_no;
	private Date day;
	
	public OrderDTO() {}

	public OrderDTO(int seq, int order_no, String item_no, int quantity, int price, String shop_no, Date day) {
		super();
		this.seq = seq;
		this.order_no = order_no;
		this.item_no = item_no;
		this.quantity = quantity;
		this.price = price;
		this.shop_no = shop_no;
		this.day = day;
	}

	public OrderDTO(int order_no, String item_no, int quantity, int price, String shop_no) {
		super();
		this.order_no = order_no;
		this.item_no = item_no;
		this.quantity = quantity;
		this.price = price;
		this.shop_no = shop_no;
	}
	
	public OrderDTO(String item_no, Date day, int quantity, int price) {
		this.item_no = item_no;
		this.quantity = quantity;
		this.price = price;
		this.day = day;
	}
	
	public OrderDTO(String shop_no, int quantity, int price) {
		this.shop_no = shop_no;
		this.quantity = quantity;
		this.price = price;
	}
	

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getOrder_no() {
		return order_no;
	}

	public void setOrder_no(int order_no) {
		this.order_no = order_no;
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
