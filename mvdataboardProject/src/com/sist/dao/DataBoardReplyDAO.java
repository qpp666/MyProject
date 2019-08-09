package com.sist.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DataBoardReplyDAO {
	private Connection conn;			//���� ÷�� (������)  >> TCP(���� �Ҿ������ �ʴ´�.)
	private PreparedStatement ps;		//InputStream(���� �д´�) OutputStream(sql ���� ����)
	private final String URL="jdbc:oracle:thin:@localhost:1521:ORCL";
	//	new ����("ip",��Ʈ��ȣ) _��Ʈ��ȣ ������ 0~65535 : 0~1024 => 1521,1433,7000,8080,(������)27017_ : DB�̸�
	
	//����̹� ���
	/*
	 * 	thin driver : ���Ḹ ����
	 * 	oci driver : �����͸� �������ִ�.(���۰���.)
	 * 
	 */
	public DataBoardReplyDAO(){
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");	//���÷���
			//Ŭ���� �̸����� �о Ŭ������ �������ִ� ���� >> Spring
			//�޸� �Ҵ� == new�� ������� �ʰ� �޸� �Ҵ�
			//=> �ݵ�� ��Ű������� Ŭ��������� 
			//<jsp:useBean class="��Ű��.�̸���.����.Ŭ������" id="">
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//����
	public void getConnection(){
		try {
			conn=DriverManager.getConnection(URL, "scott", "tiger");
			//����Ŭ conn scott/tiger
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//����
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
