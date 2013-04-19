import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
public class SQLConnect {
	SQLConnect(){}
	public void connect(){
		Connection conn=null;
		Statement stmt=null;
		ResultSet rs=null;
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn=DriverManager.getConnection("jdbc:mysql://localhost/QSO?user=Brad&password=intech2010");
			stmt=conn.createStatement();
			rs=stmt.executeQuery("select * from Callsign");
			while(rs.next()){
				System.out.print("|"+rs.getRow());
				for(int i=1;i<8;i++){
					System.out.print(" "+rs.getString(i));
				}
				System.out.print(" "+rs.getString(8)+"\n");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
