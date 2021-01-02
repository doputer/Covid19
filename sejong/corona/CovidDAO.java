package sejong.corona;

import java.sql.*;

public class CovidDAO {
	String jdbcDriver = "com.mysql.jdbc.Driver";
	String jdbcUrl = "jdbc:mysql://127.0.0.1:1105/javadb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	Connection conn = null;

	PreparedStatement pstmt;
	ResultSet rs;

	String sql = "";

	public void connectDB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, "root", "971105");
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
}
