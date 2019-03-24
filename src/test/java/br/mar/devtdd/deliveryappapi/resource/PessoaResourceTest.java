package br.mar.devtdd.deliveryappapi.resource;

import static org.hamcrest.CoreMatchers.equalTo;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.hibernate.mapping.Array;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import br.mar.devtdd.deliveryappapi.DeliveryAppApiApplicationTests;
import br.mar.devtdd.deliveryappapi.model.Pessoa;
import br.mar.devtdd.deliveryappapi.model.Telefone;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
public class PessoaResourceTest extends DeliveryAppApiApplicationTests{
	
	@Test
	public void deve_procurar_pessoa_pelo_ddd_e_numero_telefone() throws Exception{
		RestAssured.given()
					.pathParam("ddd", "86")
					.pathParam("numero", "35006330")
				.get("/pessoas/{ddd}/{numero}")
					.then()
						.log().body().and()
						.statusCode(HttpStatus.OK.value())
						.body("codigo", equalTo(3),
								"nome", equalTo("Cauê"),
								"cpf", equalTo("38767897100"));			
	}
	
	@Test
	public void deve_retornar_erro_nao_encontrado_quando_buscar_pessoa_por_telefone_inexistente() throws Exception {
		RestAssured.given()
				.pathParam("ddd", "99")
				.pathParam("numero", "987654321")
			.get("/pessoas/{ddd}/{numero}")
				.then()
					.log().body().and()
					.statusCode(HttpStatus.NOT_FOUND.value())
					.body("erro", equalTo("Não existe pessoa com o telefone (99)987654321"));
	}
	
	
	@Test
	public void  deve_salvar_nova_pessao() throws Exception{
		final Pessoa pessoa  = new Pessoa();
		
		pessoa.setNome("Isaac Luiz");
		pessoa.setCpf("22284431070");
		
		final Telefone telefone = new Telefone();
		telefone.setDdd("55");
		telefone.setNumero("2728257995");
		
		List<Telefone> telefones = new ArrayList<Telefone>();
		telefones.add(telefone);
		
		pessoa.setTelefones(telefones);		
		
		RestAssured.given()
			.request()
			.header("Accept", ContentType.ANY)
			.header("Content-type", ContentType.JSON)
			.body(pessoa)
		.when()
		.post("/pessoas")
		.then()
				.log().headers()
			.and()
				.log().body()
			.and()
			.statusCode(HttpStatus.CREATED.value())
			.header("Location", equalTo("http://localhost:"+porta+"/pessoas/55/2728257995"))
			.body("nome", equalTo("Isaac Luiz"),
					"cpf", equalTo("22284431070"));
			
		
				
				
	}
	
	

}
