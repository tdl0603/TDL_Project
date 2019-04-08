package actionPost;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dbc.*;
//�߰�
import tdl_Post.*;
import java.sql.Timestamp;//�߰��� �κ�(�ð�)

// /updatePro.do?num=?&pageNum=1
public class UpdateProAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		// TODO Auto-generated method stub
		System.out.println("================�����Խ��� ������Ʈ ���� actionPost - UpdateProActionŬ���� ����");
		//�ѱ�ó��	
		request.setCharacterEncoding("utf-8");
		String pageNum=request.getParameter("pageNum");
		
		TDLPostDTO article=new TDLPostDTO();
		System.out.println("UpdateProAction ������� ����");
		article.setTP_num(Integer.parseInt(request.getParameter("TP_num")));
		article.setTP_title(request.getParameter("TP_title"));
		article.setTP_content(request.getParameter("TP_content"));

		TDLPostDAO dbPro=new TDLPostDAO();
		int check=dbPro.updateArticle(article);
		
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("check", check);
		System.out.println("================�����Խ��� ������Ʈ ���� actionPost - UpdateProActionŬ���� ��");
		return "/TDL_POST/updatePro.jsp"; // "/index.jsp"
	}

}
