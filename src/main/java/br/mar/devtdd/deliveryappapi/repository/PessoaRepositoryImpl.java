package br.mar.devtdd.deliveryappapi.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import br.mar.devtdd.deliveryappapi.model.Pessoa;
import br.mar.devtdd.deliveryappapi.repository.filtro.PessoaFiltro;
import br.mar.devtdd.deliveryappapi.repository.helper.PessoaRepositoryQueries;

@Component
public class PessoaRepositoryImpl implements PessoaRepositoryQueries {

	
	@PersistenceContext
	EntityManager manager;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoa> filtrar(PessoaFiltro filtro) {

		final StringBuilder sb = new StringBuilder();
		final Map<String, Object> params = new HashMap<>();
		
		sb.append(" SELECT bean FROM Pessoa bean JOIN bean.telefones tele WHERE 1=1 ");
		
		preencherNomeSeTiver(filtro, sb, params);
		
		preencherCpfSeTiver(filtro, sb, params);
		
		preencherDddSeTiver(filtro, sb, params);
		
		preencherNumeroSeTiver(filtro, sb, params);
		
		
		Query query = manager.createQuery(sb.toString(), Pessoa.class);
		
		preencherParamsQuery(params, query);
		
		
		return query.getResultList();
	}

	private void preencherNumeroSeTiver(PessoaFiltro filtro, final StringBuilder sb, final Map<String, Object> params) {
		if(StringUtils.hasText(filtro.getTelefone())) {
			sb.append(" AND tele.numero = :numero ");
			params.put("numero", filtro.getTelefone());
		}
	}

	private void preencherDddSeTiver(PessoaFiltro filtro, final StringBuilder sb, final Map<String, Object> params) {
		if(StringUtils.hasText(filtro.getDdd())) {
			sb.append(" AND tele.ddd = :ddd ");
			params.put("ddd", filtro.getDdd());
		}
	}

	private void preencherCpfSeTiver(PessoaFiltro filtro, final StringBuilder sb, final Map<String, Object> params) {
		if(StringUtils.hasText(filtro.getCpf())) {
			sb.append(" AND bean.cpf LIKE :cpf ");
			params.put("cpf", "%"+ filtro.getCpf() +"%");
		}
	}

	private void preencherNomeSeTiver(PessoaFiltro filtro, final StringBuilder sb, final Map<String, Object> params) {
		if(StringUtils.hasText(filtro.getNome())) {
			sb.append(" AND bean.nome LIKE :nome ");
			params.put("nome", "%"+ filtro.getNome()+"%");
		}
	}

	private void preencherParamsQuery(final Map<String, Object> params, Query query) {
		for(Map.Entry<String, Object> param : params.entrySet()) {
			query.setParameter(param.getKey(), param.getValue());
		}
	}

	
}
