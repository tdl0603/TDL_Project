package actionPost;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dbc.*;
import tdl_Post.*;
import java.util.*;
// /list.do=action.ListAction ����
public class ListAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		// TODO Auto-generated method stub
		    System.out.println("================�����Խ��� ����Ʈ actionPost - ListActionŬ���� ����");
			String pageNum=request.getParameter("pageNum");
			//�߰�(�˻��о�,�˻���)
			String search=request.getParameter("search");
			String searchtext=request.getParameter("searchtext");
			
			int count=0;//�ѷ��ڵ�� 
			int number=0;//beginPerPage -> ���������� �����ϴ� �� ó���� ������ �Խù� ��ȣ
			List articleList=null;//ȭ�鿡 ����� ���ڵ带 ������ ����
			
			TDLPostDAO dbPro=new TDLPostDAO();
			count=dbPro.getArticleSearchCount(search, searchtext);//sql������ ���� �޶�����.
			System.out.println("���� �˻��� ���ڵ��(count) : "+count);
			
			Hashtable<String,Integer> pgList=dbPro.pageList(pageNum, count);
			
			if(count > 0){
				System.out.println(pgList.get("startRow")+","+pgList.get("endRow"));
				articleList=dbPro.getTDLArticles(pgList.get("startRow"), 
																pgList.get("pageSize"), 
																search, searchtext);//ù��° ���ڵ��ȣ,�ҷ��ð���
																					//endRow(X)
			}else {
				articleList=Collections.EMPTY_LIST;//�ƹ��͵� ���� �� list��ü ��ȯ
			}
			
		//2.request��ü�� ����
			request.setAttribute("search", search);//�˻��о�
			request.setAttribute("searchtext", searchtext);//�˻���
			request.setAttribute("pgList", pgList);//����¡ó�� 10������
			request.setAttribute("articleList", articleList);//${articleList}
			System.out.println("================�����Խ��� ����Ʈ actionPost - ListActionŬ���� ��================");
		//3.�����ؼ� �̵��� �� �ֵ��� ����
		return "/TDL_POST/list.jsp"; //request.getAttribute("currentPage")=${currentPage}
	}

}
