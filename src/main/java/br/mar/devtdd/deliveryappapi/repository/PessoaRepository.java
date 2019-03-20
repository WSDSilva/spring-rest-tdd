package br.mar.devtdd.deliveryappapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.mar.devtdd.deliveryappapi.model.Pessoa;
import br.mar.devtdd.deliveryappapi.repository.helper.PessoaRepositoryQueries;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>, PessoaRepositoryQueries {

	public Optional<Pessoa> findByCpf(String cpf);

	@Query("SELECT bean FROM Pessoa bean JOIN bean.telefones tele WHERE tele.ddd = :ddd AND tele.numero = :numero")
	public Optional<Pessoa> findByTelefoneDddAndNumero(@Param("ddd") String ddd, @Param("numero") String numero); 
		
}