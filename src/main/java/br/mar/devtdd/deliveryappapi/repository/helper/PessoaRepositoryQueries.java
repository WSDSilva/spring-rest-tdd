package br.mar.devtdd.deliveryappapi.repository.helper;

import java.util.List;

import br.mar.devtdd.deliveryappapi.model.Pessoa;
import br.mar.devtdd.deliveryappapi.repository.filtro.PessoaFiltro;

public interface PessoaRepositoryQueries {
	
	List<Pessoa> filtrar(PessoaFiltro filtro);

}
