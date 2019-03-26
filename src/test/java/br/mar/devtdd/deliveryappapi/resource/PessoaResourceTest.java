package br.mar.devtdd.deliveryappapi.resource;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import br.mar.devtdd.deliveryappapi.DeliveryAppApiApplicationTests;
import br.mar.devtdd.deliveryappapi.model.Pessoa;
import br.mar.devtdd.deliveryappapi.model.Telefone;
import br.mar.devtdd.deliveryappapi.repository.filtro.PessoaFiltro;
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
	
	@Test
	public void nao_deve_salvar_duas_pessoas_com_o_mesmo_cpf() throws Exception {
		final Pessoa pessoa  = new Pessoa();
		
		pessoa.setNome("Isaac Luiz");
		pessoa.setCpf("72788740417");
		
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
				.log().body()
			.and()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("erro", equalTo("Já existe uma pessoa cadastrada com com CPF '72788740417'"));
		
	}
	
	@Test
	public void deve_filtrar_pessoa_pelo_nome() throws Exception {
		final PessoaFiltro filtro = new PessoaFiltro();
		filtro.setNome("a");
		
		RestAssured.given()
			.request()
			.header("Accept", ContentType.ANY)
			.header("Content-type", ContentType.JSON)
			.body(filtro)
		.when()
		.post("/pessoas/filtrar")
		.then()
			.log().body()
		.and()
			.statusCode(HttpStatus.OK.value())
			.body("codigo",  containsInAnyOrder(1, 3, 5),
					"nome", containsInAnyOrder("Thiago", "Iago", "Cauê"),
					"cpf", containsInAnyOrder("86730543540", "38767897100", "72788740417"));
		
		
		
	}
	
	

}
