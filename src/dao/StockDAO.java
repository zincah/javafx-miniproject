package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import dto.StockDTO;

public class StockDAO {
	
	private static StockDAO instance = new StockDAO();
	
	public StockDAO() {}
	
	public static StockDAO getInstance() {return instance;}
	
	
	// 데이터 생성
	public int insert(StockDTO dto) {
		
		int result = 0;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		String query = "insert into stock values (stock_seq.nextval,?,?,?,?,sysdate)";
		
		try {
			con = ConnUtil.getConnection();
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, dto.getItem_no());
			pstmt.setInt(2, dto.getQuantity());
			pstmt.setInt(3, dto.getPrice());
			pstmt.setString(4, dto.getShop_no());
			result = pstmt.executeUpdate();
			
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt!=null) {pstmt.close();}
				if(con!=null) {con.close();}
			}catch(SQLException e) {}
		}
		
		return result;
	}
	
	
	// 데이터 불러오기
	public ArrayList<StockDTO> getItems() {
		
		ArrayList<StockDTO> itemList = new ArrayList<>();
		
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		String query = "select * from stock order by seq desc";
		
		try {
			con = ConnUtil.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				int seq = rs.getInt("seq");
				String item_no = rs.getString("item_no");
				int quantity = rs.getInt("quantity");
				int price = rs.getInt("price");
				String shop_no = rs.getString("shop_no");
				Date day = rs.getDate("day");
				// util date -> 연산을 할 수 있음
				// sql date -> 연산이 안됨
				
				StockDTO dto = new StockDTO(seq, item_no, quantity, price, shop_no, day);
				itemList.add(dto);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) {rs.close();}
				if(stmt!=null) {stmt.close();}
				if(con!=null) {con.close();}
			}catch(SQLException e) {}
		}
		
		return itemList;
	}
	
	public int delete(int seq) {
		int result = 0;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		String query = "delete from stock where seq=?";
		
		try {
			con = ConnUtil.getConnection();
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, seq);
			
			result = pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null) {pstmt.close();}
				if(con != null) {con.close();}
			}catch(SQLException e) {}
		}
		
		
		return result;
	}
	
	public ArrayList<StockDTO> sortItems(){
		ArrayList<StockDTO> list = new ArrayList<>();
		
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		String query = "select item_no, sum(quantity), max(price), shop_no "
				+ "from stock "
				+ "group by item_no, shop_no "
				+ "order by shop_no asc, item_no";
		
		try {
			con = ConnUtil.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				String item_no = rs.getString("item_no");
				int quantity = rs.getInt("sum(quantity)");
				int price = rs.getInt("max(price)");
				String shop_no = rs.getString("shop_no");
				
				StockDTO dto = new StockDTO(item_no, quantity, price, shop_no);
				list.add(dto);

			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) {rs.close();}
				if(stmt!=null) {stmt.close();}
				if(con!=null) {con.close();}
			}catch(SQLException e) {}
		}
		
		return list;
	}

	//table 
	/*
	 * select item_no, sum(quantity), max(price)
from stock
group by item_no
order by item_no asc; -- all store
	 */
	
	// user 재고
	public ArrayList<StockDTO> userStockList(String num){
		
		ArrayList<StockDTO> list = new ArrayList<>();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		
		String query = "select item_no, sum(quantity), max(price), shop_no "
				+ "from stock "
				+ "where shop_no=? "
				+ "group by item_no, shop_no";
		
		
		try {
			con = ConnUtil.getConnection();
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, num);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String item_no = rs.getString("item_no");
				int quantity = rs.getInt("sum(quantity)");
				int price = rs.getInt("max(price)");
				String shop_no = rs.getString("shop_no");
				
				StockDTO dto = new StockDTO(item_no, quantity, price, shop_no);
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
	
	public StockDTO findItem(String item, String shop) {
		StockDTO dto = new StockDTO();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String query = "select item_no, sum(quantity), max(price), shop_no "
				+ "from stock "
				+ "where item_no=? and shop_no=? "
				+ "group by item_no, shop_no "
				+ "having sum(quantity) >= 1";
		
		try {
			con = ConnUtil.getConnection();
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, item);
			pstmt.setString(2, shop);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String item_no = rs.getString("item_no");
				int quantity = rs.getInt("sum(quantity)");
				int price = rs.getInt("max(price)");
				String shop_no = rs.getString("shop_no");
				
				dto = new StockDTO(item_no, quantity, price, shop_no);
			}
			
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		
		return dto;
	}

}
