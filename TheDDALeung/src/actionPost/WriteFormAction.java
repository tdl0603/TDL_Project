package actionPost;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dbc.*;

public class WriteFormAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		// TODO Auto-generated method stub
		
		System.out.println("================�����Խ��� �۾�����  actionPost - WriteFormActionŬ���� ����");
		//jsp���� ó���ؾ��� �ڹ��ڵ带 ���ó�����ִ� ���� -> ��� ���� -> jsp ����
		//list.jsp(�۾���)->�űԱ�, content.jsp(�ۻ󼼺���)-> �۾���-> �亯��
   		int TP_num=0;//writePro.jsp
   		
   		//content.jsp���� �Ű������� ����
   		if(request.getParameter("TP_num")!=null){//��� (����,0�� �ƴϴ�)
   			TP_num=Integer.parseInt(request.getParameter("TP_num"));//"3"->3
   			System.out.println("WriteFormAction if��");
   			System.out.println("content.jsp���� �Ѿ�� �Ű����� Ȯ��");
   			System.out.println("TP_num : "+TP_num);
   		}
   		System.out.println("WriteFormAction ȣ��2");
   		//2.������(��������,�Ű�����,�ż����� ��������)->request������ ����
   			request.setAttribute("TP_num", TP_num); //request.getAttribute("num")->${num}
   			System.out.println("================�����Խ��� �۾��� �� actionPost - WriteFormActionŬ���� ��");
			return "/TDL_POST/writeForm.jsp";
	}

}
