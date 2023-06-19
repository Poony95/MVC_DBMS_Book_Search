package com.sist.controller;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sist.action.SistAction;

/**
 * Servlet implementation class SistController
 */
@WebServlet("*.do")
public class SistController extends HttpServlet {
	
	public HashMap<String, SistAction> map;
	//사용자의 요청별 일처리 담당객체를 담아 두기 위한 맵을 선언
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SistController() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    

    //init은 사용자의 *.do 패턴의 최초의 요청일때 
    //한번만 동작하는 메소드 입니다.
    //그때에 맵을 생성하고 
    //사용자의 요청별 일처리 담당객체가 담겨진(WEB-INF/sist.properties)
    //파일의 내용을 읽어 들어 맵에 등록하도록 합니다.
	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		
		map = new  HashMap<String, SistAction>();
		//맵을 생성합니다.
		
		String path = config.getServletContext().getRealPath("WEB-INF");
		//WEB-INF의 실제 경로를 알아옵니다
		
		
		//파일의 내용을 읽어 들여하 하니 예외처리를 합니다.
		try {
			
			FileReader fr = new FileReader(path + "/sist.properties");
			//sist.properties의 파일을 메모리로 읽어들이기 위하여 
			//스트림을 생성합니다.
						
			
			Properties prop = new Properties();
			//파일의 내용을 key와 value로 분리하기 위한
			//Properties 객체를 생성합니다.
			
			
			prop.load(fr);
			//파일의 내용을 프로퍼티 객체로 읽어 들입니다.
			//이때에 key와 value를 분리하게 됩니다.
			
			Iterator iter= prop.keySet().iterator();
			//프로퍼티 객체로 부터 모든 key를 갖고 오는 메소드가 
			//keySet이고 이것을 반복 수행시키기 위하여 
			//interator로 변환해 줍니다.
			
			//key의 목록이 있는 iter에 데이터 있는 만큼 반복 실행합니다.
			//hasNext()는 다음 데이터가 있으면 true
			//없으면 false를 반환합니다.
			//key가 있는 만큼 반복 실행합니다.
			while(iter.hasNext()) {
				
				String key = (String)iter.next();
				//next()메소드는 key목록이 담겨진 iter로 부터
				//데이터를 하나씩 꺼집어 내어 옵니다.
				//next는 Object를 반환하기 때문에 String으로 
				//형변환 하였습니다.
				//key : listBook.do
				
				String clsName = prop.getProperty(key);
				//clsName : com.sist.action.ListBookAction
				
				SistAction action = 
				(SistAction)Class.forName(clsName).newInstance();
				//문자열로 된 클래스이름에 해당하는 객체를 생성하기 위하여 
				//Class.forName().newInstance 메소드를 이용합니다.
				//newInstance Object를 반환하기 때문에 
				//SistAction로 형변환 해 줍니다.
				
				
				
				map.put(key, action);		
				//맵에 사용자가 요청을 서비스명과 그 에 따른 일처리 담당을 수행하게 될 
				//객체를 map에 등록합니다.
			}
			fr.close();			
		}catch (Exception e) {
			System.out.println("예외발생:"+e.getMessage());
		}
		
	}



	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		pro(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		pro(request, response);
	}

	private void pro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		// TODO Auto-generated method stub
		String uri = request.getRequestURI();
		String cmd = uri.substring(uri.lastIndexOf("/") + 1);
		SistAction action = map.get(cmd);
		String view = action.pro(request, response);
		
		RequestDispatcher dispatcher
		= request.getRequestDispatcher(view);
		
		dispatcher.forward(request, response);
	}

}






