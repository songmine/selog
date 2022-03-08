package io.song.selog;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class MyBatisTest {

	@Autowired
	private SqlSessionFactory sqlFactory;
	
	@Test
	public void testFactory() {
		log.info(sqlFactory);
	}
	
	@Test
	public void testSession() {
		try(SqlSession session = sqlFactory.openSession()) {
			log.info(session);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
