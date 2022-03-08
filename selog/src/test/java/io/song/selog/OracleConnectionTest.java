package io.song.selog;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Test;

import lombok.extern.log4j.Log4j;

@Log4j
public class OracleConnectionTest {
	
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver"; 
	private static final String URL = "jdbc:oracle:thin:@192.168.4.177:1521:XE";
	private static final String USER = "selog";
	private static final String PW = "selog";
	
	static {
		try {
			Class.forName(DRIVER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test() {
		try(Connection conn = DriverManager.getConnection(URL, USER, PW)) {
			log.info(conn);
			System.out.println(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
