package sejong.corona;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.util.*;

public class ManagerDAO extends CovidDAO {
	ManagerView view;

	public ManagerDAO() {
//		connectDB();
	}

	public ArrayList<UserDTO> getAllUser() {
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("select * from user left join user_detail ");
		sqlSb.append("on user.id = user_detail.id ");
		sqlSb.append("left join reserve ");
		sqlSb.append("on user.id = reserve.id ");
		sqlSb.append("left join manager_result ");
		sqlSb.append("on user.id = manager_result.user_id");

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
				dto.setResult(rs.getString("result"));

				datas.add(dto);
			}
			return datas;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void updateUser(int id, String result, String hospital, String date) {
		StringBuilder sb = new StringBuilder();
		sql = sb.append("update manager_result set").append(" result = '" + result + "'")
				.append(" where user_id = " + id + ";").toString();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		sb = new StringBuilder();
		sql = sb.append("update reserve set").append(" status = '예약완료',")
				.append(" hospital = '" + hospital + "',")
				.append(" date = '" + date + "'")
				.append(" where id = " + id + ";").toString();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
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
