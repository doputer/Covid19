package sejong.corona;

import java.sql.SQLException;
import java.util.Vector;

public class UserDAO extends CovidDAO {
	Vector<String> items = null;
	String sql;

	int result;

	public UserDAO() {
		items = new Vector<String>();
		items.add("");
	}

	public UserDTO getUserId(String phone) { // 핸드폰 번호에 해당하는 사용자의 아이디를 반환하는 메소드
		sql = "select id from user where phone = ?";
		UserDTO user = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, phone);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				user = new UserDTO(rs.getInt("id"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return user;
	}

	// 핸드폰 등록이 되어있는지 조회
	public UserDTO searchPhone(String phone) {
		sql = "select name,phone from user where phone = ?";
		UserDTO user = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, phone);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user = new UserDTO(rs.getString("name"), rs.getString("phone"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (user != null)
			return user;
		else
			return null;
	}

	// id가 등록되어 있는지 조회 (증상)
	public UserDTO IsRegister(int id) {
		sql = "select * from user_detail where id = ?";
		UserDTO user = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user = new UserDTO(rs.getString("symptom1"), rs.getString("symptom2"), rs.getString("symptom3"),
						rs.getString("symptom4"), rs.getString("etc"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (user != null)
			return user;
		else
			return null;
	}

	// id가 등록되어 있는지 조회 (선별진료소)
	public boolean IsChoosing(int id) {
		sql = "select * from reserve where id = ?";
		UserDTO user = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user = new UserDTO(rs.getString("hospital"), rs.getString("date"), rs.getString("status"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// RESERVED BEFORE
		if (user != null)
			return true;
		// NO RESERVE
		else
			return false;
	}

	// 이름, 핸드폰 번호
	public void newUser(UserDTO user) {
		sql = "insert into user (name, phone) values(?,?)";

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, user.getName());
			pstmt.setString(2, user.getPhone());

			pstmt.executeUpdate();

			System.out.println("기본 정보 등록 성공");

		} catch (SQLException e) {
			System.out.println("기본 정보 등록 실패");
			e.printStackTrace();
		}
	}

	public void newResult(int id) { // DB에 새로운 예약자를 생성하는 메소드
		sql = "insert into manager_result (user_id) values(?)";

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, id);

			pstmt.executeUpdate();

			System.out.println("진단 생성 성공");

		} catch (SQLException e) {
			System.out.println("진단 생성 실패");
			e.printStackTrace();
		}
	}

	public void updateUser(UserDTO user) { // DB에 저장된 예약자 정보를 갱신하는 메소드(이름, 연락처)
		sql = "update user set name = ? where phone = ?";

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, user.getName());
			pstmt.setString(2, user.getPhone());

			pstmt.executeUpdate();

			System.out.println("기본 정보 수정 성공");

		} catch (SQLException e) {
			System.out.println("기본 정보 수정 실패");
			e.printStackTrace();
		}
	}

	// 주소, 생년월일, 성별, 증상1234, 기타사항
	public void originalUser(UserDTO user, UserDTO userSymptom) {
		// 사용자의 id를 가져와야함
		sql = "insert into user_detail (id, address, birth, gender, symptom1, symptom2, symptom3, symptom4, etc) values(?,?,?,?,?,?,?,?,?)";

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, user.getId());
			pstmt.setString(2, user.getAddress());
			pstmt.setString(3, user.getBirth());
			pstmt.setString(4, user.getGender());
			pstmt.setString(5, userSymptom.getSymptom1());
			pstmt.setString(6, userSymptom.getSymptom2());
			pstmt.setString(7, userSymptom.getSymptom3());
			pstmt.setString(8, userSymptom.getSymptom4());
			pstmt.setString(9, userSymptom.getEtc());

			pstmt.executeUpdate();

			System.out.println("디테일 정보 등록 성공");

		} catch (SQLException e) {
			System.out.println("디테일 정보 등록 실패");
			e.printStackTrace();
		}
	}

	public void updateOriginalUser(UserDTO user, UserDTO userSymptom) { // 이름, 연락처 외에 문진표에서 작성한 정보를 저장
		sql = "update user_detail set address = ? , birth = ? , gender = ? , symptom1 = ? , symptom2 = ?, symptom3 = ?, symptom4 = ?, etc = ? where id = ?";

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, user.getAddress());
			pstmt.setString(2, user.getBirth());
			pstmt.setString(3, user.getGender());
			pstmt.setString(4, userSymptom.getSymptom1());
			pstmt.setString(5, userSymptom.getSymptom2());
			pstmt.setString(6, userSymptom.getSymptom3());
			pstmt.setString(7, userSymptom.getSymptom4());
			pstmt.setString(8, userSymptom.getEtc());
			pstmt.setInt(9, user.getId());

			pstmt.executeUpdate();

			System.out.println("디테일 정보 수정 성공");

		} catch (SQLException e) {
			System.out.println("디테일 정보 수정 실패");
			e.printStackTrace();
		}
	}

	public void chooseReservation(int id, String hospital, String date) { // 선택한 선별 진료소와 일자도 저장하는 메소드
		sql = "insert into reserve (id, hospital, date) values (?,?,?)";

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, id);
			pstmt.setString(2, hospital);
			pstmt.setString(3, date);

			pstmt.executeUpdate();

			System.out.println("선별진료소 날짜 예약 성공");

		} catch (SQLException e) {
			System.out.println("선별진료소 날짜 예약 실패");
			e.printStackTrace();
		}
	}

	public void updateReservation(int id, String hospital, String date) { // 선별 진료소와 일자를 DB에서 갱신하는 메소드
		sql = "update reserve set hospital = ? , date = ? where id = ?";

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, hospital);
			pstmt.setString(2, date);
			pstmt.setInt(3, id);

			pstmt.executeUpdate();

			System.out.println("선별진료소 날짜 예약 수정 성공");

		} catch (SQLException e) {
			System.out.println("선별진료소 날짜 예약 수정 실패");
			e.printStackTrace();
		}
	}

	public UserDTO checkReservation(int id) { // 이미 예약된 사용자인지 DB에서 확인하는 메소드
		sql = "select * from user left join reserve on user.id = reserve.id where reserve.id = ?";

		UserDTO user = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user = new UserDTO(rs.getString("name"), rs.getString("hospital"), rs.getString("date"),
						rs.getString("status"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (user != null)
			return user;
		else
			return null;
	}
}
