package dbc;

//����� �ٸ����� ��û�� �޾Ƽ� ó�����ִ� �޼��带 ������ �޼���� �ۼ�
//�ϱ� ���ؼ� �������̽��� �ۼ�
import javax.servlet.http.*;//HttpServletRequest,HttpServletResponse

public interface CommandAction {
	//�̵��� �������� ��ο� ���������� �ʿ�(��û�� ����) -> ��ȯ�� -> ModelAndView(������)
	public String requestPro(HttpServletRequest request,HttpServletResponse response)
	throws Throwable;
}