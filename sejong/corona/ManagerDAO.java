package sejong.corona;

import java.sql.*;
import java.util.*;

public class ManagerDAO extends CovidDAO {
	Vector<String> items = null;

	public ManagerDAO() {
		connectDB();
	}

	public ArrayList<UserDTO> getAllUser() {
		try {
			ArrayList<UserDTO> datas = new ArrayList<UserDTO>();
			sql = "SELECT * FROM user join user_detail";
			pstmt = conn.prepareStatement(sql);
			items = new Vector<String>();
			items.add("진료소");

			rs = pstmt.executeQuery();
			while (rs.next()) {
				UserDTO ma = new UserDTO();
				ma.setId(Integer.parseInt(rs.getString("id")));
				ma.setHospital(rs.getString("hospital"));
				ma.setDate(rs.getString("_date"));
				ma.setSymptom1(rs.getString("_status"));

				// datas.addAll(ma);
				items.add(String.valueOf(rs.getInt("id")));
			}
			return datas;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<UserDTO> getUser() {
		try {
			ArrayList<UserDTO> datas = new ArrayList<UserDTO>();
			sql = "SELECT ?, ?, ? FROM reserve";
			pstmt = conn.prepareStatement(sql);
			items = new Vector<String>();
			items.add("진료소");

			rs = pstmt.executeQuery();
			while (rs.next()) {
				UserDTO ma = new UserDTO();
				ma.setId(Integer.parseInt(rs.getString("id")));
				ma.setHospital(rs.getString("hospital"));
				ma.setDate(rs.getString("_date"));
				ma.setSymptom1(rs.getString("_status"));

				// datas.addAll(ma);
				items.add(String.valueOf(rs.getInt("id")));
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
