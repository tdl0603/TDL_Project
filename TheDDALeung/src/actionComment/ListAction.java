package actionComment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tdl_Comment.*;
import tdl_Post.*;
import java.util.*;
import dbc.*;
import dbc.DBConnectionMgr;
// /list.do=action.ListAction ����
public class ListAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		// TODO Auto-generated method stub
			String  pageNumC=request.getParameter("pageNumC");
		int TPC_num;
		/*
		 * =Integer.parseInt(request.getParameter("TP_num")); // �����Խù� ��ȣ
		 */			TPC_num=1;
			
			int countC=0;//�ѷ��ڵ�� 
			int  numberC=0;//beginPerPage -> ���������� �����ϴ� �� ó���� ������ �Խù� ��ȣ
			List  articleListC=null;//ȭ�鿡 ����� ���ڵ带 ������ ����
			
			TDLCommentDAO dbProC=new TDLCommentDAO();
			 countC=dbProC.getArticleCommentCount(TPC_num);//sql������ ���� �޶�����.
			System.out.println("���� �˻��� ���ڵ��(count) : "+ countC);
			
			Hashtable<String,Integer> pgListC=dbProC.pageList( pageNumC,  countC);
			
		
				System.out.println( pgListC.get("startRow")+","+ pgListC.get("endRow"));
				 articleListC=dbProC.getTDLArticles( pgListC.get("pageSize"), 
																TPC_num);//ù��° ���ڵ��ȣ,�ҷ��ð���
																					//endRow(X)
			
			
		//2.request��ü�� ����
			request.setAttribute("TPC_num", TPC_num);
			request.setAttribute("pgListC",  pgListC);//����¡ó�� 10������
			request.setAttribute("articleListC",  articleListC);//${articleList}
			System.out.println("actionCommentC - ListAction ȣ��");
		//3.�����ؼ� �̵��� �� �ֵ��� ����
		return "/TDL_POST/content.jsp"; //request.getAttribute("currentPage")=${currentPage}
	}

}
