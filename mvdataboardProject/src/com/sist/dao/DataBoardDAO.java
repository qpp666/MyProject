package com.sist.dao;
import java.util.*;
import java.sql.*;

public class DataBoardDAO {
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
	public DataBoardDAO(){
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
	
	//기능
	public List<DataBoardVO> boardlistData(int page){
		List<DataBoardVO> list=new ArrayList<DataBoardVO>();
		try {
			getConnection();
			String sql="SELECT no,subject,name,regdate,hit,num "
					+ "FROM (SELECT no,subject,name,regdate,hit,rownum as num "
					+ "FROM (SELECT no,subject,name,regdate,hit "
					+ "FROM databoard ORDER BY no DESC)) "
					+ "WHERE num BETWEEN ? AND ?";
			
			/*
			 * 	view = 가상테이블
			 * 		단일뷰 : 테이블 한개 연결.
			 * 		복합뷰 : 테이블 여러개 연결(subquery)
			 * 		인라인뷰 : 
			 * 
			 */
			ps=conn.prepareStatement(sql);
			//? 값을 채워야됨
			int rowsize=10;
			int start=(rowsize*page)-(rowsize-1);
			int end=(rowsize*page);
			ps.setInt(1, start);
			ps.setInt(2, end);
			
			//결과값 긁어오기
			ResultSet rs=ps.executeQuery();
			while(rs.next()){
				DataBoardVO vo=new DataBoardVO();
				vo.setNo(rs.getInt(1));
				vo.setSubject(rs.getString(2));
				vo.setName(rs.getString(3));
				vo.setRegdate(rs.getDate(4));
				vo.setHit(rs.getInt(5));
				
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
	
	public void boardDataInsert(DataBoardVO vo){
		try {
			getConnection();
			String sql="INSERT INTO databoard(no,name,subject,content,pwd,filename,filesize) "
					+ "VALUES((SELECT NVL(MAX(no)+1,1) FROM databoard),?,?,?,?,?,?)";
			ps=conn.prepareStatement(sql);
			ps.setString(1, vo.getName());
			ps.setString(2, vo.getSubject());
			ps.setString(3, vo.getContent());
			ps.setString(4, vo.getPwd());
			ps.setString(5, vo.getFilename());
			ps.setInt(6, vo.getFilesize());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
	}
	
	public DataBoardVO dataBoardDetail(int no){
		DataBoardVO vo = new DataBoardVO();
		try {
			getConnection();
			String sql="UPDATE databoard SET hit=hit+1 WHERE no=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, no);
			ps.executeUpdate();
			
			sql="SELECT no,name,subject,content,regdate,hit,filename,filesize FROM databoard WHERE no=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, no);
			ResultSet rs=ps.executeQuery();
			rs.next();
			vo.setNo(rs.getInt(1));
			vo.setName(rs.getString(2));
			vo.setSubject(rs.getString(3));
			vo.setContent(rs.getString(4));
			vo.setRegdate(rs.getDate(5));
			vo.setHit(rs.getInt(6));
			vo.setFilename(rs.getString(7));
			vo.setFilesize(rs.getInt(8));
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
		return vo;
	}
	
	public int dataTotalPage(){
		int total=0;
		try {
			getConnection();
			String sql="SELECT CEIL(COUNT(*)/10.0) FROM databoard";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			rs.next();
			total=rs.getInt(1);
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
		return total;
	}
	
	
	public DataBoardVO dataBoardUpdate(int no){
		DataBoardVO vo = new DataBoardVO();
		try {
			getConnection();	
			String sql="SELECT no,name,subject,content "
					+ "FROM databoard WHERE no=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, no);
			ResultSet rs=ps.executeQuery();
			rs.next();
			
			vo.setNo(rs.getInt(1));
			vo.setName(rs.getString(2));
			vo.setSubject(rs.getString(3));
			vo.setContent(rs.getString(4));
			
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
		return vo;
	}
	
	public boolean dataUpdate(int no,String name,String subject,String content, int pwd){
		boolean bcheck=false;
		try {
			
			
			getConnection();
			String sql="SELECT pwd FROM databoard WHERE no=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, no);
			ResultSet rs=ps.executeQuery();
			rs.next();
			DataBoardVO vo = new DataBoardVO();
			vo.setPwd(rs.getString(1));
			rs.close();
			if(pwd==Integer.parseInt(vo.getPwd())){
				
				bcheck=true;
				sql="UPDATE databoard SET name=?,subject=?,content=? WHERE no=?";
				ps=conn.prepareStatement(sql);
				
				ps.setString(1, name);
				ps.setString(2, subject);
				ps.setString(3, content);
				ps.setInt(4, no);
				ps.executeUpdate();
			}						
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
		return bcheck;
	}
	
	public String islogin(String id, String pwd){
		String result="";
			try{
				getConnection();
				String sql="SELECT count(*) FROM member WHERE id=?";
				ps=conn.prepareStatement(sql);
				ps.setString(1, id);
				ResultSet rs=ps.executeQuery();
				rs.next();
				int count=rs.getInt(1);
				rs.close();
				
				if(count==0){
					result="NOID";
				}else if(count!=0){
					sql="SELECT pwd,name FROM member WHERE id=?";
					ps=conn.prepareStatement(sql);
					ps.setString(1, id);
					rs=ps.executeQuery();
					rs.next();
					String db_pwd=rs.getString(1);
					String name=rs.getString(2);
					rs.close();
					
					if(db_pwd.equals(pwd)){
						result=name;
					}else{
						result="NOPWD";
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				disConnection();
			}
			
			
		return result;
	}
	
	public DataBoardVO boardfileInfo(int no){
		DataBoardVO vo=new DataBoardVO();
				
		try {
			getConnection();
			String sql="SELECT filename,filesize FROM databoard WHERE no=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, no);
			ResultSet rs=ps.executeQuery();
			rs.next();
			vo.setFilename(rs.getString(1));
			vo.setFilesize(rs.getInt(2));
			rs.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
				
		return vo;
	}
	
	
	public boolean boardDel(int no, String pwd){
		boolean bCheck=false;
		try {
			getConnection();
			String sql="SELECT pwd FROM databoard WHERE no=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, no);
			ResultSet rs=ps.executeQuery();
			rs.next();
			String db_Pwd=rs.getString(1);
			rs.close();
			
			if(db_Pwd.equals(pwd)){
				bCheck=true;
				sql="SELECT COUNT(*) FROM datareply WHERE bno=?";
				ps=conn.prepareStatement(sql);
				ps.setInt(1, no);
				rs=ps.executeQuery();
				rs.next();
				int count=rs.getInt(1);
				rs.close();
				
				if(count!=0){
					sql="DELETE FROM datareply WHERE bno=?";
					ps=conn.prepareStatement(sql);
					ps.setInt(1, no);
					ps.executeUpdate();
				}
				
				sql="DELETE FROM databoard WHERE no=?";
				ps=conn.prepareStatement(sql);
				ps.setInt(1, no);
				ps.executeUpdate();
				
			}else{
				bCheck=false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
		return bCheck;
	}
}
