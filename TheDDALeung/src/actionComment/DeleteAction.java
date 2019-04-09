package actionComment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tdl_Comment.*;
import tdl_Post.*;
import java.util.*;
import dbc.*;
import dbc.DBConnectionMgr;
// /list.do=action.ListAction 설정
public class DeleteAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		// TODO Auto-generated method stub
		System.out.println("actionComment DeleteAction 호출");
		int TPC_num = Integer.parseInt(request.getParameter("TPC_num"));
		int countC = Integer.parseInt(request.getParameter("countC"));
		System.out.println("DeleteAction의 TPC_num =>"+TPC_num+" countC"+countC);	
		String addr=Integer.toString(TPC_num)+'c'+Integer.toString(countC)+'c'+0;
		TDLCommentDAO dbProC=new TDLCommentDAO();
		dbProC.deleteAction(addr);	
		System.out.println("actionComment DeleteAction 종료");
		//3.공유해서 이동할 수 있도록 설정
		return "/TDL_POST_list.do"; //request.getAttribute("currentPage")=${currentPage}
	}

}
