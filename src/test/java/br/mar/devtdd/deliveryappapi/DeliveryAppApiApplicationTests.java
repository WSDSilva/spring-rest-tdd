package br.mar.devtdd.deliveryappapi;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;

@Sql(value="/load-database.sql", executionPhase=Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value="/clean-database.sql", executionPhase=Sql.ExecutionPhase.AFTER_TEST_METHOD)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public abstract class DeliveryAppApiApplicationTests {
	
	@Value("${local.server.port}")
	protected int porta;
	
	@Before
	public void setUp() throws Exception{
		RestAssured.port = porta;
	}

}
