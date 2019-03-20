package br.mar.devtdd.deliveryappapi.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import br.mar.devtdd.deliveryappapi.model.Pessoa;
import br.mar.devtdd.deliveryappapi.repository.filtro.PessoaFiltro;

@Sql(value="/load-database.sql", executionPhase=Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value="/clean-database.sql", executionPhase=Sql.ExecutionPhase.AFTER_TEST_METHOD)
@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class PessoaRepositoryTest {
	
	@Autowired
	PessoaRepository pessoaRepository;

	@Test
	public void deve_procurar_pessoa_pelo_cpf() throws Exception{
		Optional<Pessoa> optional = pessoaRepository.findByCpf("38767897100");
		
		assertThat(optional.isPresent()).isTrue();
		
		Pessoa pessoa = optional.get();
		
		assertThat(pessoa.getNome()).isEqualTo("Cauê");
		assertThat(pessoa.getCodigo()).isEqualTo(3L);
	}
	
	@Test
	public void nao_deve_encontrar_pessoa_de_cpf_inexistente() throws Exception {
		Optional<Pessoa> optional = pessoaRepository.findByCpf("38767897199");
		
		assertThat(optional.isPresent()).isFalse();		
		
	}
	
	@Test
	public void deve_retornar_pessoa_pelo_ddd_e_numero_de_telefone() throws Exception {
		Optional<Pessoa> optional = pessoaRepository.findByTelefoneDddAndNumero("86", "35006330");
		
		assertThat(optional.isPresent()).isTrue();
		
		Pessoa pessoa = optional.get();
		
		assertThat(pessoa.getNome()).isEqualTo("Cauê");
		assertThat(pessoa.getCpf()).isEqualTo("38767897100");
	}
	
	@Test
	public void nao_deve_encontrar_pessoa_onde_ddd_e_telefone_nao_existam() throws Exception{
		Optional<Pessoa> optional = pessoaRepository.findByTelefoneDddAndNumero("22", "971845203");
		
		assertThat(optional.isEmpty()).isTrue();
	}
	
	@Test
	public void deve_retornar_pessoa_por_parte_do_nome() throws Exception{
		PessoaFiltro filtro = new PessoaFiltro();
		filtro.setNome("a");
		
		List<Pessoa> pessoas = pessoaRepository.filtrar(filtro);
		
		assertThat(pessoas.size()).isEqualTo(3);
	}
	
	@Test
	public void deve_retornar_pessoa_por_parte_do_cpf() throws Exception{
		PessoaFiltro filtro = new PessoaFiltro();
		filtro.setCpf("78");
		
		List<Pessoa> pessoas = pessoaRepository.filtrar(filtro);
		
		assertThat(pessoas.size()).isEqualTo(3);
	}
	
	@Test
	public void deve_retornar_pessoa_usando_filtro_composto() throws Exception{
		PessoaFiltro filtro = new PessoaFiltro();
		filtro.setCpf("78");
		filtro.setNome("a");
		
		List<Pessoa> pessoas = pessoaRepository.filtrar(filtro);
		
		assertThat(pessoas.size()).isEqualTo(2);
	}
	
	@Test
	public void deve_retornar_pessoa_filtrando_por_ddd() throws Exception{
		PessoaFiltro filtro = new PessoaFiltro();
		filtro.setDdd("21");
		
		List<Pessoa> pessoas = pessoaRepository.filtrar(filtro);
		
		assertThat(pessoas.size()).isEqualTo(1);
	}
	
	@Test
	public void deve_retornar_pessoa_pelo_numero_do_telefone()throws Exception{
		PessoaFiltro filtro = new PessoaFiltro();
		filtro.setTelefone("997538804");
		
		List<Pessoa> pessoas = pessoaRepository.filtrar(filtro);
		
		assertThat(pessoas.size()).isEqualTo(1);
	}
	
	
}
