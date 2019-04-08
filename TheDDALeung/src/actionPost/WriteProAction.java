package actionPost;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dbc.*;
//�߰�
import tdl_Post.*;


public class WriteProAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		// TODO Auto-generated method stub
		System.out.println("================�����Խ��� �۾��� ���� actionPost - WriteProActionŬ���� ����");
		//�ѱ�ó��	
		request.setCharacterEncoding("utf-8");
		
		//DTO,DAO ��ü �ʿ�
		TDLPostDTO article=new TDLPostDTO();
		article.setTP_num(Integer.parseInt(request.getParameter("TP_num")));
		article.setTP_title(request.getParameter("TP_title"));
		article.setTP_content(request.getParameter("TP_content"));
		//==========================================================
		System.out.println("�۾���׼� �ѹ���"+Integer.parseInt(request.getParameter("TP_num")));

		TDLPostDAO dbPro=new TDLPostDAO();
		dbPro.insertArticle(article);
		System.out.println("================�����Խ��� �۾��� ���� actionPost - WriteProActionŬ���� ����");
		//response.sendRedirect("http://localhost:8090/JspBoard2/list.do");
		return "/TDL_POST/writePro.jsp"; // "/index.jsp"
	}

}
