package com.sist.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sist.dao.BookDAO;

public class ListBookAction implements SistAction {

	@Override
	public String pro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();		
		String keyword = null;
		String searchColumn = null;
		String op = null;
		
		//만약 세션에 검색된 정보가 있다면 읽어 옵니다.
		if(session.getAttribute("keyword") != null) {
			keyword = (String)session.getAttribute("keyword");
			searchColumn  = (String)session.getAttribute("searchColumn");
			
			//세션에 저장된 칼럼이름이 price라면 연산자도 갖고 옵니다.
			if(searchColumn.equals("price")) {
				op = (String)session.getAttribute("op");
			}
		}
		
		//세션에 저장된 검색어 보다 새로운 검색이 우선순위 높아야 합니다.
		//만약 새로운 검색어가 있다면 그것을 읽어 옵니다.
		if(request.getParameter("keyword") != null) {			
			keyword = request.getParameter("keyword");
			searchColumn = request.getParameter("searchColumn");
			
			//검색칼럼이 price라면 연산자도 받아옵니다.
			if(searchColumn.equals("price")) {
				op = request.getParameter("op");
			}
		}
		
		System.out.println("검색어:"+keyword);
		
		int pageNUM = 1;
		if(request.getParameter("pageNUM") != null) {
			pageNUM = Integer.parseInt(request.getParameter("pageNUM"));
		}
		System.out.println("pageNUM:"+pageNUM);
		
		BookDAO dao = BookDAO.getInstance();
		
		request.setAttribute("list", dao.findAll(pageNUM, keyword, searchColumn, op));
		request.setAttribute("totalPage", dao.totalPage);
		
		
		session.setAttribute("keyword", keyword);
		session.setAttribute("searchColumn", searchColumn);
		session.setAttribute("op", op);
		
		return "listBook.jsp";
	}
}











