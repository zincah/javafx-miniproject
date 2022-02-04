package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dto.ShopInfoDTO;

public class ShopInfoDAO {
	
	private static ShopInfoDAO instance = new ShopInfoDAO();
	
	public ShopInfoDAO() {}
	
	public static ShopInfoDAO getInstance() {return instance;}
	
	
	
	public int insert(ShopInfoDTO dto) {
		
		int result = 0;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		String query = "insert into shop_info values (?,?,?,?,?,?)";
		
		try {
			con = ConnUtil.getConnection();
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, dto.getShop_no());
			pstmt.setString(2, dto.getOwner_name());
			pstmt.setString(3, dto.getShop_tel());
			pstmt.setString(4, dto.getOwner_tel());
			pstmt.setString(5, dto.getPw());
			pstmt.setString(6, dto.getShop_address());
			
			result = pstmt.executeUpdate();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt!=null) {pstmt.close();}
				if(con!=null) {con.close();}
			}catch(SQLException e2) {}
		}
			
	
		return result;
	}
	
	
	public int checkShopNum(String shop_no) {
		int result = 0;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		String query = "select shop_no from shop_info where shop_no=?";
		
		try {
			con = ConnUtil.getConnection();
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, shop_no);
			result = pstmt.executeUpdate();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt!=null) {pstmt.close();}
				if(con!=null) {con.close();}
			}catch(Exception e) {}
		}

		return result;
	}
	
	public boolean checkUser(String shop_no, String pw) {
		boolean check = false;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String query = "select shop_no, pw from shop_info where shop_no=?";
		
		try {
			con = ConnUtil.getConnection();
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, shop_no);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String dbPw = rs.getString("pw");
				if(dbPw.equals(pw)) {
					System.out.println("로그인 성공");
					check = true;
				}else {
					System.out.println("비밀번호 오류");
					check = false;
				}
			}else {
				System.out.println("잘못된 아이디");
				check = false;
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt!=null) {pstmt.close();}
				if(con!=null) {con.close();}
			}catch(Exception e) {}
		}
		
		return check;
	}
	
	public ArrayList<String> shopList(){
		
		ArrayList<String> shopNolist = new ArrayList<>();
		
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		String query = "select shop_no from shop_info";
		
		try {
			con = ConnUtil.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				String shopNo = rs.getString("shop_no");
				shopNolist.add(shopNo);
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
		
		return shopNolist;
	}
	
	
	public ShopInfoDTO findShop(String shop_no) {
		
		ShopInfoDTO dto = new ShopInfoDTO();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String query = "select * from shop_info where shop_no=?";
		
		try {
			con = ConnUtil.getConnection();
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, shop_no);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String owner_name = rs.getString("owner_name");
				String shop_tel = rs.getString("shop_tel");
				String owner_tel = rs.getString("owner_tel");
				String shop_address = rs.getString("shop_address");
				
				dto = new ShopInfoDTO(shop_no, owner_name, shop_tel, owner_tel, shop_address);
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
		
		
		return dto;
	}
	
	public ArrayList<ShopInfoDTO> shopInfoList(){
		ArrayList<ShopInfoDTO> list = new ArrayList<>();
		
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		String query = "select * from shop_info order by shop_no";
		
		try {
			con = ConnUtil.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				String shop_no = rs.getString("shop_no");
				String name = rs.getString("owner_name");
				String shop_tel = rs.getString("shop_tel");
				String owner_tel = rs.getString("owner_tel");
				String password = rs.getString("pw");
				String address = rs.getString("shop_address");
				
				ShopInfoDTO dto = new ShopInfoDTO(shop_no, name, shop_tel, owner_tel, password, address);
				list.add(dto);
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
		
		return list;
	}
	
	
	public int update(ShopInfoDTO dto) {
		int result = 0;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		String query = "update shop_info "
				+ "set owner_name=?, shop_tel=?, "
				+ "owner_tel=?, "
				+ "shop_address=? "
				+ "where shop_no=?";
		
		try {
			con = ConnUtil.getConnection();
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, dto.getOwner_name());
			pstmt.setString(2, dto.getShop_tel());
			pstmt.setString(3, dto.getOwner_tel());
			pstmt.setString(4, dto.getShop_address());
			pstmt.setString(5, dto.getShop_no());
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
	

}
