package tdl_likey;

import java.sql.*;//DB���

import java.util.*;//ArrayList,List�� ����ϱ� ���ؼ�
import dbc.DBConnectionMgr; // DBC �ҷ�����
import tdl_Post.*;

public class TDLLikeyDAO { // MemberDAO

	private DBConnectionMgr pool = null;// 1.����

	// �������� ������ ��� �ʿ��� �������
	private Connection con = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private String sql = "";// �����ų SQL ����

	// 2.�����ڸ� ���ؼ� ����=>������
	public TDLLikeyDAO() {
		try {
			pool = DBConnectionMgr.getInstance();
			System.out.println("pool : " + pool);
		} catch (Exception e) {
			System.out.println("TDLCommentDAO -> DB���ӿ��� : " + e);
		}
	}// ������

	
	public int likeCount(String TPC_addr) { //�ش� ����� ���ƿ� 
		int x=0;//���ڵ尹��
		try {
			con=pool.getConnection();
			System.out.println("likeCount ȣ��");
			sql="select count(*) from TDL_LIKEY where TL_like like '?%'";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1,TPC_addr ); // ���ƿ�� ����Ϸù�ȣ+id�� ����Ǿ��־ 
													//like ����Ϸù�ȣ% �� �˻��ϸ� ���� ����
			rs=pstmt.executeQuery();
			if(rs.next()) {//�����ִ� ����� �ִٸ�
				x=rs.getInt(1);//������ = rs.get�ڷ���(�ʵ�� �Ǵ� �ε�����ȣ)
									 //�ʵ���� �ƴϱ⶧���� select ~ from ���̿� ������ ����
			}
		}catch(Exception e) {
			System.out.println("TL_likeCount()�޼��� �������� : "+e);
		}finally {
			pool.freeConnection(con,pstmt,rs);
		}
		return x;
	}
	
	public int badCount(String TPC_addr) { //�ش� ����� ���ƿ� 
		int x=0;//���ڵ尹��
		try {
			con=pool.getConnection();
			System.out.println("badCount ȣ��");
			sql="select count(*) from TDL_LIKEY where TL_bad like '?%'";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1,TPC_addr ); // �Ⱦ��� ����Ϸù�ȣ+id�� ����Ǿ��־ 
													//like ����Ϸù�ȣ% �� �˻��ϸ� ���� ����
			rs=pstmt.executeQuery();
			if(rs.next()) {//�����ִ� ����� �ִٸ�
				x=rs.getInt(1);//������ = rs.get�ڷ���(�ʵ�� �Ǵ� �ε�����ȣ)
									 //�ʵ���� �ƴϱ⶧���� select ~ from ���̿� ������ ����
			}
		}catch(Exception e) {
			System.out.println("TL_badCount()�޼��� �������� : "+e);
		}finally {
			pool.freeConnection(con,pstmt,rs);
		}
		return x;
	}
	
	
	//���ƿ� üũ�κ�
	public int like(String TPC_addr, String userID) {
		
		String addr = TPC_addr + userID;//����Ϸù�ȣ+���̵����� primary key 
		
		try {// ���ƿ�� ��۴� �� ���� �����ؼ� �̹� ���ƿ並 �������� Ȯ�� 
			con=pool.getConnection();
			sql = "select TL_like from TDL_LIKEY where TL_like='?'";//select������ �˻�
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, addr);
			rs = pstmt.executeQuery();
			if (rs.next()) { //�̹� ���ƿ䰡 �ִٸ� ���ƿ� ����ϰ� ����� -> toggle ���. ���ƿ� -> ���ƿ� ��� -> ���ƿ� ,,,,
				System.out.println("TL_like ���ƿ� ���");
				con=pool.getConnection();
				sql = "delete from TL_likey where TL_like='?'"; // delete������ �ش� ���ƿ並 �����ϱ� -> ���
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, addr);
				return pstmt.executeUpdate();
			} else { // ���� �˻��� ������ ���ٸ� ���ƿ� �߰��ϱ�
				try {
					con=pool.getConnection();
					sql = "INSERT INTO TL_LIKEY(TL_like) VALUES (?)";
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, addr);
					return pstmt.executeUpdate();
				} catch (SQLException e) {
					System.out.println("TL_like insert����");
					e.printStackTrace();
					return 1;//insert �κп��� ������ ���ٸ� ��ȯ�� 1
				}		
			}
		} catch (Exception e1) {
			System.out.println("like ����1");
			e1.printStackTrace();
		}
		System.out.println("TL_like -1");
		return 2;	 //select ���� ������ ���Դٸ� ��ȯ�� 2 
	}	
	
	
	// �Ⱦ�� üũ �κ�
	public int bad(String TPC_addr, String userID) {
		String addr = TPC_addr + userID;
		try {
			con=pool.getConnection();
			sql = "select TL_bad from TDL_LIKEY where TL_bad='?'";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, addr);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				System.out.println("TL_bad �Ⱦ�� ���");
				con=pool.getConnection();
				sql = "delete from TL_likey where TL_bad='?'";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, addr);
				return pstmt.executeUpdate();
				
			} else {
				try {
					con=pool.getConnection();
					sql = "INSERT INTO TL_LIKEY(TL_bad) VALUES (?)";
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, addr);
					return pstmt.executeUpdate();

				} catch (SQLException e) {
					System.out.println("TL_like insert����");
					e.printStackTrace();
				}

			}
		} catch (Exception e1) {
			System.out.println("like ����1");
			e1.printStackTrace();
		}
		System.out.println("TL_like -1");
		return -1;	
	}	
	
}
