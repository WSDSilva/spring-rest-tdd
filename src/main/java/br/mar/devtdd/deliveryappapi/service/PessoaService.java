package br.mar.devtdd.deliveryappapi.service;

import br.mar.devtdd.deliveryappapi.model.Pessoa;
import br.mar.devtdd.deliveryappapi.model.Telefone;
import br.mar.devtdd.deliveryappapi.service.exception.TelefoneNaoEncontradoException;
import br.mar.devtdd.deliveryappapi.service.exception.UnicidadeCpfException;
import br.mar.devtdd.deliveryappapi.service.exception.UnicidadeTelefoneException;


public interface PessoaService {

	Pessoa salvar(Pessoa pessoa) throws UnicidadeCpfException, UnicidadeTelefoneException;

	Pessoa buscarPorTelefone(Telefone telefone) throws TelefoneNaoEncontradoException;

}
