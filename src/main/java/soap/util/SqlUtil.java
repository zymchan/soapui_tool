package soap.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;


public class SqlUtil {
    private static final Logger logger = Logger.getLogger(SqlUtil.class.getName());

    private static Connection getConnection() throws Exception {
        try {
            String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            Class.forName(driverName);
            String dbURL = "jdbc:sqlserver://VQWD1175\\SQL8DEV:1433;DatabaseName=TestAutomation";
            String userName = "TestAutomation";
            String userPwd = "Auto123#";
            return DriverManager.getConnection(dbURL, userName, userPwd);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("---------------sql connect failed---------------");
        }
        return null;
    }

    private static ResultSet query(String SQL) {
        try {
            logger.info("SQL executeQuery:   " + SQL);
            Connection conn = getConnection();
            Statement stmt = null;
            if (conn != null) {
                stmt = conn.createStatement();
                return stmt.executeQuery(SQL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("---------------sql query failed---------------");
        }
        return null;
    }

    public static ArrayList<HashMap<String, Object>> executeQuery(String SQL) {
        ResultSet rs = query(SQL);
        ArrayList<HashMap<String, Object>> array = new ArrayList<HashMap<String, Object>>();
        try {
            ResultSetMetaData data;
            if (rs != null) {
                data = rs.getMetaData();
                while (rs.next()) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    for (int i = 1; i <= data.getColumnCount(); i++) {
                        String key = data.getColumnName(i);
                        Object value = rs.getObject(i);
                        map.put(key, value);
                    }
                    array.add(map);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return array;

    }

    public static boolean executeUpdate(String SQL) {
        try {
            logger.info("SQL executeUpdate:  " + SQL);
            Connection conn = getConnection();
            Statement st = null;
            if (conn != null) {
                st = conn.createStatement();
                int result = st.executeUpdate(SQL);
                if (result > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("---------------sql update failed---------------");
        }
        return false;
    }

    public static void main(String[] args) {

        String sql = "select * from ZYM_TESTCASE where id = 1";
        ArrayList<HashMap<String, Object>> rs = executeQuery(sql);
        System.out.println("rs.size\n" + rs.size());
        System.out.println("result\n" + rs.get(0).get("steps"));

    }

}