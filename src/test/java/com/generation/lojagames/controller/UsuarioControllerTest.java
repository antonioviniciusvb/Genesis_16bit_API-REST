package com.generation.lojagames.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.lojagames.model.Usuario;
import com.generation.lojagames.model.UsuarioLogin;
import com.generation.lojagames.repository.UsuarioRepository;
import com.generation.lojagames.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioControllerTest {

	private Usuario usuarioTeste = new Usuario(0L, "Antonio Bandeira", "antonio_bandeira@genesis16bit.com.br",
			"123456789", "", LocalDate.of(2001, 12, 28));

	private Usuario usuarioRoot = new Usuario(0L, "Root", "root@genesis16bit.com", "rootroot", "",
			LocalDate.of(1990, 7, 20));

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@BeforeAll
	void start() {

		usuarioRepository.deleteAll();

		usuarioService.cadastrarUsuario(
				new Usuario(0L, "Root", "root@genesis16bit.com", "rootroot", "", LocalDate.of(1990, 7, 20)));
	}

	@BeforeEach
	void reset() {

		usuarioTeste.setId(0L);
		usuarioTeste.setNome("Antonio Bandeira");
		usuarioTeste.setUsuario("antonio_bandeira@genesis16bit.com.br");
		usuarioTeste.setSenha("123456789");
		usuarioTeste.setFoto("");
		usuarioTeste.setDataNascimento(LocalDate.of(2001, 12, 28));

		var usuario = usuarioRepository.findByUsuario(usuarioTeste.getUsuario());

		if (usuario.isPresent())
			usuarioRepository.deleteById(usuario.get().getId());
	}

	@Test
	@DisplayName("Cadastar um Usuário")
	public void deveCriarUmUsuario() {

		usuarioTeste.setUsuario("antonio_bandeira@genesis16bit.com.br");
		usuarioTeste.setSenha("4ntonio3");
		usuarioTeste.setDataNascimento(LocalDate.of(1990, 7, 20));

		assertEquals(HttpStatus.CREATED,
				executaRequisicao(usuarioTeste, "/usuarios/cadastrar", HttpMethod.POST).getStatusCode());
	}

	@Test
	@DisplayName("Autenticar usuário")
	public void deveAutenticarUsuario() {

		String uri = "/usuarios/logar";

		assertEquals(HttpStatus.OK, executaRequisicao(usuarioRoot, uri, HttpMethod.POST).getStatusCode());
	}

	@Test
	@DisplayName("Não deve Autenticar usuário")
	public void naoDeveAutenticarUsuario() {

		String uri = "/usuarios/logar";

		assertEquals(HttpStatus.FORBIDDEN, executaRequisicao(usuarioTeste, uri, HttpMethod.POST).getStatusCode());
	}

	@Test
	@DisplayName("Não deve cadastrar usuário com senha em branco")
	public void naoDeveCadastrarUsuarioComSenhaEmBranco() {

		String uri = "/usuarios/cadastrar";

		usuarioTeste.setSenha("");

		var requisicao = executaRequisicao(usuarioTeste, uri, HttpMethod.POST);

		assertEquals(HttpStatus.BAD_REQUEST, requisicao.getStatusCode());

		requisicao.getBody().setSenha("  ");

		requisicao = executaRequisicao(usuarioTeste, uri, HttpMethod.POST);

		assertEquals(HttpStatus.BAD_REQUEST, requisicao.getStatusCode());

	}

	@Test
	@DisplayName("Não deve cadastrar usuário com nome em branco")
	public void naoDeveCadastrarUsuarioComNomeEmBranco() {

		String uri = "/usuarios/cadastrar";

		usuarioTeste.setNome("");

		var requisicao = executaRequisicao(usuarioTeste, uri, HttpMethod.POST);

		assertEquals(HttpStatus.BAD_REQUEST, requisicao.getStatusCode());

		requisicao.getBody().setNome("  ");

		requisicao = executaRequisicao(usuarioTeste, uri, HttpMethod.POST);

		assertEquals(HttpStatus.BAD_REQUEST, requisicao.getStatusCode());

	}

	@Test
	@DisplayName("Não deve cadastrar usuário com senha menor que 8 caracteres")
	public void naoDeveCadastrarUsuarioComSenhaPequena() {

		String uri = "/usuarios/cadastrar";

		usuarioTeste.setSenha("123456");

		assertEquals(HttpStatus.BAD_REQUEST, executaRequisicao(usuarioTeste, uri, HttpMethod.POST).getStatusCode());

	}

	@Test
	@DisplayName("Não deve cadastrar usuário já existente")
	public void naoDeveCadastrarUsuarioExistente() {

		String uri = "/usuarios/cadastrar";

		usuarioService.cadastrarUsuario(usuarioTeste);

		assertEquals(HttpStatus.BAD_REQUEST, executaRequisicao(usuarioTeste, uri, HttpMethod.POST).getStatusCode());
	}

	@Test
	@DisplayName("Atualizar usuário")
	public void deveAtualizarUsuario() {

		String uri = "/usuarios/atualizar";

		usuarioTeste.setUsuario("usuarioteste@email.com");

		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(usuarioTeste);

		usuarioTeste.setId(usuarioCadastrado.get().getId());
		usuarioTeste.setNome("Produção - Atualizado");
		usuarioTeste.setUsuario("administracao@genesis16bit.com");
		usuarioTeste.setDataNascimento(LocalDate.of(1990, 7, 20));

		assertEquals(HttpStatus.OK,
				executaRequisicao(Optional.of(usuarioTeste), uri, HttpMethod.PUT, usuarioRoot).getStatusCode());

	}

	@Test
	@DisplayName("Listar todos os usuários cadastrados")
	public void deveListarTodosUsuarios() {

		usuarioService.cadastrarUsuario(new Usuario(0L, "Administrativo", "rebeca@genesis16bit.com", "administrac4o",
				"", LocalDate.of(1990, 7, 20)));

		usuarioService.cadastrarUsuario(new Usuario(0L, "Administrativo", "luis@genesis16bit.com", "administrac4o", "",
				LocalDate.of(1990, 7, 20)));

		assertEquals(HttpStatus.OK,
				executaRequisicao(Optional.empty(), "/usuarios", HttpMethod.GET, usuarioRoot).getStatusCode());

	}

	@Test
	@DisplayName("Não deve cadastrar usuários menores de idade")
	public void naoDeveCadastrarUsuarioMenorIdade() {

		String uri = "/usuarios/cadastrar";

		usuarioTeste.setDataNascimento(LocalDate.now());

		assertEquals(HttpStatus.BAD_REQUEST, executaRequisicao(usuarioTeste, uri, HttpMethod.POST).getStatusCode());

		usuarioTeste.setDataNascimento(LocalDate.of(2050, 5, 14));

		assertEquals(HttpStatus.BAD_REQUEST, executaRequisicao(usuarioTeste, uri, HttpMethod.POST).getStatusCode());

		usuarioTeste.setDataNascimento(LocalDate.of(2020, 4, 5));

		assertEquals(HttpStatus.BAD_REQUEST, executaRequisicao(usuarioTeste, uri, HttpMethod.POST).getStatusCode());

		usuarioTeste.setDataNascimento(LocalDate.of(1970, 11, 30));

		assertEquals(HttpStatus.CREATED, executaRequisicao(usuarioTeste, uri, HttpMethod.POST).getStatusCode());

	}

	private ResponseEntity<Usuario> executaRequisicao(Usuario corpo, String uri, HttpMethod metodo) {

		return testRestTemplate.exchange(uri, metodo, new HttpEntity<Usuario>(corpo), Usuario.class);
	}

	private ResponseEntity<String> executaRequisicao(Optional<Usuario> corpo, String uri, HttpMethod metodo,
			Usuario usuario) {

		var corpoRequisicao = corpo.isPresent() ? new HttpEntity<Usuario>(corpo.get()) : null;

		return testRestTemplate.withBasicAuth(usuario.getUsuario(), usuario.getSenha()).exchange(uri, metodo,
				corpoRequisicao, String.class);
	}

//	private ResponseEntity<String> executaRequisicao(Optional<Usuario> corpo, String uri, HttpMethod metodo,
//			String userName, String passWord) {
//
//		var corpoRequisicao = corpo.isPresent() ? new HttpEntity<Usuario>(corpo.get()) : null;
//
//		return testRestTemplate.withBasicAuth(userName, passWord).exchange(uri, metodo, corpoRequisicao, String.class);
//	}

}
