package tdl_Post;

public class TDLPostDTO {

	private int TP_num;  // 자유게시판 글 번호
	private String TP_title; //자유게시판 제목
	private String TP_id; //유저 아이디
	private String TP_date;// 작성일
	private String TP_content; //글 내용
	
/*
 TP_num,TP,title,TP_id,TP_date,TP_content
  
  
 */
	
	
	public int getTP_num() {
		return TP_num;
	} 
	public void setTP_num(int tP_num) {
		TP_num = tP_num;
	}
	public String getTP_title() {
		return TP_title;
	}
	public void setTP_title(String tP_title) {
		TP_title = tP_title;
	}
	public String getTP_id() {
		return TP_id;
	}
	public void setTP_id(String tP_id) {
		TP_id = tP_id;
	}
	public String getTP_date() {
		return TP_date;
	}
	public void setTP_date(String tP_date) {
		TP_date = tP_date;
	}
	public String getTP_content() {
		return TP_content;
	}
	public void setTP_content(String tP_content) {
		TP_content = tP_content;
	}
	
	
}
