package com.sist.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.sist.db.ConnectionProvider;
import com.sist.vo.BookVO;


public class BookDAO {
	public static int pageSIZE = 5;
	//한화면에 보여줄 레코드의 수
	
	public static int totalRecord;
	//전체레코드 수
	
	public static int totalPage;
	//전체페이지 수
	
	
	public static BookDAO dao;

	private BookDAO() {
	}

	public static BookDAO getInstance() {
		if (dao == null) {
			dao = new BookDAO();
		}
		return dao;
	}

	//전체레코드 수를 반환하는 메소드		getTotalRecord
	public int getTotalRecord(String keyword, String searchColumn, String op) {
		int cnt = 0;
		String sql = "select count(*) from book ";
		if(keyword != null) {
			if(searchColumn.equals("price")) {
				sql += "where price "+op+" "+keyword;
			}else {
				sql += "where "+searchColumn+" like '%"+keyword+"%'";
			}
			
			
		}		
		try {
			Connection conn = ConnectionProvider.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()) {
				cnt = rs.getInt(1);
			}
			ConnectionProvider.close(conn, stmt, rs);
		}catch (Exception e) {
			System.out.println("예외발생:"+e.getMessage());
		}
		
		return cnt;
	}
	
	
	public ArrayList<BookVO> findAll(int pageNUM, String keyword, String searchColumn, String op) {
		totalRecord = getTotalRecord(keyword, searchColumn, op);
		totalPage = (int)Math.ceil(totalRecord/(double)pageSIZE);
		System.out.println("전체레코드수:"+totalRecord);
		System.out.println("전체페이지수:"+totalPage);
		
		int start = (pageNUM-1)*pageSIZE+1;
		int end = start+pageSIZE-1;
		if(end > totalRecord) {
			end  = totalRecord;
		}
		
		ArrayList<BookVO> list = new ArrayList<BookVO>();
		String sql = "select bookid, bookname, publisher, price "
				+ "from( "
				+ "select rownum n, bookid, bookname, publisher, price "
				+ "from (select * from book ";
		if(keyword != null) {
			if(searchColumn.equals("price")) {
				sql += "where price "+op+" "+keyword;
			}else {			
				sql += " where "+searchColumn+" like '%"+keyword+"%'";
			}
		}
		
		sql += " order by bookid)) a "
				+ "where a.n between ? and ?";
		try {
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				BookVO b = new BookVO();
				b.setBookid(rs.getInt(1));
				b.setBookname(rs.getString(2));
				b.setPublisher(rs.getString(3));
				b.setPrice(rs.getInt(4));
				list.add(b);
			}
			ConnectionProvider.close(conn, pstmt, rs);
		} catch (Exception e) {
			System.out.println("예외발생:" + e.getMessage());
		}
		return list;
	}
}
