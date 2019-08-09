package com.sist.model;

import java.util.List;
import java.net.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;		//cos ���Ͼ��ε��Ҷ� ���� ��Ű��
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.sist.dao.*;
import java.io.*;


public class Model {
	public void dataBoardModel(HttpServletRequest request){
		String page=request.getParameter("page");
		if(page==null){
			page="1";
		}
		int curpage=Integer.parseInt(page);
		
		
		DataBoardDAO dao=new DataBoardDAO();
		List<DataBoardVO> list=dao.boardlistData(curpage);
		int totalpage=dao.dataTotalPage();
		//jsp�� ����� ����
		request.setAttribute("curpage", curpage);
		request.setAttribute("list", list);
		request.setAttribute("totalpage", totalpage);
	}
	
	public void dataInsertModel(HttpServletRequest request, HttpServletResponse response){
		try {
			request.setCharacterEncoding("UTF-8");
			String path="c:\\upload";
			String enctype="UTF-8"; //�ѱ۸� ÷�������� ������ ������
			int size=100*1024*1024;	//100MB
			MultipartRequest mr=new MultipartRequest(request, path, size, enctype, new DefaultFileRenamePolicy());
			//new DefaultFileRenamePolicy() >> �������ϸ��ϰ�� upload�� �ڵ����� ������ �ȵǰ� ƨ�ܳ����µ� �̋� �ڿ� 1�� �������Ѽ� �������ִ� ����
			
			String name=mr.getParameter("name");
			String subject=mr.getParameter("subject");
			String content=mr.getParameter("content");
			String pwd=mr.getParameter("pwd");
			DataBoardVO vo=new DataBoardVO();
			vo.setName(name);
			vo.setSubject(subject);
			vo.setContent(content);
			vo.setPwd(pwd);
			
			String filename=mr.getOriginalFileName("upload");
			if(filename==null){
				vo.setFilename("");
				vo.setFilesize(0);
			}else{
				File file=new File(path+"\\"+filename);
				vo.setFilename(filename);
				vo.setFilesize((int)file.length());
			}
			DataBoardDAO dao=new DataBoardDAO();
			dao.boardDataInsert(vo);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	public void dataDetailModel(HttpServletRequest request){
		//detail.jsp?no${vo.no}
		String no = request.getParameter("no");
		String curpage=request.getParameter("page");
		//dao����
		DataBoardDAO dao=new DataBoardDAO();
		DataBoardVO vo=dao.dataBoardDetail(Integer.parseInt(no));
		request.setAttribute("vo", vo);
		request.setAttribute("curpage", curpage);
		
		//���
		DataBoardReplyDAO dao2=new DataBoardReplyDAO();
		List<DataBoardReplyVO> list=dao2.reply(Integer.parseInt(no));
		request.setAttribute("list", list);
		request.setAttribute("len", list.size());
		System.out.println(list.size());
		System.out.println("no:"+no);
	}
	
	//�ٿ�ε�
	public void download(HttpServletRequest request, HttpServletResponse response){

	}
	public void dataupdatemodel(HttpServletRequest request){
		String no=request.getParameter("no");
		String page=request.getParameter("page");
		//DAO ����
		DataBoardDAO dao=new DataBoardDAO();
		DataBoardVO vo=dao.dataBoardUpdate(Integer.parseInt(no));
		//request ���� ����ش�. >> JSP���� request�� �ִ� ������ ���
		/*
		 * ��>> �б⾲�� : ���ͼ���
		 * �� >> ��ûó��
		 * 
		 */
		request.setAttribute("vo", vo);
		request.setAttribute("curpage", page);
	}
	public void dataupdatemodel_ok(HttpServletRequest request){
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String no=request.getParameter("no");
		String name=request.getParameter("name");
		String subject=request.getParameter("subject");
		String content=request.getParameter("content");
		String pwd=request.getParameter("pwd");
		String page=request.getParameter("page");
		DataBoardDAO dao=new DataBoardDAO();
		boolean chk=dao.dataUpdate(Integer.parseInt(no), name, subject, content, Integer.parseInt(pwd));
		// �̰� ���� ȥ���Ѱǵ� VO�� ��ġ�� �ʰ� ���� �޼ҵ�  �Ű������θ� �����ſ�
		
		request.setAttribute("no", no);
		request.setAttribute("page", page);
		request.setAttribute("chk", chk);
	}
	
	public void login(HttpServletRequest request){
		String id=request.getParameter("id");
		String pwd=request.getParameter("pwd");
		
		DataBoardDAO dao=new DataBoardDAO();
		String result=dao.islogin(id, pwd);
		
		HttpSession session=request.getSession();
		if(!(result.equals("NOID")||result.equals("NOPWD"))){
			
			session.setAttribute("id", id);
			session.setAttribute("result", result);
		}else{
			session.setAttribute("result", result);
		}
	
		
		
	}
	
	public void replyInsert(HttpServletRequest request){
		
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String msg=request.getParameter("msg");
		String bno=request.getParameter("bno");
		String page=request.getParameter("page");
		HttpSession session=request.getSession();	//��Ű�� ������ ������Ʈ�� �����´�.
		String id=(String)session.getAttribute("id");
		String name=(String)session.getAttribute("result");
		
		DataBoardReplyVO vo=new DataBoardReplyVO();
		vo.setId(id);
		vo.setName(name);
		vo.setBno(Integer.parseInt(bno));
		vo.setMsg(msg);
		
		
		DataBoardReplyDAO dao=new DataBoardReplyDAO();
		dao.replyDataInsert(vo);
		
		request.setAttribute("no", bno);
		request.setAttribute("page", page);
		
		System.out.println("vo.getId : "+vo.getId());
		System.out.println("vo.getName : "+vo.getName());
		System.out.println("vo.getMsg : "+vo.getMsg());
		System.out.println("vo.getBno : "+vo.getBno());
	}
	
	
	public void replyDelete(HttpServletRequest request){
		

		String no=request.getParameter("no");
		String bno=request.getParameter("bno");
		String page=request.getParameter("page");
		
		DataBoardReplyDAO dao=new DataBoardReplyDAO();
		dao.replyDataDelete(Integer.parseInt(no));
		
		request.setAttribute("no", bno);
		request.setAttribute("page", page);
		
	}
	
	public void replyUpdate(HttpServletRequest request){
		
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String no=request.getParameter("no");
		String bno=request.getParameter("bno");
		String page=request.getParameter("page");
		String msg=request.getParameter("msg");
		
		DataBoardReplyDAO dao=new DataBoardReplyDAO();
		dao.replyDataupdate(Integer.parseInt(no),msg);
		
		request.setAttribute("no", bno);
		request.setAttribute("page", page);
		
	}
	
	public void boardDelete(HttpServletRequest request){
		
		// detail.jsp ����  no,page�� delete�� ����
	
		String no=request.getParameter("no");
		String page=request.getParameter("page");


		
		request.setAttribute("no", no);
		request.setAttribute("page", page);
		
	}
	
	public void boardDelete_ok(HttpServletRequest request){
		
		// detail.jsp ����  no,page�� delete�� ����
	
		String no=request.getParameter("no");
		String page=request.getParameter("page");
		String pwd=request.getParameter("pwd");

		
		DataBoardDAO dao=new DataBoardDAO();
		DataBoardVO vo=dao.boardfileInfo(Integer.parseInt(no));
		boolean bcheck=dao.boardDel(Integer.parseInt(no), pwd);
		if(bcheck==true){
			if(vo.getFilesize()>0){
				try {
					File file=new File("c:\\upload\\"+vo.getFilename());
					file.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}	
		
		
		
		request.setAttribute("bcheck", bcheck);
		request.setAttribute("page", page);
		
		
	}
	
}
