package actionPost;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dbc.*;
//추가
import tdl_Post.*;
;public class UpdateFormAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		// TODO Auto-generated method stub
		System.out.println("================자유게시판 업데이트 폼 actionPost - UpdateFormAction클래스 시작");
		//content.jsp -> 글수정 -> /TDL/updateForm.do?num=?&pageNum=1
		int TP_num=Integer.parseInt(request.getParameter("TP_num"));
		String pageNum=request.getParameter("pageNum");
		System.out.println("UpdateFormAction TP_num 값 =>"+TP_num);
		System.out.println("UpdateFormAction  pageNum -> "+pageNum);
		TDLPostDAO dbPro=new TDLPostDAO();
		TDLPostDTO article=dbPro.updateGetArticle(TP_num);
	
		request.setAttribute("pageNum",pageNum);//${pageNum}
		request.setAttribute("article",article);
		System.out.println("================자유게시판 업데이트 폼 actionPost - UpdateFormAction클래스 끝 ");
		return "/TDL_POST/updateForm.jsp";
	}

}
