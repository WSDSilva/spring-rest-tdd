package br.mar.devtdd.deliveryappapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.expression.spel.ast.OpInc;
import org.springframework.test.context.junit4.SpringRunner;

import br.mar.devtdd.deliveryappapi.model.Pessoa;
import br.mar.devtdd.deliveryappapi.model.Telefone;
import br.mar.devtdd.deliveryappapi.repository.PessoaRepository;
import br.mar.devtdd.deliveryappapi.service.exception.TelefoneNaoEncontradoException;
import br.mar.devtdd.deliveryappapi.service.exception.UnicidadeCpfException;
import br.mar.devtdd.deliveryappapi.service.exception.UnicidadeTelefoneException;
import br.mar.devtdd.deliveryappapi.service.impl.PessoaServiceImpl;

@RunWith(SpringRunner.class)
public class PessoaServiceTest {
	
	private static final String NOME = "Wanderson Duarte";
	private static final String CPF = "12345678901";
	private static final String TELEFONE = "987654321";
	private static final String DDD = "21";
	
	
	private PessoaService sut;
	
	@MockBean
	private PessoaRepository pessoaRepository;
	
	private Pessoa pessoa;
	Telefone telefone;
	
	@Rule
	public ExpectedException expectedExcepiton = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {
		sut = new PessoaServiceImpl(pessoaRepository);
		telefone = new Telefone()		;
		telefone.setDdd(DDD);
		telefone.setNumero(TELEFONE);
				
		List<Telefone> telefones = new ArrayList();
		telefones.add(telefone);
		
		pessoa = new Pessoa();
		pessoa.setNome(NOME);
		pessoa.setCpf(CPF);
		pessoa.setTelefones(telefones);
		
		when(pessoaRepository.findByCpf(CPF)).thenReturn(Optional.empty());
		when(pessoaRepository.findByTelefoneDddAndNumero(DDD, TELEFONE)).thenReturn(Optional.empty());
		
	}
	
	@Test
	public void deve_salvar_pessoa_no_repository() throws Exception{
		
		sut.salvar(pessoa);
		
		verify(pessoaRepository).save(pessoa);
	}
	
	@Test(expected = UnicidadeCpfException.class)
	public void nao_deve_salvar_duas_pessoas_com_mesmo_cpf() throws Exception{
		when(pessoaRepository.findByCpf(CPF)).thenReturn(Optional.of(pessoa));
		
		expectedExcepiton.expect(UnicidadeCpfException.class);
		expectedExcepiton.expectMessage("Já existe uma pessoa cadastrada com com CPF '"+CPF+"'");
		
		sut.salvar(pessoa);
	}
	
	@Test(expected = UnicidadeTelefoneException.class)
	public void nao_deve_salvar_duas_pessoas_com_mesmo_telefone() throws Exception{
		when(pessoaRepository.findByTelefoneDddAndNumero(DDD,TELEFONE)).thenReturn(Optional.of(pessoa));
		
		
		
		sut.salvar(pessoa);
	}
	
	@Test
	public void deve_procurar_pessoa_pelo_ddd_e_numero_do_telefone() throws Exception{
		when(pessoaRepository.findByTelefoneDddAndNumero(DDD, TELEFONE)).thenReturn(Optional.of(pessoa));
		
		Pessoa pessoaTeste = sut.buscarPorTelefone(telefone);
		
		verify(pessoaRepository).findByTelefoneDddAndNumero(DDD, TELEFONE);
		
		assertThat(pessoaTeste).isNotNull();
		assertThat(pessoaTeste.getNome()).isEqualTo(NOME);
		assertThat(pessoaTeste.getCpf()).isEqualTo(CPF);
				
	}
	
	@Test(expected = TelefoneNaoEncontradoException.class)
	public void deve_retornar_uma_excecao_quando_nao_encontrar_telefone() throws Exception {
		
		sut.buscarPorTelefone(telefone);
		
	}
	
	@Test public void 
	deve_conter_ddd_e_numero_na_mensagem_de_erro() throws Exception{
		expectedExcepiton.expect(TelefoneNaoEncontradoException.class);
		expectedExcepiton.expectMessage("Não existe pessoa com o telefone ("+DDD+")"+TELEFONE);
		sut.buscarPorTelefone(telefone);
	}

}
