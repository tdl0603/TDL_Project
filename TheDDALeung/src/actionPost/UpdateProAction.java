package actionPost;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dbc.*;
//추가
import tdl_Post.*;
import java.sql.Timestamp;//추가할 부분(시간)

// /updatePro.do?num=?&pageNum=1
public class UpdateProAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		// TODO Auto-generated method stub
		System.out.println("================자유게시판 업데이트 프로 actionPost - UpdateProAction클래스 시작");
		//한글처리	
		request.setCharacterEncoding("utf-8");
		String pageNum=request.getParameter("pageNum");
		
		TDLPostDTO article=new TDLPostDTO();
		System.out.println("UpdateProAction 여기까진 나옴");
		article.setTP_num(Integer.parseInt(request.getParameter("TP_num")));
		article.setTP_title(request.getParameter("TP_title"));
		article.setTP_content(request.getParameter("TP_content"));

		TDLPostDAO dbPro=new TDLPostDAO();
		int check=dbPro.updateArticle(article);
		
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("check", check);
		System.out.println("================자유게시판 업데이트 프로 actionPost - UpdateProAction클래스 끝");
		return "/TDL_POST/updatePro.jsp"; // "/index.jsp"
	}

}
