package dto;

public class ShopInfoDTO {
	
	private String shop_no;
	private String owner_name;
	private String shop_tel;
	private String owner_tel;
	private String pw;
	private String shop_address;
	
	public ShopInfoDTO() {
		// TODO Auto-generated constructor stub
	}	
		
	
	public ShopInfoDTO(String shop_no, String owner_name, String shop_tel, String owner_tel, String pw, String shop_address) {
		super();
		this.shop_no = shop_no;
		this.owner_name = owner_name;
		this.shop_tel = shop_tel;
		this.owner_tel = owner_tel;
		this.pw = pw;
		this.shop_address = shop_address;
		
	}
	
	public ShopInfoDTO(String shop_no, String owner_name, String shop_tel, String owner_tel, String shop_address) {
		super();
		this.shop_no = shop_no;
		this.owner_name = owner_name;
		this.shop_tel = shop_tel;
		this.owner_tel = owner_tel;
		this.shop_address = shop_address;
	}


	public String getShop_no() {
		return shop_no;
	}


	public void setShop_no(String shop_no) {
		this.shop_no = shop_no;
	}


	public String getOwner_name() {
		return owner_name;
	}


	public void setOwner_name(String owner_name) {
		this.owner_name = owner_name;
	}


	public String getShop_tel() {
		return shop_tel;
	}


	public void setShop_tel(String shop_tel) {
		this.shop_tel = shop_tel;
	}


	public String getOwner_tel() {
		return owner_tel;
	}


	public void setOwner_tel(String owner_tel) {
		this.owner_tel = owner_tel;
	}


	public String getPw() {
		return pw;
	}


	public void setPw(String pw) {
		this.pw = pw;
	}


	public String getShop_address() {
		return shop_address;
	}


	public void setShop_address(String shop_address) {
		this.shop_address = shop_address;
	}
	

	
}
