package com.rbbsolucoes.controleseubolso.service;

import java.util.Optional;

import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.BeforeTransaction;

import com.rbbsolucoes.controleseubolso.exception.ErroAutenticacao;
import com.rbbsolucoes.controleseubolso.exception.RegraNegocioException;
import com.rbbsolucoes.controleseubolso.model.entity.Usuario;
import com.rbbsolucoes.controleseubolso.model.repository.UsuarioRepository;
import com.rbbsolucoes.controleseubolso.service.impl.UsuarioServiceImpl;

import junit.framework.Assert;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	@SpyBean
	UsuarioServiceImpl service;
	
	@MockBean
	UsuarioRepository Repository;

	@Test(expected = Test.None.class)
	public void deveSalvarUmUsuario() {
		//cenario
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder().id(1l).nome("nome").email("email@email.com").senha("senha").build();
		Mockito.when(Repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		//acao
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
				
		//verificacao
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");

	}
	
	@Test(expected = RegraNegocioException.class)
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {
		//cenario
		String email = "email@email.com";
		Usuario usuario = Usuario.builder().email(email).build();
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
		
		//acao
		service.salvarUsuario(usuario);
		
		//verificacao
		Mockito.verify( Repository, Mockito.never() ).save(usuario);
		
	}
	
	@Test(expected = Test.None.class)
	public void deveAutenticarUmUsuarioComSucesso() {
		//cenario
		String email = "email@email.com";
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when(Repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		//acao
		Usuario result = service.autenticar(email, senha);
		
		//verificar
		Assertions.assertThat(result).isNotNull();
	}
	
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastroComEmailInformado() {

		//cenario
		Mockito.when(Repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		//acao
		Throwable exception = Assertions.catchThrowable( () -> service.autenticar("email@email.com", "senha") );
		
		//verificacao
		Assertions.assertThat(exception)
			.isInstanceOf(ErroAutenticacao.class)
			.hasMessage("Usuário não encontrado para o email informado.");
		
	}
	
	@Test
	public void deveLancarErroQuandoSenhaNaoBater() {
		
		//cenario
		String senha = "senha";
		Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
		Mockito.when(Repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		//acao
		Throwable exception = Assertions.catchThrowable( () -> service.autenticar("email@email.com", "123"));
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha inválida");
		
		
	}
	@Test(expected = Test.None.class)
	public void deveValidarEmail() {
		//cenario
		Mockito.when(Repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		//acao
		service.validarEmail("email@email.com");
		
	}
	
	@Test(expected = RegraNegocioException.class)
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		//cenario
		Mockito.when(Repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		//acao
		service.validarEmail("email@email.com");

	}

}
