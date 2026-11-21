package com.senai.sistema_almoxarifado.exceptions;

public class EstoqueInsuficienteException extends IllegalArgumentException {
    public EstoqueInsuficienteException(int disponivel, int solicitado) {
        super("Saída não permitida: estoque insuficiente. Disponível: %d. Solicitado: %d."
                .formatted(disponivel, solicitado));
    }
}
