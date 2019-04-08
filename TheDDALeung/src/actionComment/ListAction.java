package actionComment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tdl_Comment.*;
import tdl_Post.*;
import java.util.*;
import dbc.*;
import dbc.DBConnectionMgr;
// /list.do=action.ListAction 설정
public class ListAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		// TODO Auto-generated method stub
			String  pageNumC=request.getParameter("pageNumC");
		int TPC_num;
		/*
		 * =Integer.parseInt(request.getParameter("TP_num")); // 자유게시물 번호
		 */			TPC_num=1;
			
			int countC=0;//총레코드수 
			int  numberC=0;//beginPerPage -> 페이지별로 시작하는 맨 처음에 나오는 게시물 번호
			List  articleListC=null;//화면에 출력할 레코드를 저장할 변수
			
			TDLCommentDAO dbProC=new TDLCommentDAO();
			 countC=dbProC.getArticleCommentCount(TPC_num);//sql구문에 따라서 달라진다.
			System.out.println("현재 검색된 레코드수(count) : "+ countC);
			
			Hashtable<String,Integer> pgListC=dbProC.pageList( pageNumC,  countC);
			
		
				System.out.println( pgListC.get("startRow")+","+ pgListC.get("endRow"));
				 articleListC=dbProC.getTDLArticles( pgListC.get("pageSize"), 
																TPC_num);//첫번째 레코드번호,불러올갯수
																					//endRow(X)
			
			
		//2.request객체에 저장
			request.setAttribute("TPC_num", TPC_num);
			request.setAttribute("pgListC",  pgListC);//페이징처리 10개정보
			request.setAttribute("articleListC",  articleListC);//${articleList}
			System.out.println("actionCommentC - ListAction 호출");
		//3.공유해서 이동할 수 있도록 설정
		return "/TDL_POST/content.jsp"; //request.getAttribute("currentPage")=${currentPage}
	}

}
