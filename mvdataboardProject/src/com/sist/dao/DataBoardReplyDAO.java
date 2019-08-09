package com.sist.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DataBoardReplyDAO {
	private Connection conn;			//소켓 첨부 (연결기기)  >> TCP(값을 잃어버리지 않는다.)
	private PreparedStatement ps;		//InputStream(값을 읽는다) OutputStream(sql 문장 전송)
	private final String URL="jdbc:oracle:thin:@localhost:1521:ORCL";
	//	new 소켓("ip",포트번호) _포트번호 범위는 0~65535 : 0~1024 => 1521,1433,7000,8080,(몽고디비)27017_ : DB이름
	
	//드라이버 등록
	/*
	 * 	thin driver : 연결만 해줌
	 * 	oci driver : 데이터를 가지고있다.(버퍼같이.)
	 * 
	 */
	public DataBoardReplyDAO(){
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");	//리플렉션
			//클래스 이름으로 읽어서 클래스를 제어해주는 역할 >> Spring
			//메모리 할당 == new를 사용하지 않고 메모리 할당
			//=> 반드시 패키지명부터 클래스명까지 
			//<jsp:useBean class="패키지.이름을.쓰고.클래스명" id="">
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//연결
	public void getConnection(){
		try {
			conn=DriverManager.getConnection(URL, "scott", "tiger");
			//오라클 conn scott/tiger
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//해제
	public void disConnection(){
		try {
			if(ps!=null) ps.close();
			if(conn!=null) conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<DataBoardReplyVO> reply(int bno){
		List<DataBoardReplyVO> list=new ArrayList<DataBoardReplyVO>();
		try {
			getConnection();
			String sql="SELECT no,bno,id,name,msg,to_char(regdate,'yyyy-mm-dd hh24:mi:ss') "
					+ "from datareply "
					+ "where bno=? "
					+ "ORDER BY no DESC";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, bno);
			ResultSet rs=ps.executeQuery();
			while(rs.next()){
				DataBoardReplyVO vo=new DataBoardReplyVO();
				vo.setNo(rs.getInt(1));
				vo.setBno(rs.getInt(2));
				vo.setId(rs.getString(3));
				vo.setName(rs.getString(4));
				vo.setMsg(rs.getString(5));
				vo.setDbday(rs.getString(6));
				list.add(vo);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
		
		return list;
	}
	
	
	
	
	public void replyDataInsert(DataBoardReplyVO vo){
		try {
			getConnection();
			String sql="INSERT INTO datareply VALUES((SELECT NVL(MAX(no)+1,1) From datareply),?,?,?,?,SYSDATE)";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, vo.getBno());
			ps.setString(2, vo.getId());
			ps.setString(3, vo.getName());
			ps.setString(4, vo.getMsg());
			
			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
		
	}
	
	public void replyDataDelete(int no){
		try {
			getConnection();
			String sql="DELETE FROM datareply WHERE no=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, no);
			
			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
	}
	
	public void replyDataupdate(int no, String msg){
		
		try {
			getConnection();
			String sql="UPDATE datareply SET msg=? WHERE no=?";
			ps=conn.prepareStatement(sql);
			ps.setString(1, msg);
			ps.setInt(2, no);
			
			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}

	}
}
