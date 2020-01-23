package com.rbbsolucoes.controleseubolso.model.repository;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.rbbsolucoes.controleseubolso.model.entity.Usuario;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository Repository;
	
	@Autowired
	TestEntityManager EntityManager;

	//criando estatico para testes
	public static Usuario criarUsuario() {
		return Usuario
				.builder()
				.nome("usuario")
				.email("usuario@email.com")
				.senha("senha")
				.build();
	}
	
	@Test
	public void deveVerificarExistenciaEmail() {
		
		//cenario
		Usuario usuario = criarUsuario();
		EntityManager.persist(usuario);
		
		//acao/execucao
		boolean result = Repository.existsByEmail("usuario@email.com");
		
		//verificacao
		Assertions.assertThat(result).isTrue();
		
	}
	
	@Test
	public void deveRetornarFalseQuandoNaoHouverUsuarioCadastradoComEmail() {
		
		//cenario

		//acao/execucao
		boolean result = Repository.existsByEmail("usuario@email.com");
				
		//verificacao
		Assertions.assertThat(result).isFalse();
		
	}
	
	@Test
	public void devePersistirUmUsuarioNaBaseDeDado() {
		
		//cenario
		Usuario usuario =  criarUsuario();
		
		//acao
		Usuario usuarioSalvo = Repository.save(usuario);
		
		//verificacao
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();	

	}
	
	@Test
	public void deveBuscarUmUsuarioPorEmail() {

		//cenario
		Usuario usuario =  criarUsuario();
		EntityManager.persist(usuario);
		
		//verificacao
		Optional<Usuario> result = Repository.findByEmail("usuario@email.com");
		
		Assertions.assertThat(result.isPresent()).isTrue();
		
		
	}
	
	@Test
	public void deveRetornarVazioAoBuscarUsuarioPorEmailQuandoNaoExisteNaBase() {

		//cenario
		
		//verificacao
		Optional<Usuario> result = Repository.findByEmail("usuario@email.com");
		
		Assertions.assertThat(result.isPresent()).isFalse();
		
		
	}
}
