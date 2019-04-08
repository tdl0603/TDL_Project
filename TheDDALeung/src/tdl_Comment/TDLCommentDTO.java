package tdl_Comment;

public class TDLCommentDTO {
	
	private String TPC_addr;  // 일련번호
	private int TPC_ref;  // 댓글 글 그룹번호
	private int TPC_num;  // 자유게시물 번호 -> 어디게시물의 댓글인지 알기 위해
	private String TPC_id; // 댓글 작성자
	private String TPC_content; // 댓글 내용
	private String TPC_date; // 댓글 작성일
	private int TPC_step; // 게시물 번호
	private int TPC_level; // 댓글의 댓글 구분
	private int TPC_good; // 좋아요
	private int TPC_bad; // 싫어요
	
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
