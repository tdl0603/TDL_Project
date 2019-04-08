package actionPost;

import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dbc.*;
import tdl_Comment.TDLCommentDAO;
//추가
import tdl_Post.*;

//content.jsp에서 처리한 자바코드부분을 대신 처리해주는 액션클래스
public class ContentAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {

		System.out.println("================자유게시판 글보기 actionPost - ContentAction클래스 시작");
		int TP_num=Integer.parseInt(request.getParameter("TP_num"));
		String pageNum=request.getParameter("pageNum");
		TDLPostDAO dbPro=new TDLPostDAO();
		TDLPostDTO article=dbPro.getArticle(TP_num);
	
		System.out.println("content.do의 매개변수");		
		//ModelAndView mav=new ModelAndView(); 
		//mav.addObject("TP_num",TP_num);
		request.setAttribute("TP_num", TP_num);
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("article", article);
		
		System.out.println("================자유게시판 글보기 actionPost - ContentAction클래스 끝");
		// ---------------------------여기부터는 댓글 ---------------------------------------------------------
		String  pageNumC=request.getParameter("pageNumC");
		 int TPC_num=1;
		/*
		 * =Integer.parseInt(request.getParameter("TP_num")); // 자유게시물 번호
		 */			
			int countC=0;//총레코드수 
			int  numberC=0;//beginPerPage -> 페이지별로 시작하는 맨 처음에 나오는 게시물 번호
			List  articleListC=null;//화면에 출력할 레코드를 저장할 변수
			
			TDLCommentDAO dbProC=new TDLCommentDAO();
			 countC=dbProC.getArticleCommentCount(TPC_num);//sql구문에 따라서 달라진다.
			System.out.println("현재 검색된 레코드수(count) : "+ countC);
			
			Hashtable<String,Integer> pgListC=dbProC.pageList( pageNumC,  countC);
			
		
				System.out.println( pgListC.get("startRow")+","+ pgListC.get("endRow"));
				 articleListC=dbProC.getTDLArticles( pgListC.get("pageSize"),TPC_num);//첫번째 레코드번호,불러올갯수
																				
				
		//2.request객체에 저장
		   
			request.setAttribute("TPC_num", TPC_num);
			request.setAttribute("pgListC",  pgListC);//페이징처리 10개정보
			request.setAttribute("articleListC",  articleListC);//${articleList}
			System.out.println("actionCommentC - ListAction 호출");
		//3.공유해서 이동할 수 있도록 설정
	
			
		return "/TDL_POST/content.jsp";
	}

}
