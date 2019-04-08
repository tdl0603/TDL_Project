package actionPost;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dbc.*;
//�߰�
import tdl_Post.*;
;public class UpdateFormAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		// TODO Auto-generated method stub
		System.out.println("================�����Խ��� ������Ʈ �� actionPost - UpdateFormActionŬ���� ����");
		//content.jsp -> �ۼ��� -> /TDL/updateForm.do?num=?&pageNum=1
		int TP_num=Integer.parseInt(request.getParameter("TP_num"));
		String pageNum=request.getParameter("pageNum");
		System.out.println("UpdateFormAction TP_num �� =>"+TP_num);
		System.out.println("UpdateFormAction  pageNum -> "+pageNum);
		TDLPostDAO dbPro=new TDLPostDAO();
		TDLPostDTO article=dbPro.updateGetArticle(TP_num);
	
		request.setAttribute("pageNum",pageNum);//${pageNum}
		request.setAttribute("article",article);
		System.out.println("================�����Խ��� ������Ʈ �� actionPost - UpdateFormActionŬ���� �� ");
		return "/TDL_POST/updateForm.jsp";
	}

}
