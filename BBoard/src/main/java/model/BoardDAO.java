package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {
	
	Connection con; // 데이터베이스에 접근할수있도록 설정
	PreparedStatement pstmt; // 데이터베이스에서 쿼리를 실행시켜주는 객체
	ResultSet rs; // 데이터베이스의 테이블의 결과를 리턴받아 자바에 저장해주는 객체
	
	// 데이터베이스에 접근할수 있도록 도와주는 메소드
		public void getCon() {
			
			//커넥션 풀을 이용하여 데이터 베이스에 접근
			try {
				//외부에서 데이터를 읽어야 하기에
				Context initctx = new InitialContext();
				//톰캣 서버에 정보를 담아놓은 곳으로 이동
				Context envctx = (Context) initctx.lookup("java:comp/env");
				//데이터 소스 객체를 선언
				DataSource ds = (DataSource) envctx.lookup("jdbc/pool");
				//데이터 소스를 기준으로 커넥션 연결
				con = ds.getConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
		
		// 하나의 새로운 게시글이 넘어와서 저장되는 메소드
		public void insertBoard(BoardBean bean) {
			
			getCon();
			// 빈클래스에 넘어오지 않았던 데이터들을 초기화 해주어야한다.
			int ref = 0; // 글그룹을 의미 = 쿼리를 실행시켜서 가장 큰 ref값을 가져온후 +1을 더해주면됌
			int re_step = 1; // 새 글이기에 = 부모글이기에
			int re_level = 1;
			
			try {
				// 가장 큰 ref값을 읽어오는 쿼리 준비
				String refsql = "select max(ref) from board";
				// 쿼리 실행객체
				pstmt = con.prepareStatement(refsql);
				// 쿼리 실행 후 결과를 리턴
				rs = pstmt.executeQuery();
				if (rs.next()) { //결과값이 있다면
					ref = rs.getInt(1) + 1; // 최대값에 + 1을 더해서 글 그룹을 설정
				}
				// 실제로 게시글 전체값을 데이터 테이블에 저장
				String sql = "insert into board values(board_seq.NEXTVAL,?,?,?,?,sysdate, ?, ?, ?, 0, ?)";
				pstmt = con.prepareStatement(sql);
				// ?에 값을 매핑
				pstmt.setString(1, bean.getWriter());
				pstmt.setString(2, bean.getEmail());
				pstmt.setString(3, bean.getSubject());
				pstmt.setString(4, bean.getPassword());
				
				pstmt.setInt(5, ref);
				pstmt.setInt(6, re_step);
				pstmt.setInt(7, re_level);
				pstmt.setString(8, bean.getContent());
				
				// 쿼리를 실행하시오
				pstmt.executeUpdate();
				// 자원 반납
				con.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// 모든 게시글을 리턴해주는 메소드 작성
		public Vector<BoardBean> getAllBoard() {
			
			// 리턴할 객체 선언
			Vector<BoardBean> v = new Vector<>();
			
			getCon();
			
			try {
				// 쿼리준비
				String sql = "select * from board order by ref desc , re_step asc";
				// 쿼리를 실행할 객체 선언
				pstmt = con.prepareStatement(sql);
				// 쿼리 실행 후 결과 저장
				rs = pstmt.executeQuery();
				// 데이터 객수가 몇개인지 모르기에 반복문을 이용하여 추출
				while (rs.next()) {
					// 데이터를 패키징  (가방 = BoardBean 클래스를 이용)
					BoardBean bean = new BoardBean();
					bean.setNum(rs.getInt(1));
					bean.setWriter(rs.getString(2));
					bean.setEmail(rs.getString(3));
					bean.setSubject(rs.getString(4));
					bean.setPassword(rs.getString(5));
					bean.setReg_date(rs.getDate(6).toString());
					bean.setRef(rs.getInt(7));
					bean.setRe_step(rs.getInt(8));
					bean.setRe_level(rs.getInt(9));
					bean.setReadcount(rs.getInt(10));
					bean.setContent(rs.getString(11));
					// 패키징 한 데이터를 벡터에 저장
					v.add(bean);
					
				}
				con.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return v;
		}
		
		// BoardInfo 하나의 게시글을 리턴하는 메소드
		public BoardBean getOneBoard(int num) {
			
			// 리턴 타입 선언
			BoardBean bean = new BoardBean();
			getCon();
			
			try {
				// 조회수 증가 쿼리
				String readsql = "update board set readcount = readcount + 1 where num = ?";
				pstmt = con.prepareStatement(readsql);
				pstmt.setInt(1, num);
				pstmt.executeUpdate();
				
				// 쿼리 준비
				String sql = "select * from board where num = ?";
				// 쿼리 실행 객체 선언
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, num);
				// 쿼리 실행 후 결과를 리턴
				rs = pstmt.executeQuery();
				if (rs.next()) {
					bean.setNum(rs.getInt(1));
					bean.setWriter(rs.getString(2));
					bean.setEmail(rs.getString(3));
					bean.setSubject(rs.getString(4));
					bean.setPassword(rs.getString(5));
					bean.setReg_date(rs.getDate(6).toString());
					bean.setRef(rs.getInt(7));
					bean.setRe_step(rs.getInt(8));
					bean.setRe_level(rs.getInt(9));
					bean.setReadcount(rs.getInt(10));
					bean.setContent(rs.getString(11));
				}
				
				con.close();
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return bean;
		}
		
		// 답변글이 저장되는 메소드
		public void reWriteBoard(BoardBean bean) {
			// 부모 글그룹과 글레벨 글스텝을 읽어드림
			int ref = bean.getRef();
			int re_step = bean.getRe_step();
			int re_level = bean.getRe_level();
			
			getCon();
			
			try {
				////////////////////// 핵심 코드 ///////////////////////
				//부모 글보다 큰 re_level의 값을 전부 1씩 증가시켜준다
				String levelsql = "update board set re_level=re_level+1 where ref=? and re_level > ?";
				// 쿼리 실행 객체 선언
				pstmt = con.prepareStatement(levelsql);
				pstmt.setInt(1, ref);
				pstmt.setInt(2, re_level);
				// 쿼리 실행
				pstmt.executeUpdate();
				//답변글 데이터를 저장
				String sql = "insert into board values(board_seq.NEXTVAL, ?, ?, ?, ?, sysdate, ?, ?, ?, 0, ?)";
				pstmt = con.prepareStatement(sql);
				// ?에 값을 대입
				pstmt.setString(1, bean.getWriter());
				pstmt.setString(2, bean.getEmail());
				pstmt.setString(3, bean.getSubject());
				pstmt.setString(4, bean.getPassword());
				pstmt.setInt(5, ref); // 부모의 ref값을 넣어줌
				pstmt.setInt(6, re_step + 1); // 답글이기에 부모 글 re_step 에 1을 더해줌
				pstmt.setInt(7, re_level + 1);
				pstmt.setString(8, bean.getContent());
				
				pstmt.executeUpdate();
				
				con.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// BoardUpdate용 Delete시 하나의 게시글을 리턴
		public BoardBean getOneUpdateBoard(int num) {
			
			// 리턴 타입 선언
			BoardBean bean = new BoardBean();
			getCon();
			
			try {
				
				// 쿼리 준비
				String sql = "select * from board where num = ?";
				// 쿼리 실행 객체 선언
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, num);
				// 쿼리 실행 후 결과를 리턴
				rs = pstmt.executeQuery();
				if (rs.next()) {
					bean.setNum(rs.getInt(1));
					bean.setWriter(rs.getString(2));
					bean.setEmail(rs.getString(3));
					bean.setSubject(rs.getString(4));
					bean.setPassword(rs.getString(5));
					bean.setReg_date(rs.getDate(6).toString());
					bean.setRef(rs.getInt(7));
					bean.setRe_step(rs.getInt(8));
					bean.setRe_level(rs.getInt(9));
					bean.setReadcount(rs.getInt(10));
					bean.setContent(rs.getString(11));
				}
				
				con.close();
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return bean;
		}
		
		// update 와 delete시 필요한 패스워드값을 리턴
		public String getPass(int num) {
			// 리턴할 변수 객체 선언
			String pass = "";
			getCon();
			try {
				
				//쿼리 준비
				String sql = "select password from board where num = ?";
				// 쿼리 실행 객체 선언
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, num);
				rs = pstmt.executeQuery();
				// 패스워드값을 저장
				if (rs.next()) {
					pass = rs.getString(1);
				}
				// 자원반납
				con.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return pass;
		}
		
		// 하나의 게시글을 수정하는 메소드
		public void updateBoard(BoardBean bean) {
			
			getCon();
			
			try {
				
				// 쿼리준비
				String sql = "update board set subject=?, content=? where num=?";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, bean.getSubject());
				pstmt.setString(2, bean.getContent());
				pstmt.setInt(3, bean.getNum());
				
				pstmt.executeUpdate();
				
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// 하나의 게시글을 삭제하는 메소드
		public void deleteBoard(int num) {
			
			getCon();
			
			try {
				
				// 쿼리준비
				String sql = "delete from board where num = ?";
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, num);
				pstmt.executeUpdate();
				
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
}
