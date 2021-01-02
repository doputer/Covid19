package sejong.corona;

import java.sql.*;
import java.util.ArrayList;

public class CovidDAO {
	String jdbcDriver = "com.mysql.jdbc.Driver";
	String jdbcUrl = "jdbc:mysql://127.0.0.1/javadb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	Connection conn = null;

	PreparedStatement pstmt;
	ResultSet rs;

	String sql = "";

	public void connectDB() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, "root", "root");
			System.out.println("드라이버 연결 성공");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void closeDB() {
		try {
			if (pstmt != null)
				pstmt.close();
			if (rs != null)
				rs.close();
			if (conn != null)
				conn.close();
			System.out.println("드라이버 종료 성공");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<UserDTO> getUser(String hospital, String date) {
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("select * from user left join user_detail ");
		sqlSb.append("on user.id = user_detail.id ");
		sqlSb.append("left join reserve ");
		sqlSb.append("on user.id = reserve.id ");
		sqlSb.append("left join manager_result ");
		sqlSb.append("on user.id = manager_result.user_id ");
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
				dto.setResult(rs.getString("result"));

				datas.add(dto);
			}
			return datas;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
