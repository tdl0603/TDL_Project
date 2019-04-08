package actionComment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//추가
import dbc.*;
import tdl_Comment.*;
import java.sql.Timestamp;//추가할 부분(시간)

public class WriteProAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		// TODO Auto-generated method stub
	System.out.println("actionComment WriteProAction 시작!");
		//한글처리	
		request.setCharacterEncoding("utf-8");
		//BoardDTO -> Setter Method(5)+hidden (4)
		//BoardDATO 객체 필요
		TDLCommentDTO articleC=new TDLCommentDTO();
		
		System.out.println("TPC_num 받아오는 값 -> "+request.getParameter("TP_num"));
		int TPC_ref=1,TPC_step=0,TPC_level=0;//writePro.jsp
		articleC.setTPC_num(Integer.parseInt(request.getParameter("TP_num")));
		articleC.setTPC_ref(TPC_ref);
		articleC.setTPC_step(TPC_step);
		articleC.setTPC_level(TPC_level);
		articleC.setTPC_content(request.getParameter("TPC_content"));
		System.out.println("중간점검 "); 
		System.out.println("TPC_num => "+articleC.getTPC_num());
		System.out.println("TPC_ref => "+articleC.getTPC_ref());
		System.out.println("TPC_step => "+articleC.getTPC_step());
		System.out.println("TPC_level => "+articleC.getTPC_level());

		
		TDLCommentDAO dbPro=new TDLCommentDAO();
		dbPro.insertArticle(articleC);
		//response.sendRedirect("http://localhost:8090/JspBoard2/list.do");
		return "/TDL_COMMENT/writePro.jsp"; // "/index.jsp"
	}

}
