package com.generation.lojagames.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_produto")
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O atributo [nome] é obrigatório!")
	@Size(min = 10, max = 150, message = "O [nome] deve conter no mínimo 10 e no máximo 150 caracteres.")
	@Column(length = 150, nullable = false)
	private String nome;

	@Size(min = 10, max = 1000, message = "A [descricao] deve conter no mínimo 10 e no máximo 1000 caracteres.")
	@Column(length = 1000, nullable = true)
	private String descricao;

	@NotNull(message = "O [preco] não pode ser vazio.")
	@Positive(message = "O [preco] deve ser maior que 0")
	@Digits(integer = 10, fraction = 2)
	private BigDecimal preco;

	@NotBlank(message = "A [plataforma] é obrigatória!")
	@Size(min = 2, max = 150, message = "A [plataforma] deve conter no mínimo 2 e no máximo 150 caracteres.")
	@Column(length = 150)
	private String plataforma;

	@NotBlank(message = "A [produtora] é obrigatória!")
	@Size(min = 2, max = 150, message = "A [produtora] deve conter no mínimo 10 e no máximo 150 caracteres.")
	private String produtora;

	@PastOrPresent(message = "Não é possivel adicionar datas do futuro para [data_lancamento]")
	@NotNull(message = "A [data_lancamento] é obrigatória!")
	@Column(name = "data_lancamento")
	private LocalDate dataLancamento;

	@Size(min = 10, max = 500, message = "A [foto] deve conter no mínimo 10 e no máximo 500 caracteres.")
	@Column(length = 500, nullable = true)
	private String foto;

	@Min(value = 1, message = "A [quantidade] deve ser maior que 0")
	@NotNull(message = "A [quantidade] é obrigatória!")
	private Integer quantidade;

	@ManyToOne
	@JsonIgnoreProperties("produto")
	private Categoria categoria;

	@ManyToOne
	@JsonIgnoreProperties("produto")
	private Usuario usuario;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public String getPlataforma() {
		return plataforma;
	}

	public void setPlataforma(String plataforma) {
		this.plataforma = plataforma;
	}

	public String getProdutora() {
		return produtora;
	}

	public void setProdutora(String produtora) {
		this.produtora = produtora;
	}

	public LocalDate getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(LocalDate dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
