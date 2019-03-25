package br.mar.devtdd.deliveryappapi.resource;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.mar.devtdd.deliveryappapi.model.Pessoa;
import br.mar.devtdd.deliveryappapi.model.Telefone;
import br.mar.devtdd.deliveryappapi.service.PessoaService;
import br.mar.devtdd.deliveryappapi.service.exception.TelefoneNaoEncontradoException;
import br.mar.devtdd.deliveryappapi.service.exception.UnicidadeCpfException;
import br.mar.devtdd.deliveryappapi.service.exception.UnicidadeTelefoneException;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {
	
	@Autowired
	PessoaService pessoaservice;
	
	@GetMapping("/{ddd}/{numero}")
	public ResponseEntity<Pessoa> buscarPorDddENumero(@PathVariable String ddd, 
												 	  @PathVariable String numero) throws TelefoneNaoEncontradoException{
		final Telefone telefone = new Telefone();
		telefone.setDdd(ddd);
		telefone.setNumero(numero);
				
		Pessoa pessoa = pessoaservice.buscarPorTelefone(telefone);
				
		return new ResponseEntity<Pessoa>(pessoa, HttpStatus.OK);
	}
	
	@ExceptionHandler({TelefoneNaoEncontradoException.class})
	public ResponseEntity<Erro> handleTelefoneNaoEncontradoException(TelefoneNaoEncontradoException ex){
		return new ResponseEntity<Erro>(new Erro(ex.getMessage()), HttpStatus.NOT_FOUND);
	}
	
	
	@PostMapping()
	public ResponseEntity<Pessoa> salvarPessoa(@RequestBody Pessoa pessoa, HttpServletResponse response) throws UnicidadeCpfException,
																												UnicidadeTelefoneException{
		final Pessoa pessoaSalva =  pessoaservice.salvar(pessoa);
		
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequestUri().path("/{ddd}/{numero}")
				.buildAndExpand(pessoa.getTelefones().get(0).getDdd(), pessoa.getTelefones().get(0).getNumero()).toUri();	
		
		response.setHeader("Location", uri.toASCIIString());
		
		return new ResponseEntity<>(pessoaSalva, HttpStatus.CREATED);
	}
	
	@ExceptionHandler({ UnicidadeCpfException.class })
	public ResponseEntity<Erro> handleUnicidadeCpfException(UnicidadeCpfException ex){
		return new ResponseEntity(new Erro(ex.getMessage()), HttpStatus.BAD_REQUEST);
	}
	
	
	class Erro{
		String erro;

		public Erro(String erro) {
			this.erro = erro;
		}
		
		public String getErro() {
			return erro;
		}
		
	}

}
