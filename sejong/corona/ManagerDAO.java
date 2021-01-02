package sejong.corona;

import java.sql.*;
import java.util.*;

public class ManagerDAO extends CovidDAO {
	public ManagerDAO() {
		connectDB();
	}

	public ArrayList<UserDTO> getAllUser() {
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("select * from user left join user_detail ");
		sqlSb.append("on user.id = user_detail.id ");
		sqlSb.append("left join reserve ");
		sqlSb.append("on user.id = reserve.id");

		sql = sqlSb.toString();

		try {
			ArrayList<UserDTO> datas = new ArrayList<UserDTO>();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				UserDTO dto = new UserDTO();
				dto.setId(Integer.parseInt(rs.getString("id")));
				dto.setName(rs.getString("name"));
				dto.setPhone(rs.getString("phone"));
				dto.setAddress(rs.getString("address"));
				dto.setBirth(rs.getString("birth"));
				dto.setGender(rs.getString("gender"));
				dto.setSymptom1(rs.getString("symptom1"));
				dto.setSymptom2(rs.getString("symptom2"));
				dto.setSymptom3(rs.getString("symptom3"));
				dto.setSymptom4(rs.getString("symptom4"));
				dto.setEtc(rs.getString("etc"));
				dto.setHospital(rs.getString("hospital"));
				dto.setDate(rs.getString("date"));
				dto.setStatus(rs.getString("status"));

				datas.add(dto);
			}
			return datas;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<UserDTO> getUser(String hospital, String date) {
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("select * from user left join user_detail ");
		sqlSb.append("on user.id = user_detail.id ");
		sqlSb.append("left join reserve ");
		sqlSb.append("on user.id = reserve.id ");
		if (date == "") {
			sqlSb.append("where hospital = '" + hospital + "'");
		} else if (hospital == "") {
			sqlSb.append("where date = '" + date + "'");
		} else {
			sqlSb.append("where hospital = '" + hospital + "'");
			sqlSb.append("and date = '" + date + "'");
		}

		sql = sqlSb.toString();

		try {
			ArrayList<UserDTO> datas = new ArrayList<UserDTO>();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				UserDTO dto = new UserDTO();
				dto.setId(Integer.parseInt(rs.getString("id")));
				dto.setName(rs.getString("name"));
				dto.setPhone(rs.getString("phone"));
				dto.setAddress(rs.getString("address"));
				dto.setBirth(rs.getString("birth"));
				dto.setGender(rs.getString("gender"));
				dto.setSymptom1(rs.getString("symptom1"));
				dto.setSymptom2(rs.getString("symptom2"));
				dto.setSymptom3(rs.getString("symptom3"));
				dto.setSymptom4(rs.getString("symptom4"));
				dto.setEtc(rs.getString("etc"));
				dto.setHospital(rs.getString("hospital"));
				dto.setDate(rs.getString("date"));
				dto.setStatus(rs.getString("status"));

				datas.add(dto);
			}
			return datas;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void updateUser(UserDTO t) {
		sql = "UPDATE manager_result SET result = ? WHERE id = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, String.valueOf(t.getResult()));
			pstmt.setString(2, String.valueOf(t.getId()));
			int sucess = pstmt.executeUpdate();

			if (sucess > 0)
				System.out.println(t.getName() + " 수정 성공");
			else
				System.out.println(t.getName() + " 수정 실패");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteUser(int code) {
		try {
			sql = "DELETE FROM user WHERE id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, String.valueOf(code));

			int sucess = pstmt.executeUpdate();
			if (sucess > 0)
				System.out.println(code + " 삭제 성공");
			else
				System.out.println(code + "삭제 실패");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteAllUser() {
		try {
			sql = "TRUNCATE manager_result" + "TRUNCATE reserve" + "TRUNCATE user_detail" + "TRUNCATE user";
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate(sql);

			int sucess = pstmt.executeUpdate();
			if (sucess > 0)
				System.out.println("전체 삭제 성공");
			else
				System.out.println("전체 삭제 실패");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
