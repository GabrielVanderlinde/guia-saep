package com.senai.sistema_almoxarifado.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "produto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45,nullable = false,unique = true)
    private String codigo;

    @Column(length = 45,nullable = false)
    private String nome;

    @Column(length = 45,nullable = false)
    private String caracteristicas;

    @Column(name = "estoque_atual",nullable = false)
    private Integer estoqueAtual;

    @Column(name = "estoque_minimo",nullable = false)
    private Integer estoqueMinimo;

    @CreationTimestamp
    @Column(name = "data_cadastro", updatable = false)
    private LocalDateTime dataCadastro;
}
