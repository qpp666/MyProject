package com.sist.model;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.sist.dao.StudentDAO;
import com.sist.dao.StudentVO;

public class ListModel implements Model {

	@Override
	public String handlerRequest(HttpServletRequest request) {
		List<StudentVO> list=StudentDAO.studentAllData();
		request.setAttribute("list", list);
		return "main/list.jsp";
	}

}
