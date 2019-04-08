package actionPost;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dbc.*;
//추가
import tdl_Post.*;


public class WriteProAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		// TODO Auto-generated method stub
		System.out.println("================자유게시판 글쓰기 프로 actionPost - WriteProAction클래스 시작");
		//한글처리	
		request.setCharacterEncoding("utf-8");
		
		//DTO,DAO 객체 필요
		TDLPostDTO article=new TDLPostDTO();
		article.setTP_num(Integer.parseInt(request.getParameter("TP_num")));
		article.setTP_title(request.getParameter("TP_title"));
		article.setTP_content(request.getParameter("TP_content"));
		//==========================================================
		System.out.println("글쓰기액션 넘버값"+Integer.parseInt(request.getParameter("TP_num")));

		TDLPostDAO dbPro=new TDLPostDAO();
		dbPro.insertArticle(article);
		System.out.println("================자유게시판 글쓰기 프로 actionPost - WriteProAction클래스 시작");
		//response.sendRedirect("http://localhost:8090/JspBoard2/list.do");
		return "/TDL_POST/writePro.jsp"; // "/index.jsp"
	}

}
