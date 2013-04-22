import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mysql.jdbc.PreparedStatement;

public class SQLConnect {
	Object[][] data;

	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	int length;

	SQLConnect() {
		connect();
	}

	public void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager
					.getConnection("jdbc:mysql://localhost/QSO?user=Brad&password=intech2010");
			stmt = conn.createStatement();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public boolean commit(GUI in) {
		try {
			PreparedStatement ps = (PreparedStatement) conn
					.prepareStatement("INSERT INTO contact (CallSign,Date,Time,Freq,Mode) VALUES (?,?,?,?,?)");
			String temp[] = quaryMake(in);
			ps.setString(1, temp[0]);
			ps.setString(2, temp[1]);
			ps.setString(3, temp[2]);
			ps.setString(4, temp[3]);
			ps.setString(5, temp[4]);
			ps.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	public String[] quaryMake(GUI in) {
		String q[] = new String[length];

		if (in.CallSign.getText() == null) {
			return null;
		} else if (in.Freq.getText() == null) {
			return null;
		} else if (in.Time.getText() == null) {
			in.Time.setText(String.valueOf(in.getTime()));
		} else if (in.DATE.getText() == null) {
			in.DATE.setText(in.getDate().toString());
		}

		q[0] = in.CallSign.getText();
		q[1] = in.DATE.getText();
		q[2] = in.Time.getText();
		q[3] = in.Freq.getText();
		q[4] = "1";

		return q;
	}

	public Object[][] getData(){
		Object[][] data;
		try {
			rs = stmt.executeQuery("select count(PK) from contact");
			rs.next();
			length = Integer.parseInt(rs.getString(1));
			data = new Object[Integer.parseInt(rs.getString(1))][5];
			rs = stmt.executeQuery("select * from contact");
			while (rs.next()) {
				Object[] temp = new Object[5];
				for (int i = 1; i < 6; i++) {
					data[rs.getRow() - 1][i - 1] = rs.getObject(i + 1);
				}
			}
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error on data get");
		}
		return null;
	}
}
