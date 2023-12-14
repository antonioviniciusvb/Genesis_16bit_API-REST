package com.generation.lojagames.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	@Column(length=150, nullable = false)
	private String nome;
	
	@Size(min = 10, max = 150, message = "A [descricao] deve conter no mínimo 10 e no máximo 150 caracteres.")
	@Column(length=150, nullable = true)
	private String descricao;
	
	@NotNull(message = "O [preco] não pode ser vazio.")
	@Positive(message = "O [preco] deve ser maior que 0")
	@Digits(integer = 10, fraction = 2)
	private BigDecimal preco;
	
	@NotBlank(message = "A [plataforma] é obrigatória!")
	@Size(min = 10, max = 150, message = "A [plataforma] deve conter no mínimo 10 e no máximo 150 caracteres.")
	@Column(length=150)
	private String plataforma;
	
	@NotBlank(message = "A [produtora] é obrigatória!")
	@Size(min = 10, max = 150, message = "A [produtora] deve conter no mínimo 10 e no máximo 150 caracteres.")
	private String produtora;
	
	@NotBlank(message = "A [data_lancamento] é obrigatória!")
	@PastOrPresent(message = "Não é possivel adicionar datas do futuro para [data_lancamento]")
	@Column(name = "data_lancamento")
	private LocalDate dataLancamento;
	
	@Size(min = 10, max = 500, message = "A [foto] deve conter no mínimo 10 e no máximo 500 caracteres.")
	@Column(length=500, nullable = true)
	private String foto;
	
	@Min(value = 1, message = "A [quantidade] deve ser maior que 0")
	@NotNull(message = "A [quantidade] é obrigatória!")
	private Integer quantidade;
	
}
