package tdl_likey;

import java.sql.*;//DB사용

import java.util.*;//ArrayList,List를 사용하기 위해서
import dbc.DBConnectionMgr; // DBC 불러오기
import tdl_Post.*;

public class TDLLikeyDAO { // MemberDAO

	private DBConnectionMgr pool = null;// 1.선언

	// 공통으로 접속할 경우 필요한 멤버변수
	private Connection con = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private String sql = "";// 실행시킬 SQL 구문

	// 2.생성자를 통해서 연결=>의존성
	public TDLLikeyDAO() {
		try {
			pool = DBConnectionMgr.getInstance();
			System.out.println("pool : " + pool);
		} catch (Exception e) {
			System.out.println("TDLCommentDAO -> DB접속오류 : " + e);
		}
	}// 생성자

	
	public int likeCount(String TPC_addr) { //해당 댓글의 좋아요 
		int x=0;//레코드갯수
		try {
			con=pool.getConnection();
			System.out.println("likeCount 호출");
			sql="select count(*) from TDL_LIKEY where TL_like like '?%'";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1,TPC_addr ); // 좋아요는 댓글일련번호+id로 저장되어있어서 
													//like 댓글일련번호% 로 검색하면 수가 나옴
			rs=pstmt.executeQuery();
			if(rs.next()) {//보여주는 결과가 있다면
				x=rs.getInt(1);//변수명 = rs.get자료형(필드명 또는 인덱스번호)
									 //필드명이 아니기때문에 select ~ from 사이에 나오는 순서
			}
		}catch(Exception e) {
			System.out.println("TL_likeCount()메서드 에러유발 : "+e);
		}finally {
			pool.freeConnection(con,pstmt,rs);
		}
		return x;
	}
	
	public int badCount(String TPC_addr) { //해당 댓글의 좋아요 
		int x=0;//레코드갯수
		try {
			con=pool.getConnection();
			System.out.println("badCount 호출");
			sql="select count(*) from TDL_LIKEY where TL_bad like '?%'";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1,TPC_addr ); // 싫어요는 댓글일련번호+id로 저장되어있어서 
													//like 댓글일련번호% 로 검색하면 수가 나옴
			rs=pstmt.executeQuery();
			if(rs.next()) {//보여주는 결과가 있다면
				x=rs.getInt(1);//변수명 = rs.get자료형(필드명 또는 인덱스번호)
									 //필드명이 아니기때문에 select ~ from 사이에 나오는 순서
			}
		}catch(Exception e) {
			System.out.println("TL_badCount()메서드 에러유발 : "+e);
		}finally {
			pool.freeConnection(con,pstmt,rs);
		}
		return x;
	}
	
	
	//좋아요 체크부분
	public int like(String TPC_addr, String userID) {
		
		String addr = TPC_addr + userID;//댓글일련번호+아이디값으로 primary key 
		
		try {// 좋아요는 댓글당 한 번씩 가능해서 이미 좋아요를 눌렀는지 확인 
			con=pool.getConnection();
			sql = "select TL_like from TDL_LIKEY where TL_like='?'";//select문으로 검색
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, addr);
			rs = pstmt.executeQuery();
			if (rs.next()) { //이미 좋아요가 있다면 좋아요 취소하게 만들기 -> toggle 기능. 좋아요 -> 좋아요 취소 -> 좋아요 ,,,,
				System.out.println("TL_like 좋아요 취소");
				con=pool.getConnection();
				sql = "delete from TL_likey where TL_like='?'"; // delete문으로 해당 좋아요를 삭제하기 -> 취소
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, addr);
				return pstmt.executeUpdate();
			} else { // 만약 검색된 내용이 없다면 좋아요 추가하기
				try {
					con=pool.getConnection();
					sql = "INSERT INTO TL_LIKEY(TL_like) VALUES (?)";
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, addr);
					return pstmt.executeUpdate();
				} catch (SQLException e) {
					System.out.println("TL_like insert오류");
					e.printStackTrace();
					return 1;//insert 부분에서 에러가 났다면 반환값 1
				}		
			}
		} catch (Exception e1) {
			System.out.println("like 에러1");
			e1.printStackTrace();
		}
		System.out.println("TL_like -1");
		return 2;	 //select 에서 에러가 나왔다면 반환값 2 
	}	
	
	
	// 싫어요 체크 부분
	public int bad(String TPC_addr, String userID) {
		String addr = TPC_addr + userID;
		try {
			con=pool.getConnection();
			sql = "select TL_bad from TDL_LIKEY where TL_bad='?'";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, addr);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				System.out.println("TL_bad 싫어요 취소");
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
					System.out.println("TL_like insert오류");
					e.printStackTrace();
				}

			}
		} catch (Exception e1) {
			System.out.println("like 에러1");
			e1.printStackTrace();
		}
		System.out.println("TL_like -1");
		return -1;	
	}	
	
}
