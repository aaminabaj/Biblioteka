package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BazaUtil {
    // Povezivanje s cloud bazom
    private static final String URL =
            "jdbc:mysql://avnadmin:AVNS_0OX4Wx_TboyvqLHCRc1@mysql-biblioteka-size-biblioteka-ahjf.b.aivencloud.com:26743/defaultdb?ssl-mode=REQUIRED";

    private static final String USER = "avnadmin";
    private static final String PASS = "AVNS_0OX4Wx_TboyvqLHCRc1";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
