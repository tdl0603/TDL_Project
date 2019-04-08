package tdl_Comment;

//DBConnectionMgr(DB����,����),BoardDTO(�Ű�����,��ȯ��)
import java.sql.*;//DB���
import java.util.*;//ArrayList,List�� ����ϱ� ���ؼ�
import dbc.DBConnectionMgr; // DBC �ҷ�����
import tdl_Post.*;
public class TDLCommentDAO { //MemberDAO

	private DBConnectionMgr pool=null;//1.����
	
	//�������� ������ ��� �ʿ��� �������
		private Connection con=null;
		private PreparedStatement pstmt=null;
		private ResultSet rs=null;
		private String sql="";//�����ų SQL ����
	
	//2.�����ڸ� ���ؼ� ����=>������
	public TDLCommentDAO() {
		try {
			pool=DBConnectionMgr.getInstance();
			System.out.println("pool : "+pool);
		}catch(Exception e) {
			System.out.println("TDLCommentDAO -> DB���ӿ��� : "+e);
		}
	}//������
	
	//1.����¡ ó���� ���ؼ� ��ü ���ڵ���� ���ؿ;� �Ѵ�.
	//select count(*) from board->select count(*) from member; ->getMemberCount()
	
	public int getArticleCount() {
		int x=0;//���ڵ尹��
		
		try {
			con=pool.getConnection();
			System.out.println("con : "+con);
			sql="select count(*) from TDL_POST_COMMENT";
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next()) {//�����ִ� ����� �ִٸ�
				x=rs.getInt(1);//������ = rs.get�ڷ���(�ʵ�� �Ǵ� �ε�����ȣ)
									 //�ʵ���� �ƴϱ⶧���� select ~ from ���̿� ������ ����
			}
		}catch(Exception e) {
			System.out.println("TDLCommentDAO -> getArticleCount()�޼��� �������� : "+e);
		}finally {
			pool.freeConnection(con,pstmt,rs);
		}
		return x;
	}
	
	
	//2.�۸�Ϻ��⿡ ���� �޼��� ����->���ڵ尡 �Ѱ��̻�=>�� �������� 10���� ��� �����ش�.
	//1.���ڵ��� ���۹�ȣ  2.�ҷ��� ���ڵ��� ����
	public List getArticles(int start,int end) {//getMemberArticle(int start,int end)
																//id asc name desc
		List articleList=null;//ArrayList articleList=null;
		
		try {
			con=pool.getConnection();
			//�׷��ȣ�� ���� �ֽ��� �����߽����� �����ϵ�,���࿡ level�� ���� ��쿡��
			//step������ ���������� ���ؼ� ���° ���ڵ� ��ȣ�� �����ؼ� �����϶�.
			sql="select * from TDL_POST_COMMENT order by TPC_ref desc,TPC_step asc limit ?,?";//1,10
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1,  start-1);;//mysql�� ���ڵ������ ���������� 0���� ����
			pstmt.setInt(2, end);
			rs=pstmt.executeQuery();
			//������ ���ڵ�ܿ� �߰��� ���ڵ带 ÷���ؼ� �ٰ��� �����ִ� ���� -> ��������(����) 
			if(rs.next()) {//���ڵ尡 �����Ѵٸ� (�ּҸ��� 1��)
				//articleList=new List(); X
				//����) articleList=new �ڽ�Ŭ������();
				articleList=new ArrayList(end);//10 -> end ������ŭ �����͸� ���� ���������϶�
				do {			
					TDLCommentDTO article=makeArticleFromResult();		
			    	//�߰� 
					articleList.add(article);
				}while(rs.next());
			}
		}catch(Exception e) {
			System.out.println("TDLCommentDAO -> getArticles()�޼��� ��������"+e);
		}finally {
			pool.freeConnection(con,pstmt,rs);
		}
		return articleList;
	}

	
	
	//����¡ ó���� ���������ִ� �޼����ۼ�(ListAction Ŭ�������� �ҷ�����)
	//1.ȭ�鿡 �����ִ� ������ ��ȣ 2.ȭ�鿡 ����� ���ڵ尹��
	public Hashtable pageList(String pageNum,int count) {
		
		//1.����¡ ó������� ������ hashtable��ü�� ����
		Hashtable<String,Integer> pgList = new Hashtable<String,Integer>();
		
		
		//1.Jsp���� ����ߴ� �ڹ��� �ڵ带 ���⿡�� ����
		int pageSize=5;//numperpage; -> �������� �����ִ� �Խù���(=���ڵ��) -> 20���̻�
		int blockSize=2;//pagePerBlock -> ���� �����ִ� ��������
		
		//����¡ó���� �ش��ϴ� ȯ�漳���� ������
		//�Խ����� �� ó�� �����Ű�� ������ 1���������� ���
		if(pageNum==null){
			pageNum="1";//default(������ 1�������� �������� �ʾƵ� ������� �Ǳ⶧����)
		}
		int currentPage=Integer.parseInt(pageNum);//���������� -> nowPage
		//���۷��ڵ��ȣ -> limit ?,?
		//					(1-1)*10+1=1,(2-1)*10+1=11,(3-1)*10+1=21
		int startRow=(currentPage-1)*pageSize+1;
		int endRow=currentPage*pageSize;//1*10=10,2*10=20,3*10=30
		int number=0;//beginPerPage->�������� �����ϴ� �� ó���� ������ �Խù� ��ȣ
		System.out.println("TDLCOmmentDAO -> Hashtable pageList() ���� ���ڵ��(count) -> "+count);
		number=count-(currentPage-1)*pageSize;
		System.out.println("TDLCOmmentDAO -> Hashtable pageList() �������� number : "+number);
		 
		//�� ��������, ����,���������� ���
		int pageCount=count/pageSize+(count%pageSize==0?0:1);
	       //2.���������� 
	       //���� �������� ���->10->10���,3->3�� ���
	       int startPage=0;//1,2,3,,,,10 [������ 10],11,12,,,,,20
	       if(currentPage%blockSize!=0){ //1~9,11~19,21~29,,,
	    	   startPage=currentPage/blockSize*blockSize+1;
	       }else{ //10%10 (10,20,30,40~)
	    	   //             ((10/10)-1)*10+1=1
	    	  startPage=((currentPage/blockSize)-1)*blockSize+1; 
	       }
	       int endPage=startPage+blockSize-1;//1+10-1=10
	       System.out.println("startPage="+startPage+",endPage=>"+endPage);
	       //������ �����ؼ� ��ũ�ɾ ���
	       if(endPage > pageCount) endPage=pageCount;//������������=����������
	      
	       //����¡ ó���� ���� ����� -> Hashtable -> ListAction ���� -> request -> list.jsp���
	       //~DAO ->  ������ ���õ� �ڵ�-> �׼�Ŭ������ ���� -> view(jsp)�� �������
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
	

	
	
	//---------------�Խ����� �۾��� �� �� �亯�ޱ�-------------------------------------------------------------------------
	//insert into board values(?,,,
	public void insertArticle(TDLCommentDTO article) {//~(MemberDTO mem)
		
		//1.article -> �űԱ����� �亯������ ����
		int TPC_num=article.getTPC_num();//0(�űԱ�����) 0�� �ƴ� ���(�亯��)
		int TPC_ref=article.getTPC_ref();
		int TPC_step=article.getTPC_step();
		int TPC_level=article.getTPC_level();
		//���̺� �Է��� �Խù� ��ȣ�� ������ ����
		int number =0;
		System.out.println("TDLCommentDAO -> insertArticle �޼����� ������ num : "+TPC_num);
		System.out.println("ref : "+TPC_ref+",step : "+TPC_step+"level : "+TPC_level);
		TDLPostDTO TD= new TDLPostDTO();
		TD.getTP_num();
		
		try {
			con=pool.getConnection();
			sql="select max(TPC_num) from TDL_POST_COMMENT";//�ִ밪+1=���� ������ �Խù���ȣ
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				number=rs.getInt(1)+1;
			}else {//�� ó���� ���ڵ尡 �Ѱ��� ���ٸ� 
				number=1;
				
			}
			//���࿡ �亯���ΰ��
		
			//���࿡ �亯���ΰ��
			if(TPC_num!=0) {
				//���� �׷��ȣ�� ������ �����鼭 ������ step���� ū ���� ã�Ƽ� �� step����
				sql="update TDL_POST_COMMENT set TPC_step=TPC_step+1 where TPC_ref=? and TPC_step > ?";
				pstmt=con.prepareStatement(sql);
				pstmt.setInt(1,TPC_ref);
				pstmt.setInt(2,TPC_step);
				int update=pstmt.executeUpdate();
				System.out.println("��ۼ�������(update) : "+update);//1 or 0
				//�亯��
				TPC_step=TPC_step+1;
				TPC_level=TPC_level+1;
				
			}else {//num=0�� ����̱⿡ �űԱ����� �� ���� �ִ�.
				TPC_ref=number;
				TPC_step=0;
				TPC_level=0;
			}
			int TPC_good = 0;
			int TPC_bad = 0;
			
			////////////////////////////�α��� ����� ������ �� ���̵����� ������
			String name ="test";
		
			// TPC_addr ���� �����Խù���ȣ(TPC_num)+��۹�ȣ(TPC_ref)+��۱���(����� ������� ���� TPC_level)
			String addr = Integer.toString(TPC_num)+"c"+Integer.toString(TPC_ref)+"c"+Integer.toString(TPC_level);
			
			//12�� -> num,reg_date,reaconut(���� -> default -> sysdate.now() <- mysql
			sql="insert into TDL_POST_COMMENT(TPC_addr,TPC_ref,TPC_num,TPC_id,TPC_content,";
			sql+="TPC_date,TPC_step,TPC_level,TPC_good,TPC_bad)values(?,?,?,?,?,?,?,?,?,?)";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1,article.getTPC_addr());// �Ϸù�ȣ
			pstmt.setInt(2,article.getTPC_ref());// �۱׷��ȣ
			pstmt.setInt(3,article.getTPC_num());//�����Խù� ��ȣ ����
			pstmt.setString(4,name);
			pstmt.setString(5, article.getTPC_content());//�۳���
			pstmt.setString(6, article.getTPC_date());//������ ����ؼ� ����
			pstmt.setInt(7, TPC_step);//pstmt.setInt(6,article,getRef());(X)
			pstmt.setInt(8,TPC_level);//0
			pstmt.setInt(9, TPC_good);//0
			pstmt.setInt(10,TPC_bad);
			int insert=pstmt.executeUpdate();
			System.out.println("�Խ����� �۾��� ��������(insert) : "+insert );
		}catch(Exception e) {
			System.out.println("TDLCommentDAO -> insertArticle()�޼��� �������� : "+e);
			e.printStackTrace();
		}finally {
			pool.freeConnection(con,pstmt,rs);
		}//finally
	}
	
///////�߰�(1) �˻��о߿� ���� �˻�� �Է������� ���ǿ� �����ϴ� ���ڵ尹���� �ʿ�
	//////////////////////////////////////

	public int getArticleCommentCount(int TPC_num) { 
		System.out.println("================ TDLComment  - getArticleSearchCount() ����");
		int x=0;//���ڵ尹��
	
		try {
			con=pool.getConnection();
			System.out.println("TDLCommentDAO -> con : "+con);
				sql="select count(*) from TDL_POST_COMMENT where TPC_num=?";	
				System.out.println("getArticleCommentCount �� �˻��� sql=>"+sql);				
				pstmt=con.prepareStatement(sql);
				pstmt.setInt(1,TPC_num);
			
			//----------------------------------------------------------------------		
			rs=pstmt.executeQuery();
			if(rs.next()) {//�����ִ� ����� �ִٸ�
				x=rs.getInt(1);//������ = rs.get�ڷ���(�ʵ�� �Ǵ� �ε�����ȣ)
									 //�ʵ���� �ƴϱ⶧���� select ~ from ���̿� ������ ����
			}
		}catch(Exception e) {
			System.out.println("getArticleCommentCount()�޼��� �������� : "+e);
		}finally {
			pool.freeConnection(con,pstmt,rs);
		}
		System.out.println("================ TDLPost  - getArticleSearchCount() ��");
		return x;
	}
 	
 	
	
	
	public List getTDLArticles( int end,int TPC_num) {// getMemberArticle(int start,int end)
		// id asc name desc 
		System.out.println("================ TDLComment  - getTDLArticles() ����");
		List articleList = null;// ArrayList articleList=null;

		try {
			con = pool.getConnection();
			//-----------------------------------------------------------------------------
			sql = "select * from TDL_POST_COMMENT where TPC_num=?";// 1,10		
			System.out.println("TDLComment  getTPCArticles()�� sql => "+sql);
			//-----------------------------------------------------------------------------
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, TPC_num);
			rs = pstmt.executeQuery();
			
//������ ���ڵ�ܿ� �߰��� ���ڵ带 ÷���ؼ� �ٰ��� �����ִ� ���� -> ��������(����) 
			if (rs.next()) {// ���ڵ尡 �����Ѵٸ� (�ּҸ��� 1��)
				//articleList=new List(); X
				//����) articleList=new �ڽ�Ŭ������();
								articleList = new ArrayList(end);// 10 -> end ������ŭ �����͸� ���� ���������϶�
								do {
									TDLCommentDTO article = makeArticleFromResult();
									
									articleList.add(article);
								} while (rs.next());
							}
			System.out.println("�Ϸ�");
		} catch (Exception e) {
			System.out.println("TCP getArticles()�޼��� ��������" + e);
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		System.out.println("================ TDLComment  - getTDLArticles() ��");
		return articleList;
	}
	
	////////////////////////////
	
	//�ۻ󼼺��� -> list.jsp
	// <a href="content.jsp?num=3&pageNum=1">�Խ����̶�</a>
	
	public TDLCommentDTO getArticle(String TPC_addr) {
		TDLCommentDTO article=null;
		System.out.println("TDLCommentDTO getArticle ����");
		try {
			con=pool.getConnection();
		
			sql="select * from TPC_POST_COMMENT where TPC_num=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1,Integer.parseInt(TPC_addr));
			rs=pstmt.executeQuery();
			
			if(rs.next()) {//���ڵ尡 �����Ѵٸ�
				article=makeArticleFromResult();
				
			}
		}catch(Exception e) {
			System.out.println("TDL_POST_COMMENT -> getArticle()�޼��� ��������"+e);
		}finally {
			pool.freeConnection(con,pstmt,rs);
		}
		return article;
	}
	
	// �ۼ��� ���ϱ� ---------------------------------------------------------------------------------------------------------
		public String getTP_date() {
			System.out.println("================ TDLComment  - getTPC_date() ����");
			String SQL = "SELECT NOW()";
			try {
				PreparedStatement pstmt = con.prepareStatement(SQL);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					return rs.getString(1);
				}
			}catch(Exception e) {
				System.out.print("TDLCommentDAO getTPC_date() ��������  ->");
				e.printStackTrace();
			}
			System.out.println("================ TDLComment  - getTPC_date() ��");
			return ""; // �����ͺ��̽� ����
		}
		
	//----�ߺ��� ���ڵ� �Ѱ��� ���� �� �ִ� �޼��带 ���� ���� ó��----------------------
	private TDLCommentDTO makeArticleFromResult() throws Exception{
		TDLCommentDTO article=new TDLCommentDTO();//MemberDTO mem=new MemberDTO();
		article.setTPC_addr(rs.getString("TPC_addr"));// �Ϸù�ȣ
		article.setTPC_ref(rs.getInt("TPC_ref")); // �۱׷��ȣ
		article.setTPC_num(rs.getInt("TPC_num"));//�Խù���ȣ
		article.setTPC_id(rs.getString("TPC_id"));//�ۼ���
		article.setTPC_content(rs.getString("TPC_content")); //��� ����
		article.setTPC_date(rs.getString("TPC_date"));//���ó�¥->�ڵ� now()
		article.setTPC_step(rs.getInt("TPC_step"));//default -> 0
		article.setTPC_level(rs.getInt("TPC_level"));//�׷��ȣ->�űԱ۰� �亯�� �����ִ� ��ȣ
		article.setTPC_good(rs.getInt("TPC_good"));//�亯���� ����
		article.setTPC_bad(rs.getInt("TPC_bad"));//�鿩����(�亯�� ����)
		return article;
	}
	
	
	
	//-----------------------------------------------------------------
	
	//�Խ����� �ۼ����ϱ�
	//select * from board where num=? -> ��ȸ���� ����X

	public TDLCommentDTO updateGetArticle(String TPC_addr) { //updateForm.jsp���� ���
		TDLCommentDTO article=null;
		
		try {
			con=pool.getConnection();
			
			sql="select * from TDL_POST_COMMENT where TPC_addr=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1,TPC_addr);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {//���ڵ尡 �����Ѵٸ�
				article=makeArticleFromResult();
			
			}
		}catch(Exception e) {
			System.out.println("TDLCommentDAO -> updateGetArticle()�޼��� ��������"+e);
		}finally {
			pool.freeConnection(con,pstmt,rs);
		}
		return article;
	}
	
	//�� ���������ִ� �޼��� -> insertArticle�� ���� ���� -> ��ȣ�� �����.
	
	public int updateArticle(TDLCommentDTO article) {
		int x=-1;//�Խù��� ������������
		
		try {
			con=pool.getConnection();
		
					sql="update TDL_POST_COMMENT set TPC_content=? where TPC_addr=?";
					pstmt=con.prepareStatement(sql);
					pstmt.setString(1, article.getTPC_content());
					pstmt.setString(2, article.getTPC_addr());
					
					int update=pstmt.executeUpdate();
					System.out.println("TDLCommentDAO -> �Խ����� �ۼ��� ��������(update) : "+update);//1����
					x=1;

			//if(rs.next()); -> x=-1;
		}catch(Exception e) {
			System.out.println("TDLCommentDAO -> updateArticle()�޼��� �������� : "+e);
		}finally {
			pool.freeConnection(con,pstmt,rs);//��ȣ�� ã�� ������
		}
		return x;
	}
	
	
	//�� ���������ִ� �޼��� -> ȸ��Ż��(����)=>��ȣ�� �����.=> deleteArticle	
	public int deleteArticle(String TPC_addr) { 
		
	
		int x=-1;//�Խù��� ������������
		
		try {
					con=pool.getConnection();
			
					sql="delete from TDL_POST_COMMENT where TPC_addr=?";
					pstmt=con.prepareStatement(sql);
					pstmt.setString(1, TPC_addr);
					int delete=pstmt.executeUpdate();
					System.out.println("TDLCommentDAO -> �Խ����� �ۻ��� ��������(delete) : "+delete);//1����
					x=1;
			
		}catch(Exception e) {
			System.out.println("TDLCommentDAO -> deleteArticle()�޼��� �������� : "+e);
		}finally {
			pool.freeConnection(con,pstmt,rs);//��ȣ�� ã�� ������
		}
		return x;
	
		
	}
}
