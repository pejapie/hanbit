package hanbit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FreeBoardDAO {
	DBConnect dbconnect = null;
	String sql="";
	
	public FreeBoardDAO() {
		dbconnect = new DBConnect();
	}
	
	public int count() {
		Connection con = dbconnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int cnt = 0;
		
		try {
			sql = "SELECT COUNT(*) FROM board1";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				cnt=rs.getInt(1);
			}
			
		}catch(Exception e) {
			
		}finally {
			DBClose.close(con,pstmt,rs);
		}
		return cnt;
	}
	
	public String pasing(String data) {
		try {
			data = new String(data.getBytes("8859_1"), "euc-kr");
		}catch (Exception e){ }
		return data;
	}
	
	public ArrayList<FreeBoardVO> getMemberList() {
		Connection con = dbconnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		ArrayList<FreeBoardVO> alist = new ArrayList<FreeBoardVO>();
		
		try {
			
			sql = "SELECT id,writer,pass,write_date,title,content,hit,good from board1 order by id desc";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				FreeBoardVO vo = new FreeBoardVO();
				vo.setId(rs.getInt(1));
				vo.setWriter(rs.getString(2));
				vo.setPass(rs.getString(3));
				String year = rs.getString(4).substring(0,10);
				vo.setWrite_date(year);
				vo.setTitle(rs.getString(5));
				vo.setContent(rs.getString(6));
				vo.setHit(rs.getInt(7));
				vo.setGood(rs.getInt(8));
				alist.add(vo);
			}
			
		}catch(Exception e) {
			
		}finally {
			DBClose.close(con,pstmt,rs);
		}
		return alist;
	}
	
	public int getMax() {
		Connection con = dbconnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int max = 0;
		
		try {
			sql = "SELECT MAX(NUM) FROM board1";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				max=rs.getInt(1);
			}
			
		}catch(Exception e) {
			
		}finally {
			DBClose.close(con,pstmt,rs);
		}
		return max;
	}
	
	public void insertWrite(FreeBoardVO vo) {
		Connection con = dbconnect.getConnection();
		PreparedStatement pstmt = null;
		
		try {
			sql = "INSERT INTO board1(id,writer,pass,write_date,title,content,hit,good) "
					+ "VALUES(DEFAULT,?,?,?,?,?,?,?)";
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, pasing(vo.getWriter()));
			pstmt.setString(2, pasing(vo.getPass()));
			pstmt.setString(3, pasing(vo.getWrite_date()));
			// check time function
//			Date date = new Date();
//			SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd"); 
//			String year = (String)simpleDate.format(date);
			pstmt.setString(4, pasing(vo.getTitle()));
			pstmt.setString(5, pasing(vo.getContent()));			
			pstmt.setInt(6, vo.getHit());
			pstmt.setInt(7, vo.getGood());
			
			pstmt.execute();
		}catch(Exception e) {
			
		}finally {
			DBClose.close(con,pstmt);
		}
	}
	
	public FreeBoardVO getView(int idx) {
		Connection con = dbconnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		FreeBoardVO vo = null;
		
		try {
			sql = "SELECT id,writer,pass,write_date,title,content,hit,good FROM board1 WHERE NUM=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, idx);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				vo = new FreeBoardVO();
				vo.setId(rs.getInt(1));
				vo.setWriter(rs.getString(2));
				vo.setPass(rs.getString(3));
				vo.setWrite_date(rs.getString(4));
				vo.setTitle(rs.getString(5));
				vo.setContent(rs.getString(6));
				vo.setHit(rs.getInt(7));
				vo.setGood(rs.getInt(8));
			}
			
		}catch(Exception e) {
			
		}finally {
			DBClose.close(con,pstmt,rs);
		}
		return vo;
	}
	
	public void UpdateHit(int idx) {
		Connection con = dbconnect.getConnection();
		PreparedStatement pstmt = null;
		
		try {
			sql = "UPDATE board1 SET HIT=HIT+1 where NUM=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, idx);
			pstmt.executeUpdate();
			
		}catch(Exception e) {
			
		}finally {
			DBClose.close(con,pstmt);
		}
	}
	
	public boolean checkPassword(FreeBoardVO vo, int idx) {
		Connection con = dbconnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean ch = false;
		
		try {
			sql = "SELECT id FROM board1 where id=? and pass=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, idx);
			pstmt.setString(2, vo.getPass());
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				ch = true;
			} else {
				ch = false;
			}
			
		}catch(Exception e) {
			
		}finally {
			DBClose.close(con,pstmt,rs);
		}
		return ch;
	}
	
	public void delete(int idx) {
		Connection con = dbconnect.getConnection();
		PreparedStatement pstmt = null;
		
		try {
			sql = "DELETE FROM board1 WHERE id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, idx);
			pstmt.executeUpdate();
			
		}catch(Exception e) {
			
		}finally {
			DBClose.close(con,pstmt);
		}
	}
	
	public void modify(FreeBoardVO vo, int idx) {
		Connection con = dbconnect.getConnection();
		PreparedStatement pstmt = null;
		
		try {
			sql = "UPDATE board1 SET title=?, content=? where id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, pasing(vo.getTitle()));
			pstmt.setString(2, pasing(vo.getContent()));
			pstmt.setInt(3, idx);
			pstmt.executeUpdate();
			
		}catch(Exception e) {
			
		}finally {
			DBClose.close(con,pstmt);
		}
	}
	
/*	public void UpdateStep(int ref, int step) {
		Connection con = dbconnect.getConnection();
		PreparedStatement pstmt = null;
		
		try {
			sql = "UPDATE board1 SET STEP=STEP+1 where REF=? and STEP>?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, ref);
			pstmt.setInt(2, step);
			pstmt.executeUpdate();
			
		}catch(Exception e) {
			
		}finally {
			DBClose.close(con,pstmt);
		}
	}
	
	public void insertReply(FreeBoardVO vo, int ref, int indent, int step) {
		Connection con = dbconnect.getConnection();
		PreparedStatement pstmt = null;
		
		try {
			sql = "INSERT INTO board1(USERNAME, PASSWORD, TITLE, MEMO, REF, INDENT, STEP) "+
					"VALUES(?,?,?,?,?,?,?)";
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, pasing(vo.getName()));
			pstmt.setString(2, pasing(vo.getPassword()));
			pstmt.setString(3, pasing(vo.getTitle()));
			pstmt.setString(4, pasing(vo.getMemo()));
			pstmt.setInt(5, ref);
			pstmt.setInt(6, indent+1);
			pstmt.setInt(7, step+1);
			
			pstmt.execute();
		}catch(Exception e) {
			
		}finally {
			DBClose.close(con,pstmt);
		}
	} */
}
