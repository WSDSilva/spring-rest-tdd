package br.mar.devtdd.deliveryappapi.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.mar.devtdd.deliveryappapi.model.Pessoa;
import br.mar.devtdd.deliveryappapi.model.Telefone;
import br.mar.devtdd.deliveryappapi.repository.PessoaRepository;
import br.mar.devtdd.deliveryappapi.service.PessoaService;
import br.mar.devtdd.deliveryappapi.service.exception.TelefoneNaoEncontradoException;
import br.mar.devtdd.deliveryappapi.service.exception.UnicidadeCpfException;
import br.mar.devtdd.deliveryappapi.service.exception.UnicidadeTelefoneException;

@Service
public class PessoaServiceImpl implements PessoaService {



	private PessoaRepository pessoaRepository;

	public PessoaServiceImpl(PessoaRepository pessoaRepository) {
		this.pessoaRepository = pessoaRepository;
	}

	@Override
	public Pessoa salvar(Pessoa pessoa) throws UnicidadeCpfException, UnicidadeTelefoneException {
		Optional<Pessoa> optional = pessoaRepository.findByCpf(pessoa.getCpf());
		
		String ddd = pessoa.getTelefones().get(0).getDdd();
		String numero = pessoa.getTelefones().get(0).getNumero();
		
		if(optional.isPresent()) {
			throw new UnicidadeCpfException();
		} 		
		
		optional = pessoaRepository.findByTelefoneDddAndNumero(ddd, numero);
		
		if(optional.isPresent()) {
			throw new UnicidadeTelefoneException();
		}
		
		
		return pessoaRepository.save(pessoa);
		
	}
	
	@Override
	public Pessoa buscarPorTelefone(Telefone telefone) throws TelefoneNaoEncontradoException {
		
		String msgErro = "Não existe pessoa com o telefone ("+telefone.getDdd()+")"+ telefone.getNumero();
		final Optional<Pessoa> optional = pessoaRepository.findByTelefoneDddAndNumero(telefone.getDdd(), telefone.getNumero());
		return optional.orElseThrow(() -> new TelefoneNaoEncontradoException(msgErro));
	}

}
