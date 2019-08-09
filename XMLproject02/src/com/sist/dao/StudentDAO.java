package com.sist.dao;
import java.util.*;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.*;

public class StudentDAO {
	private static SqlSessionFactory ssf;
	static{
		try{
			//XML을 읽어온다.
			Reader read=Resources.getResourceAsReader("config.xml");
			ssf=new SqlSessionFactoryBuilder().build(read);
			/*
			 * map.put("studentAllData",SELECT~~~)
			 */
			
		}catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public static List<StudentVO> studentAllData(){
		return ssf.openSession().selectList("studentAllData");
	}
}
