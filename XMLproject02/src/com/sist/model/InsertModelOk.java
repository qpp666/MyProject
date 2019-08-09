package com.sist.model;

import javax.servlet.http.HttpServletRequest;

public class InsertModelOk implements Model {

	@Override
	public String handlerRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return "redirect:movie/insert_ok.do";
	}

}
