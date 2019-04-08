package tdl_Comment;

//DBConnectionMgr(DB접속,관리),BoardDTO(매개변수,반환형)
import java.sql.*;//DB사용
import java.util.*;//ArrayList,List를 사용하기 위해서
import dbc.DBConnectionMgr; // DBC 불러오기
import tdl_Post.*;
public class TDLCommentDAO { //MemberDAO

	private DBConnectionMgr pool=null;//1.선언
	
	//공통으로 접속할 경우 필요한 멤버변수
		private Connection con=null;
		private PreparedStatement pstmt=null;
		private ResultSet rs=null;
		private String sql="";//실행시킬 SQL 구문
	
	//2.생성자를 통해서 연결=>의존성
	public TDLCommentDAO() {
		try {
			pool=DBConnectionMgr.getInstance();
			System.out.println("pool : "+pool);
		}catch(Exception e) {
			System.out.println("TDLCommentDAO -> DB접속오류 : "+e);
		}
	}//생성자
	
	//1.페이징 처리를 위해서 전체 레코드수를 구해와야 한다.
	//select count(*) from board->select count(*) from member; ->getMemberCount()
	
	public int getArticleCount() {
		int x=0;//레코드갯수
		
		try {
			con=pool.getConnection();
			System.out.println("con : "+con);
			sql="select count(*) from TDL_POST_COMMENT";
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next()) {//보여주는 결과가 있다면
				x=rs.getInt(1);//변수명 = rs.get자료형(필드명 또는 인덱스번호)
									 //필드명이 아니기때문에 select ~ from 사이에 나오는 순서
			}
		}catch(Exception e) {
			System.out.println("TDLCommentDAO -> getArticleCount()메서드 에러유발 : "+e);
		}finally {
			pool.freeConnection(con,pstmt,rs);
		}
		return x;
	}
	
	
	//2.글목록보기에 대한 메서드 구현->레코드가 한개이상=>한 페이지당 10개씩 끊어서 보여준다.
	//1.레코드의 시작번호  2.불러올 레코드의 갯수
	public List getArticles(int start,int end) {//getMemberArticle(int start,int end)
																//id asc name desc
		List articleList=null;//ArrayList articleList=null;
		
		try {
			con=pool.getConnection();
			//그룹번호가 가장 최신의 글을중심으로 정렬하되,만약에 level이 같은 경우에는
			//step값으로 오름차순을 통해서 몇번째 레코드 번호를 기준해서 정렬하라.
			sql="select * from TDL_POST_COMMENT order by TPC_ref desc,TPC_step asc limit ?,?";//1,10
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1,  start-1);;//mysql은 레코드순번이 내부적으로 0부터 시작
			pstmt.setInt(2, end);
			rs=pstmt.executeQuery();
			//기존의 레코드외에 추가된 레코드를 첨부해서 다같이 보여주는 개념 -> 누적개념(벽돌) 
			if(rs.next()) {//레코드가 존재한다면 (최소만족 1개)
				//articleList=new List(); X
				//형식) articleList=new 자식클래스명();
				articleList=new ArrayList(end);//10 -> end 갯수만큼 데이터를 담을 공간생성하라
				do {			
					TDLCommentDTO article=makeArticleFromResult();		
			    	//추가 
					articleList.add(article);
				}while(rs.next());
			}
		}catch(Exception e) {
			System.out.println("TDLCommentDAO -> getArticles()메서드 에러유발"+e);
		}finally {
			pool.freeConnection(con,pstmt,rs);
		}
		return articleList;
	}

	
	
	//페이징 처리를 재조정해주는 메서드작성(ListAction 클래스에서 불러오기)
	//1.화면에 보여주는 페이지 번호 2.화면에 출력할 레코드갯수
	public Hashtable pageList(String pageNum,int count) {
		
		//1.페이징 처리결과를 저장할 hashtable객체를 선언
		Hashtable<String,Integer> pgList = new Hashtable<String,Integer>();
		
		
		//1.Jsp에서 담당했던 자바의 코드를 여기에서 실행
		int pageSize=5;//numperpage; -> 페이지당 보여주는 게시물수(=레코드수) -> 20개이상
		int blockSize=2;//pagePerBlock -> 블럭당 보여주는 페이지수
		
		//페이징처리에 해당하는 환경설정을 마무리
		//게시판을 맨 처음 실행시키면 무조건 1페이지부터 출력
		if(pageNum==null){
			pageNum="1";//default(무조건 1페이지는 선택하지 않아도 보여줘야 되기때문에)
		}
		int currentPage=Integer.parseInt(pageNum);//현재페이지 -> nowPage
		//시작레코드번호 -> limit ?,?
		//					(1-1)*10+1=1,(2-1)*10+1=11,(3-1)*10+1=21
		int startRow=(currentPage-1)*pageSize+1;
		int endRow=currentPage*pageSize;//1*10=10,2*10=20,3*10=30
		int number=0;//beginPerPage->페이지별 시작하는 맨 처음에 나오는 게시물 번호
		System.out.println("TDLCOmmentDAO -> Hashtable pageList() 현재 레코드수(count) -> "+count);
		number=count-(currentPage-1)*pageSize;
		System.out.println("TDLCOmmentDAO -> Hashtable pageList() 페이지별 number : "+number);
		 
		//총 페이지수, 시작,종료페이지 계산
		int pageCount=count/pageSize+(count%pageSize==0?0:1);
	       //2.시작페이지 
	       //블럭당 페이지수 계산->10->10배수,3->3의 배수
	       int startPage=0;//1,2,3,,,,10 [다음블럭 10],11,12,,,,,20
	       if(currentPage%blockSize!=0){ //1~9,11~19,21~29,,,
	    	   startPage=currentPage/blockSize*blockSize+1;
	       }else{ //10%10 (10,20,30,40~)
	    	   //             ((10/10)-1)*10+1=1
	    	  startPage=((currentPage/blockSize)-1)*blockSize+1; 
	       }
	       int endPage=startPage+blockSize-1;//1+10-1=10
	       System.out.println("startPage="+startPage+",endPage=>"+endPage);
	       //블럭별로 구분해서 링크걸어서 출력
	       if(endPage > pageCount) endPage=pageCount;//마지막페이지=총페이지수
	      
	       //페이징 처리에 대한 계산결과 -> Hashtable -> ListAction 전달 -> request -> list.jsp출력
	       //~DAO ->  업무에 관련된 코딩-> 액션클래스로 전달 -> view(jsp)에 최종출력
	       pgList.put("pageSize", pageSize);// <-> pgList.get("pageSize")
	       pgList.put("blockSize", blockSize);
	       pgList.put("currentPage", currentPage);
	       pgList.put("startRow", startRow);
	       pgList.put("endRow", endRow);
	       pgList.put("count", count);
	       pgList.put("number", number);
	       pgList.put("startPage", startPage);
	       pgList.put("endPage", endPage);
	       pgList.put("pageCount", pageCount);
	       
		return pgList;
	}
	

	
	
	//---------------게시판의 글쓰기 및 글 답변달기-------------------------------------------------------------------------
	//insert into board values(?,,,
	public void insertArticle(TDLCommentDTO article) {//~(MemberDTO mem)
		
		//1.article -> 신규글인지 답변글인지 구분
		int TPC_num=article.getTPC_num();//0(신규글인지) 0이 아닌 경우(답변글)
		int TPC_ref=article.getTPC_ref();
		int TPC_step=article.getTPC_step();
		int TPC_level=article.getTPC_level();
		//테이블에 입력할 게시물 번호를 저장할 변수
		int number =0;
		System.out.println("TDLCommentDAO -> insertArticle 메서드의 내부의 num : "+TPC_num);
		System.out.println("ref : "+TPC_ref+",step : "+TPC_step+"level : "+TPC_level);
		TDLPostDTO TD= new TDLPostDTO();
		TD.getTP_num();
		
		try {
			con=pool.getConnection();
			sql="select max(TPC_num) from TDL_POST_COMMENT";//최대값+1=실제 저장할 게시물번호
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				number=rs.getInt(1)+1;
			}else {//맨 처음에 레코드가 한개라도 없다면 
				number=1;
				
			}
			//만약에 답변글인경우
		
			//만약에 답변글인경우
			if(TPC_num!=0) {
				//같은 그룹번호를 가지고 있으면서 나보다 step값이 큰 놈을 찾아서 그 step증가
				sql="update TDL_POST_COMMENT set TPC_step=TPC_step+1 where TPC_ref=? and TPC_step > ?";
				pstmt=con.prepareStatement(sql);
				pstmt.setInt(1,TPC_ref);
				pstmt.setInt(2,TPC_step);
				int update=pstmt.executeUpdate();
				System.out.println("댓글수정유무(update) : "+update);//1 or 0
				//답변글
				TPC_step=TPC_step+1;
				TPC_level=TPC_level+1;
				
			}else {//num=0인 경우이기에 신규글임을 알 수가 있다.
				TPC_ref=number;
				TPC_step=0;
				TPC_level=0;
			}
			int TPC_good = 0;
			int TPC_bad = 0;
			
			////////////////////////////로그인 만들면 수정할 것 아이디값으로 넣을것
			String name ="test";
		
			// TPC_addr 값은 자유게시물번호(TPC_num)+댓글번호(TPC_ref)+댓글깊이(댓글의 댓글인지 구분 TPC_level)
			String addr = Integer.toString(TPC_num)+"c"+Integer.toString(TPC_ref)+"c"+Integer.toString(TPC_level);
			
			//12개 -> num,reg_date,reaconut(생략 -> default -> sysdate.now() <- mysql
			sql="insert into TDL_POST_COMMENT(TPC_addr,TPC_ref,TPC_num,TPC_id,TPC_content,";
			sql+="TPC_date,TPC_step,TPC_level,TPC_good,TPC_bad)values(?,?,?,?,?,?,?,?,?,?)";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1,article.getTPC_addr());// 일련번호
			pstmt.setInt(2,article.getTPC_ref());// 글그룹번호
			pstmt.setInt(3,article.getTPC_num());//자유게시물 번호 증가
			pstmt.setString(4,name);
			pstmt.setString(5, article.getTPC_content());//글내용
			pstmt.setString(6, article.getTPC_date());//웹에서 계산해서 저장
			pstmt.setInt(7, TPC_step);//pstmt.setInt(6,article,getRef());(X)
			pstmt.setInt(8,TPC_level);//0
			pstmt.setInt(9, TPC_good);//0
			pstmt.setInt(10,TPC_bad);
			int insert=pstmt.executeUpdate();
			System.out.println("게시판의 글쓰기 성공유무(insert) : "+insert );
		}catch(Exception e) {
			System.out.println("TDLCommentDAO -> insertArticle()메서드 에러유발 : "+e);
			e.printStackTrace();
		}finally {
			pool.freeConnection(con,pstmt,rs);
		}//finally
	}
	
///////추가(1) 검색분야에 따른 검색어를 입력했을때 조건에 만족하는 레코드갯수가 필요
	//////////////////////////////////////

	public int getArticleCommentCount(int TPC_num) { 
		System.out.println("================ TDLComment  - getArticleSearchCount() 시작");
		int x=0;//레코드갯수
	
		try {
			con=pool.getConnection();
			System.out.println("TDLCommentDAO -> con : "+con);
				sql="select count(*) from TDL_POST_COMMENT where TPC_num=?";	
				System.out.println("getArticleCommentCount 의 검색어 sql=>"+sql);				
				pstmt=con.prepareStatement(sql);
				pstmt.setInt(1,TPC_num);
			
			//----------------------------------------------------------------------		
			rs=pstmt.executeQuery();
			if(rs.next()) {//보여주는 결과가 있다면
				x=rs.getInt(1);//변수명 = rs.get자료형(필드명 또는 인덱스번호)
									 //필드명이 아니기때문에 select ~ from 사이에 나오는 순서
			}
		}catch(Exception e) {
			System.out.println("getArticleCommentCount()메서드 에러유발 : "+e);
		}finally {
			pool.freeConnection(con,pstmt,rs);
		}
		System.out.println("================ TDLPost  - getArticleSearchCount() 끝");
		return x;
	}
 	
 	
	
	
	public List getTDLArticles( int end,int TPC_num) {// getMemberArticle(int start,int end)
		// id asc name desc 
		System.out.println("================ TDLComment  - getTDLArticles() 시작");
		List articleList = null;// ArrayList articleList=null;

		try {
			con = pool.getConnection();
			//-----------------------------------------------------------------------------
			sql = "select * from TDL_POST_COMMENT where TPC_num=?";// 1,10		
			System.out.println("TDLComment  getTPCArticles()의 sql => "+sql);
			//-----------------------------------------------------------------------------
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, TPC_num);
			rs = pstmt.executeQuery();
			
//기존의 레코드외에 추가된 레코드를 첨부해서 다같이 보여주는 개념 -> 누적개념(벽돌) 
			if (rs.next()) {// 레코드가 존재한다면 (최소만족 1개)
				//articleList=new List(); X
				//형식) articleList=new 자식클래스명();
								articleList = new ArrayList(end);// 10 -> end 갯수만큼 데이터를 담을 공간생성하라
								do {
									TDLCommentDTO article = makeArticleFromResult();
									
									articleList.add(article);
								} while (rs.next());
							}
			System.out.println("완료");
		} catch (Exception e) {
			System.out.println("TCP getArticles()메서드 에러유발" + e);
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		System.out.println("================ TDLComment  - getTDLArticles() 끝");
		return articleList;
	}
	
	////////////////////////////
	
	//글상세보기 -> list.jsp
	// <a href="content.jsp?num=3&pageNum=1">게시판이란</a>
	
	public TDLCommentDTO getArticle(String TPC_addr) {
		TDLCommentDTO article=null;
		System.out.println("TDLCommentDTO getArticle 실행");
		try {
			con=pool.getConnection();
		
			sql="select * from TPC_POST_COMMENT where TPC_num=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1,Integer.parseInt(TPC_addr));
			rs=pstmt.executeQuery();
			
			if(rs.next()) {//레코드가 존재한다면
				article=makeArticleFromResult();
				
			}
		}catch(Exception e) {
			System.out.println("TDL_POST_COMMENT -> getArticle()메서드 에러유발"+e);
		}finally {
			pool.freeConnection(con,pstmt,rs);
		}
		return article;
	}
	
	// 작성일 구하기 ---------------------------------------------------------------------------------------------------------
		public String getTP_date() {
			System.out.println("================ TDLComment  - getTPC_date() 시작");
			String SQL = "SELECT NOW()";
			try {
				PreparedStatement pstmt = con.prepareStatement(SQL);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					return rs.getString(1);
				}
			}catch(Exception e) {
				System.out.print("TDLCommentDAO getTPC_date() 에러유발  ->");
				e.printStackTrace();
			}
			System.out.println("================ TDLComment  - getTPC_date() 끝");
			return ""; // 데이터베이스 오류
		}
		
	//----중복된 레코드 한개를 담을 수 있는 메서드를 따로 만들어서 처리----------------------
	private TDLCommentDTO makeArticleFromResult() throws Exception{
		TDLCommentDTO article=new TDLCommentDTO();//MemberDTO mem=new MemberDTO();
		article.setTPC_addr(rs.getString("TPC_addr"));// 일련번호
		article.setTPC_ref(rs.getInt("TPC_ref")); // 글그룹번호
		article.setTPC_num(rs.getInt("TPC_num"));//게시물번호
		article.setTPC_id(rs.getString("TPC_id"));//작성자
		article.setTPC_content(rs.getString("TPC_content")); //댓글 내용
		article.setTPC_date(rs.getString("TPC_date"));//오늘날짜->코딩 now()
		article.setTPC_step(rs.getInt("TPC_step"));//default -> 0
		article.setTPC_level(rs.getInt("TPC_level"));//그룹번호->신규글과 답변글 묶어주는 번호
		article.setTPC_good(rs.getInt("TPC_good"));//답변글의 순서
		article.setTPC_bad(rs.getInt("TPC_bad"));//들여쓰기(답변의 깊이)
		return article;
	}
	
	
	
	//-----------------------------------------------------------------
	
	//게시판의 글수정하기
	//select * from board where num=? -> 조회수를 증가X

	public TDLCommentDTO updateGetArticle(String TPC_addr) { //updateForm.jsp에서 출력
		TDLCommentDTO article=null;
		
		try {
			con=pool.getConnection();
			
			sql="select * from TDL_POST_COMMENT where TPC_addr=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1,TPC_addr);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {//레코드가 존재한다면
				article=makeArticleFromResult();
			
			}
		}catch(Exception e) {
			System.out.println("TDLCommentDAO -> updateGetArticle()메서드 에러유발"+e);
		}finally {
			pool.freeConnection(con,pstmt,rs);
		}
		return article;
	}
	
	//글 수정시켜주는 메서드 -> insertArticle와 거의 동일 -> 암호를 물어본다.
	
	public int updateArticle(TDLCommentDTO article) {
		int x=-1;//게시물의 수정성공유무
		
		try {
			con=pool.getConnection();
		
					sql="update TDL_POST_COMMENT set TPC_content=? where TPC_addr=?";
					pstmt=con.prepareStatement(sql);
					pstmt.setString(1, article.getTPC_content());
					pstmt.setString(2, article.getTPC_addr());
					
					int update=pstmt.executeUpdate();
					System.out.println("TDLCommentDAO -> 게시판의 글수정 성공유무(update) : "+update);//1성공
					x=1;

			//if(rs.next()); -> x=-1;
		}catch(Exception e) {
			System.out.println("TDLCommentDAO -> updateArticle()메서드 에러유발 : "+e);
		}finally {
			pool.freeConnection(con,pstmt,rs);//암호를 찾기 때문에
		}
		return x;
	}
	
	
	//글 삭제시켜주는 메서드 -> 회원탈퇴(삭제)=>암호를 물어본다.=> deleteArticle	
	public int deleteArticle(String TPC_addr) { 
		
	
		int x=-1;//게시물의 수정성공유무
		
		try {
					con=pool.getConnection();
			
					sql="delete from TDL_POST_COMMENT where TPC_addr=?";
					pstmt=con.prepareStatement(sql);
					pstmt.setString(1, TPC_addr);
					int delete=pstmt.executeUpdate();
					System.out.println("TDLCommentDAO -> 게시판의 글삭제 성공유무(delete) : "+delete);//1성공
					x=1;
			
		}catch(Exception e) {
			System.out.println("TDLCommentDAO -> deleteArticle()메서드 에러유발 : "+e);
		}finally {
			pool.freeConnection(con,pstmt,rs);//암호를 찾기 때문에
		}
		return x;
	
		
	}
}
