package tdl_Comment;

public class TDLCommentDTO {
	
	private String TPC_addr;  // �Ϸù�ȣ
	private int TPC_ref;  // ��� �� �׷��ȣ
	private int TPC_num;  // �����Խù� ��ȣ -> ���Խù��� ������� �˱� ����
	private String TPC_id; // ��� �ۼ���
	private String TPC_content; // ��� ����
	private String TPC_date; // ��� �ۼ���
	private int TPC_step; // �Խù� ��ȣ
	private int TPC_level; // ����� ��� ����
	private int TPC_good; // ���ƿ�
	private int TPC_bad; // �Ⱦ��
	
	public int getTPC_level() {
		return TPC_level;
	}
	public void setTPC_level(int tPC_level) {
		TPC_level = tPC_level;
	}
	public String getTPC_addr() {
		return TPC_addr;
	}
	public void setTPC_addr(String TPC_addr) {
		this.TPC_addr = TPC_addr;
	}
	public int getTPC_ref() {
		return TPC_ref;
	}
	public void setTPC_ref(int tPC_ref) {
		TPC_ref = tPC_ref;
	}
	public int getTPC_num() {
		return TPC_num;
	}
	public void setTPC_num(int tPC_num) {
		TPC_num = tPC_num;
	}
	public String getTPC_id() {
		return TPC_id;
	}
	public void setTPC_id(String tPC_id) {
		TPC_id = tPC_id;
	}
	public String getTPC_content() {
		return TPC_content;
	}
	public void setTPC_content(String tPC_content) {
		TPC_content = tPC_content;
	}
	public String getTPC_date() {
		return TPC_date;
	}
	public void setTPC_date(String tPC_date) {
		TPC_date = tPC_date;
	}
	public int getTPC_step() {
		return TPC_step;
	}
	public void setTPC_step(int tPC_step) {
		TPC_step = tPC_step;
	}
	public int getTPC_good() {
		return TPC_good;
	}
	public void setTPC_good(int tPC_good) {
		TPC_good = tPC_good;
	}
	public int getTPC_bad() {
		return TPC_bad;
	}
	public void setTPC_bad(int tPC_bad) {
		TPC_bad = tPC_bad;
	}
	
	
}
