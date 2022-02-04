package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import controller.LoginController;
import dto.OrderDTO;

public class OrderDAO {

	private static OrderDAO instance = new OrderDAO();
	
	public OrderDAO() {}
	
	public static OrderDAO getInstance() {return instance;}
	
	public int insert(OrderDTO dto) {
		int result = 0;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		String query = "insert into orders values (orders_seq.nextval,?,?,?,?,?,sysdate)";
		
		try {
			con = ConnUtil.getConnection();
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, dto.getOrder_no());
			pstmt.setString(2, dto.getItem_no());
			pstmt.setInt(3, dto.getQuantity());
			pstmt.setInt(4, dto.getPrice());
			pstmt.setString(5, dto.getShop_no());
			result = pstmt.executeUpdate();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public OrderDTO checkDate(String shop_no) {
		
		OrderDTO dto = null;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String query = "select * from (select * from orders order by seq desc) "
				+ "where shop_no=? "
				+ "and rownum=1";
		
		try {
			con = ConnUtil.getConnection();
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, shop_no);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				int seq = rs.getInt("seq");
				int order_no = rs.getInt("order_no");
				String item_no = rs.getString("item_no");
				int quantity = rs.getInt("quantity");
				int price = rs.getInt("price");
				shop_no = rs.getString("shop_no");
				Date day = rs.getDate("day");
				dto = new OrderDTO(seq, order_no, item_no, quantity, price, shop_no, day);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return dto;
	}
	
	public ArrayList<OrderDTO> dayList(String date, String shopNum){
		
		ArrayList<OrderDTO> list = new ArrayList<>();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String query = "select * from orders where to_char(day, 'yyyy-MM-dd')=? "
				+ "and shop_no=? order by seq";
		
		try {
			con = ConnUtil.getConnection();
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, date);
			pstmt.setString(2, shopNum);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int seq = rs.getInt("seq");
				int order_no = rs.getInt("order_no");
				String item_no = rs.getString("item_no");
				int quantity = rs.getInt("quantity");
				int price = rs.getInt("price");
				String shop_no = rs.getString("shop_no");
				Date day = rs.getDate("day");
				
				OrderDTO dto = new OrderDTO(seq, order_no, item_no, quantity, price, shop_no, day);
				list.add(dto);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) {rs.close();}
				if(pstmt!=null) {pstmt.close();}
				if(con!=null) {con.close();}
			}catch(SQLException e) {}
		}
		
		
		return list;
	}
	
	public ArrayList<OrderDTO> sales(String shop_no, String firstDay, String lastDay){
		ArrayList<OrderDTO> list = new ArrayList<>();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String query = "select * from orders where shop_no=? "
				+ "and to_char(day, 'yyyy-MM-dd') "
				+ "between ? and ? "
				+ "order by seq";

		try {
			con = ConnUtil.getConnection();
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, shop_no);
			pstmt.setString(2, firstDay);
			pstmt.setString(3, lastDay);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int seq = rs.getInt("seq");
				int order_no = rs.getInt("order_no");
				String item_no = rs.getString("item_no");
				int quantity = rs.getInt("quantity");
				int price = rs.getInt("price");
				shop_no = rs.getString("shop_no");
				Date day = rs.getDate("day");
				
				OrderDTO dto = new OrderDTO(seq, order_no, item_no, quantity, price, shop_no, day);
				list.add(dto);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) {rs.close();}
				if(pstmt!=null) {pstmt.close();}
				if(con!=null) {con.close();}
			}catch(SQLException e) {}
		}
		
		return list;
	}
	
	/*
	 * 	select to_char(day, 'yyyy-MM-dd'), max(order_no), 
		sum(quantity), sum(price) from orders
		group by to_char(day, 'yyyy-MM-dd');
	 * 
	 * 
	 */
	
	public ArrayList<OrderDTO> itemSoldList(String item_no, String shop_no, String firstDay, String lastDay ){
		ArrayList<OrderDTO> list = new ArrayList<>();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String query = "select day, quantity, price from orders "
				+ "where item_no=? "
				+ "and shop_no=? "
				+ "and to_char(day, 'yyyy-MM-dd') "
				+ "between ? and ? "
				+ "order by day desc";
		
		try {
			con = ConnUtil.getConnection();
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, item_no);
			pstmt.setString(2, shop_no);
			pstmt.setString(3, firstDay);
			pstmt.setString(4, lastDay);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Date day = rs.getDate("day");
				int quantity = rs.getInt("quantity");
				int price = rs.getInt("price");
				
				OrderDTO dto = new OrderDTO(item_no, day, quantity, price);
				list.add(dto);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) {rs.close();}
				if(pstmt!=null) {pstmt.close();}
				if(con!=null) {con.close();}
			}catch(SQLException e) {}
		}

		return list;
	}
	
	public ArrayList<OrderDTO> soldOfMonth(String shop_no, String firstDay, String lastDay){
		ArrayList<OrderDTO> list = new ArrayList<>();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String query = "select to_char(day, 'yyyy-MM-dd') as day, sum(price), max(order_no), sum(quantity) "
				+ "from orders where shop_no=? "
				+ "and to_char(day, 'yyyy-MM-dd') "
				+ "between ? and ? "
				+ "group by to_char(day, 'yyyy-MM-dd') "
				+ "order by day asc";
		
		try {
			con = ConnUtil.getConnection();
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, shop_no);
			pstmt.setString(2, firstDay);
			pstmt.setString(3, lastDay);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Date day = rs.getDate("day");
				int price = rs.getInt("sum(price)");
				int order_no = rs.getInt("max(order_no)");
				int quantity = rs.getInt("sum(quantity)");
				
				OrderDTO dto = new OrderDTO(0, order_no, "item", quantity, price, shop_no, day);
				list.add(dto);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) {rs.close();}
				if(pstmt!=null) {pstmt.close();}
				if(con!=null) {con.close();}
			}catch(SQLException e) {}
		}
		
		return list;
	}
	
	public ArrayList<OrderDTO> getSales(String firstDay, String lastDay){
		ArrayList<OrderDTO> list = new ArrayList<>();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String query = "select shop_no, sum(quantity), sum(price) "
				+ "from orders "
				+ "where to_char(day,'yyyy-MM-dd') "
				+ "between ? and ? "
				+ "group by shop_no";
		
		try {
			con = ConnUtil.getConnection();
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, firstDay);
			pstmt.setString(2, lastDay);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String shop_no = rs.getString("shop_no");
				int quantity = rs.getInt("sum(quantity)");
				int price = rs.getInt("sum(price)");
				
				OrderDTO dto = new OrderDTO(shop_no, quantity, price);
				list.add(dto);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) {rs.close();}
				if(pstmt!=null) {pstmt.close();}
				if(con!=null) {con.close();}
			}catch(SQLException e) {}
		}
		return list;
	}
	
	public ArrayList<OrderDTO> getItemSales(String item_no, String firstDay, String lastDay){
		ArrayList<OrderDTO> list = new ArrayList<>();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String query = "select shop_no, sum(quantity), sum(price) "
				+ "from orders "
				+ "where item_no=? "
				+ "and to_char(day, 'yyyy-MM-dd') "
				+ "between ? and ? "
				+ "group by shop_no "
				+ "order by shop_no";
		
		try {
			con = ConnUtil.getConnection();
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, item_no);
			pstmt.setString(2, firstDay);
			pstmt.setString(3, lastDay);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String shop_no = rs.getString("shop_no");
				int quantity = rs.getInt("sum(quantity)");
				int price = rs.getInt("sum(price)");
				
				OrderDTO dto = new OrderDTO(shop_no, quantity, price);
				list.add(dto);		
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) {rs.close();}
				if(pstmt!=null) {pstmt.close();}
				if(con!=null) {con.close();}
			}catch(SQLException e) {}
		}
		
		return list;
	}
	
	public ArrayList<OrderDTO> makeRefundList(String shop_no, String first, String last) {
		ArrayList<OrderDTO> list = new ArrayList<>();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String query = "select item_no, sum(quantity), sum(price) from orders "
				+ "where shop_no=? "
				+ "and to_char(day, 'yyyy-MM-dd') "
				+ "between ? and ? "
				+ "group by item_no";
		
		
		try {
			con = ConnUtil.getConnection();
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, shop_no);
			pstmt.setString(2, first);
			pstmt.setString(3, last);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String item_no = rs.getString("item_no");
				int quantity = rs.getInt("sum(quantity)");
				int price = rs.getInt("sum(price)");
				Date day = new Date();
				OrderDTO dto = new OrderDTO(item_no, day, quantity, price);
				list.add(dto);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) {rs.close();}
				if(pstmt!=null) {pstmt.close();}
				if(con!=null) {con.close();}
			}catch(SQLException e) {}
		}
		
		
		
		return list;
	}
}
