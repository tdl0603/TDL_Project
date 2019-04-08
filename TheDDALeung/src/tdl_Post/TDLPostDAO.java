package tdl_Post;

//DBConnectionMgr(DB����,����),BoardDTO(�Ű�����,��ȯ��)
import java.sql.*;//DB���
import java.util.*;//ArrayList,List�� ����ϱ� ���ؼ�

import tdl_Post.TDLPostDTO;
import dbc.DBConnectionMgr;
 
public class TDLPostDAO {

	private DBConnectionMgr pool=null;//1.����
	
	//�������� ������ ��� �ʿ��� �������
	private Connection con=null;
	private PreparedStatement pstmt=null;
	private ResultSet rs=null;
	private String sql="";//�����ų SQL ����
	
	//2.�����ڸ� ���ؼ� ����=>������
	public TDLPostDAO() { // ������
		try {
			
			System.out.println("================TDLPost - TDLPostDAO() ����");
			pool=DBConnectionMgr.getInstance();
			System.out.println("pool : "+pool);
		}catch(Exception e) {
			System.out.println("DB���ӿ��� : "+e);
		}
		System.out.println("================ TDLPost - TDLPostDAO() ��");
	}

	//1.����¡ ó���� ���ؼ� ��ü ���ڵ���� ���ؿ;� �Ѵ�.		
	public int getArticleCount() {
		System.out.println("================ TDLPost  - getArticleCount() ����");
		int x=0;//���ڵ尹��
		
		try {
			con=pool.getConnection();
			System.out.println("con : "+con);
			sql="select count(*) from TDL_POST";
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next()) {//�����ִ� ����� �ִٸ�
				x=rs.getInt(1);//������ = rs.get�ڷ���(�ʵ�� �Ǵ� �ε�����ȣ)
									 //�ʵ���� �ƴϱ⶧���� select ~ from ���̿� ������ ����
			}
			System.out.println("TDLPostDAO ī��Ʈ ��¼���");
		}catch(Exception e) {
			System.out.println("getArticleCount()�޼��� �������� : "+e);
		}finally {
			pool.freeConnection(con,pstmt,rs);
		}
		System.out.println("================ TDLPost  - getArticleCount() ��");
		return x;
	}

	// �۸�Ϻ��⿡ ���� �޼��� ����->���ڵ尡 �Ѱ��̻�=>�� �������� 10���� ��� �����ش�.
	public List getArticles(int start, int end) {// getMemberArticle(int start,int end)
		// id asc name desc
		System.out.println("================ TDLPost  - getArticles() ����");
		List articleList = null;// ArrayList articleList=null;

		try {
			con = pool.getConnection();
//�׷��ȣ�� ���� �ֽ��� �����߽����� �����ϵ�,���࿡ level�� ���� ��쿡��
//step������ ���������� ���ؼ� ���° ���ڵ� ��ȣ�� �����ؼ� �����϶�.
			sql = "select * from TDL_POST order by TP_num limit ?,?";// 1,10
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, start - 1);
			;// mysql�� ���ڵ������ ���������� 0���� ����
			pstmt.setInt(2, end);
			rs = pstmt.executeQuery();
//������ ���ڵ�ܿ� �߰��� ���ڵ带 ÷���ؼ� �ٰ��� �����ִ� ���� -> ��������(����) 
			if (rs.next()) {// ���ڵ尡 �����Ѵٸ� (�ּҸ��� 1��)
//articleList=new List(); X
//����) articleList=new �ڽ�Ŭ������();
				articleList = new ArrayList(end);// 10 -> end ������ŭ �����͸� ���� ���������϶�
				do {
					
					TDLPostDTO article=makeArticleFromResult(); //�ߺ� �޼��� ���� �ҷ�����
					/*
					TDLPostDTO article = new TDLPostDTO();// MemberDTO mem=new MemberDTO();
					article.setTP_num(rs.getInt("num"));//�����Խ��� ��ȣ
					article.setTP_title(rs.getString("TP_title"));//�����Խ��� ����
					article.setTP_id(rs.getString("TP_id"));// �������̵�
					article.setTP_date(rs.getString("TP_date"));// �ۼ���
					article.setTP_content(rs.getString("TP_content")); //�����Խ��� �� ����
					*/
//�߰� 
					articleList.add(article);
				} while (rs.next());
			}
		} catch (Exception e) {
			System.out.println("TDLPostDAO getArticles()�޼��� ��������" + e);
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		System.out.println("================ TDLPost  - getArticles() ��");
		return articleList;
	}

	
	//����¡ ó���� ���������ִ� �޼����ۼ�(ListAction Ŭ�������� �ҷ�����)
	//1.ȭ�鿡 �����ִ� ������ ��ȣ 2.ȭ�鿡 ����� ���ڵ尹��
	public Hashtable pageList(String pageNum,int count) {
		System.out.println("================ TDLPost  - pageList() ����");
		//1.����¡ ó������� ������ hashtable��ü�� ����
		Hashtable<String,Integer> pgList = new Hashtable<String,Integer>();
		
		
		//1.Jsp���� ����ߴ� �ڹ��� �ڵ带 ���⿡�� ����
		int pageSize=10;//numperpage; -> �������� �����ִ� �Խù���(=���ڵ��) -> 20���̻�
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
		System.out.println("Hashtable pageList() ���� ���ڵ��(count) -> "+count);
		number=count-(currentPage-1)*pageSize;
		System.out.println("Hashtable pageList() �������� number : "+number);
		 
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
	       //~DAO -> �������� ������ ���õ� �ڵ�-> �׼�Ŭ������ ���� -> view(jsp)�� �������
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
	       System.out.println("================ TDLPost  - pageList() ��");
		return pgList;
	}
	
	
///////�߰�(1) �˻��о߿� ���� �˻�� �Է������� ���ǿ� �����ϴ� ���ڵ尹���� �ʿ�
	
	public int getArticleSearchCount(String search,String searchtext) { 
		System.out.println("================ TDLPost  - getArticleSearchCount() ����");
		int x=0;//���ڵ尹��
		
		try {
			con=pool.getConnection();
			System.out.println("con : "+con);
			//�˻�� �Է����� �������(�˻��о� ����x)
			
			if(search == null  || search=="") {
				sql="select count(*) from TDL_POST";	
			}else { // �˻��о�(����,�ۼ���,����+����)
				if(search.contentEquals("subject_content")) {
					sql="select count(*) from TDL_POST where TP_title like '%"+searchtext
					+"%' or TP_content like '%"+searchtext+"%'";
				}else { //����, �ۼ��� -> �Ű������� �̿��ؼ� �ϳ��� sql ����
					sql="select count(*) from TDL_POST where "+search+" like '%"+searchtext+"%' ";
				}
			}
			System.out.println("getArticleSearchCount �� �˻��� sql=>"+sql);
			//----------------------------------------------------------------------
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next()) {//�����ִ� ����� �ִٸ�
				x=rs.getInt(1);//������ = rs.get�ڷ���(�ʵ�� �Ǵ� �ε�����ȣ)
									 //�ʵ���� �ƴϱ⶧���� select ~ from ���̿� ������ ����
			}
		}catch(Exception e) {
			System.out.println("getArticleSearchCount()�޼��� �������� : "+e);
		}finally {
			pool.freeConnection(con,pstmt,rs);
		}
		System.out.println("================ TDLPost  - getArticleSearchCount() ��");
		return x;
	}
	
	public List getTDLArticles(int start, int end,String search,String searchtext) {// getMemberArticle(int start,int end)
		// id asc name desc 
		System.out.println("================ TDLPost  - getTDLArticles() ����");
		List articleList = null;// ArrayList articleList=null;

		try {
			con = pool.getConnection();
			//-----------------------------------------------------------------------------
			if(search == null || search =="") {
				sql = "select * from TDL_POST order by TP_num desc limit ?,?";// 1,10
			}else {
				if(search.contentEquals("subject_content")) {
					sql="select * from TDL_POST where TP_title like '%"+searchtext
					+"%' or TP_content like '%"+searchtext
					+"%' order by TP_num desc limit ?,?";
				}else { //����, �ۼ��� -> �Ű������� �̿��ؼ� �ϳ��� sql ����
					sql="select * from TDL_POST where "+search+" like '%"+searchtext
					+"%' order by TP_num desc limit ?,?";
				}
			}
			System.out.println("TDLPost getTDLArticles()�� sql => "+sql);
			//-----------------------------------------------------------------------------
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, start - 1);
			;// mysql�� ���ڵ������ ���������� 0���� ����
			pstmt.setInt(2, end);
			rs = pstmt.executeQuery();
//������ ���ڵ�ܿ� �߰��� ���ڵ带 ÷���ؼ� �ٰ��� �����ִ� ���� -> ��������(����) 
			if (rs.next()) {// ���ڵ尡 �����Ѵٸ� (�ּҸ��� 1��)
//articleList=new List(); X
//����) articleList=new �ڽ�Ŭ������();
				articleList = new ArrayList(end);// 10 -> end ������ŭ �����͸� ���� ���������϶�
				do {
					TDLPostDTO article = makeArticleFromResult();
					
					articleList.add(article);
				} while (rs.next());
			}
		} catch (Exception e) {
			System.out.println("getArticles()�޼��� ��������" + e);
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		System.out.println("================ TDLPost  - getTDLArticles() ��");
		return articleList;
	}
	
	
	// �ۼ��� ���ϱ� ---------------------------------------------------------------------------------------------------------
	public String getTP_date() {
		System.out.println("================ TDLPost  - getTP_date() ����");
		String SQL = "SELECT NOW()";
		try {
			PreparedStatement pstmt = con.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString(1);
			}
		}catch(Exception e) {
			System.out.print("TDLPostDAO getTP_date() ��������  ->");
			e.printStackTrace();
		}
		System.out.println("================ TDLPost  - getTP_date() ��");
		return ""; // �����ͺ��̽� ����
	}
	
	
	//---------------�Խ����� �۾��� �� �� �亯�ޱ�-------------------------------------------------------------------------
		//insert into board values(?,,,
		public void insertArticle(TDLPostDTO article) {//~(MemberDTO mem)
			System.out.println("================ TDLPost  - insertArticle() ����");
			//1.article -> �űԱ����� �亯������ ����
			int TP_num=article.getTP_num();//0(�űԱ�����) 0�� �ƴ� ���(�亯��)
			//���̺� �Է��� �Խù� ��ȣ�� ������ ����
			int number=0;
			System.out.println("insertArticle �޼����� ������ num : "+TP_num);
			
			try {
				con=pool.getConnection();
				sql="select count(*) from TDL_POST";//�ִ밪+1=���� ������ �Խù���ȣ
				pstmt=con.prepareStatement(sql);
				rs=pstmt.executeQuery();
				if(rs.next()) {
					number=rs.getInt(1)+1;
				}else {//�� ó���� ���ڵ尡 �Ѱ��� ���ٸ� 
					number=1;
					
				}
				System.out.println("insertArticle()�� ��¥ �� ->"+getTP_date());
				String name ="test";
				//12�� -> num,reg_date,reaconut(���� -> default -> sysdate.now() <- mysql
				sql="insert into TDL_POST(TP_num,TP_title,TP_id,TP_date,TP_content)";
				sql+="values(?,?,?,?,?)";
				pstmt=con.prepareStatement(sql);
				pstmt.setInt(1,number);
				pstmt.setString(2,article.getTP_title());//�������� Setter Method�� �޸𸮿� ����
				pstmt.setString(3,name);
				pstmt.setString(4,getTP_date());//�ۼ���¥
				pstmt.setString(5,article.getTP_content());//�۳���
				
				int insert=pstmt.executeUpdate();
				System.out.println("TDLPostDAO �Խ����� �۾��� ��������(insert) : "+insert );
			}catch(Exception e) {
				System.out.println("TDLPostDAO insertArticle()�޼��� �������� : "+e);
				e.printStackTrace();
			}finally {
				pool.freeConnection(con,pstmt,rs);
			}//finally
			System.out.println("================ TDLPost  - insertArticle() ��");
		}
	
	//------------������Ʈ-----------------------
		
		public TDLPostDTO getArticle(int TP_num) {
			System.out.println("================ TDLPost  - TDLPostDTO getArticle() ����");
			TDLPostDTO article=null;
			
			try {
				con=pool.getConnection();
				
				sql="select * from TDL_POST where TP_num=?";
				pstmt=con.prepareStatement(sql);
				pstmt.setInt(1,TP_num);
				rs=pstmt.executeQuery();
				
				if(rs.next()) {//���ڵ尡 �����Ѵٸ�
					article=makeArticleFromResult();
					
				}
			}catch(Exception e) {
				System.out.println(" getArticle()�޼��� ��������"+e);
			}finally {
				pool.freeConnection(con,pstmt,rs);
			}
			System.out.println("================ TDLPost  - TDLPostDTO getArticle() ��");
			return article;
		}
	

	
	
	//�Խ����� �ۼ����ϱ�   ������Ʈ
	
		//������Ʈ �� �����Խ��� �Խù��� �����ֱ�==============================
		public TDLPostDTO updateGetArticle(int TP_num) { //updateForm.jsp���� ���
			System.out.println("================ TDLPost  - TDLPostDTO updateGetArticle() ����");
			TDLPostDTO article=null;
			
			try {
				con=pool.getConnection();
				sql="select * from TDL_POST where TP_num=?";
				pstmt=con.prepareStatement(sql);
				pstmt.setInt(1,TP_num);
				rs=pstmt.executeQuery();
				
				if(rs.next()) {//���ڵ尡 �����Ѵٸ�
					article=makeArticleFromResult();
				
				}
			}catch(Exception e) {
				System.out.println("TDLPostDAO updateGetArticle()�޼��� ��������"+e);
			}finally {
				pool.freeConnection(con,pstmt,rs);
			}
			System.out.println("================ TDLPost  - TDLPostDTO updateGetArticle() ��");
			return article;
		}
	
	//  �����Խ��� ���� �������� �����ϴ� ���� ============================
		public int updateArticle(TDLPostDTO article) {
			System.out.println("================ TDLPost  -  updateArticle() ����");
			int x=-1;//�Խù��� ������������
			
			try {				

				System.out.println("������Ʈ��ƼŬupdateArticle() "+article.getTP_title());
				System.out.println("������Ʈ��ƼŬupdateArticle() "+article.getTP_content());
				System.out.println("������Ʈ��ƼŬupdateArticle() "+article.getTP_num());
				
				con=pool.getConnection();
				sql="update TDL_POST set TP_title=?,TP_content=? where TP_num=?";
				
				pstmt=con.prepareStatement(sql);
				pstmt.setString(1, article.getTP_title());
				pstmt.setString(2, article.getTP_content());
				pstmt.setInt(3, article.getTP_num());
				
				int update=pstmt.executeUpdate();
				System.out.println("TDLPostDAO �Խ����� �ۼ��� ��������(update) : "+update);//1����
				x=1;					
				
			}catch(Exception e) {
				System.out.println("TDLPostDAO updateArticle()�޼��� �������� : "+e);
			}finally {
				pool.freeConnection(con,pstmt,rs);//��ȣ�� ã�� ������
			}
			System.out.println("================ TDLPost  -  updateArticle() ��");
			return x;
		}
	
		
		//�� ���������ִ� �޼��� -> ȸ��Ż��(����)=>��ȣ�� �����.=> deleteArticle	
		public int deleteArticle(int TP_num) { 
			System.out.println("================ TDLPost  -  deleteArticle() ����");
			int x=-1;//�Խù��� ������������	
			try {
				con=pool.getConnection();
				
						sql="delete from TDL_POST where TP_num=?";
						pstmt=con.prepareStatement(sql);
						pstmt.setInt(1, TP_num);
						int delete=pstmt.executeUpdate();
						System.out.println("TDLPostDAO �Խ����� �ۼ��� ��������(delete) : "+delete);//1����
						x=1;
				//if(rs.next()); -> x=-1;
			}catch(Exception e) {
				System.out.println("TDLPostDAO deleteArticle()�޼��� �������� : "+e);
			}finally {
				pool.freeConnection(con,pstmt,rs);//��ȣ�� ã�� ������
			}
			System.out.println("================ TDLPost  -  deleteArticle() ��");
			return x;
		
		}
		
		
		//�ݺ��Ǵ� �ڵ� ���̱�
		private TDLPostDTO makeArticleFromResult() throws Exception{
			
			TDLPostDTO article = new TDLPostDTO();// MemberDTO mem=new MemberDTO();
			article.setTP_num(rs.getInt("TP_num"));//�����Խ��� ��ȣ
			article.setTP_title(rs.getString("TP_title"));//�����Խ��� ����
			article.setTP_id(rs.getString("TP_id"));// �������̵�
			article.setTP_date(rs.getString("TP_date"));// �ۼ���
			article.setTP_content(rs.getString("TP_content")); //�����Խ��� �� ����
			return article;
		}
		
		
		
		
		
	
}
