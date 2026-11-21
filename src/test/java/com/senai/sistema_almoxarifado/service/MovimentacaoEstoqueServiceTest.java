package com.senai.sistema_almoxarifado.service;

import com.senai.sistema_almoxarifado.dto.movimentacao.MovimentacaoEstoqueDto;
import com.senai.sistema_almoxarifado.entity.MovimentacaoEstoqueEntity;
import com.senai.sistema_almoxarifado.entity.ProdutoEntity;
import com.senai.sistema_almoxarifado.entity.TipoMovimentacaoEstoque;
import com.senai.sistema_almoxarifado.entity.UsuarioEntity;
import com.senai.sistema_almoxarifado.exceptions.EstoqueInsuficienteException;
import com.senai.sistema_almoxarifado.repository.MovimentacaoEstoqueRepository;
import com.senai.sistema_almoxarifado.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MovimentacaoEstoqueServiceTest {

    private final ProdutoEntity produto = new ProdutoEntity();
    private final UsuarioEntity operador = new UsuarioEntity();
    private MovimentacaoEstoqueEntity movimento;
    private boolean produtoGravado;
    private final MovimentacaoEstoqueService service;

    MovimentacaoEstoqueServiceTest() {
        produto.setEstoqueAtual(10);
        ProdutoRepository produtos = repositorio(ProdutoRepository.class, (proxy, metodo, args) ->
                switch (metodo.getName()) {
                    case "buscarParaAtualizar" -> Optional.of(produto);
                    case "save" -> {
                        produtoGravado = true;
                        yield args[0];
                    }
                    default -> throw new AssertionError(metodo);
                });
        MovimentacaoEstoqueRepository movimentacoes = repositorio(MovimentacaoEstoqueRepository.class,
                (proxy, metodo, args) -> {
                    assertThat(metodo.getName()).isEqualTo("save");
                    movimento = (MovimentacaoEstoqueEntity) args[0];
                    return movimento;
                });
        service = new MovimentacaoEstoqueService(movimentacoes, produtos);
    }

    private static <T> T repositorio(Class<T> tipo, InvocationHandler comportamento) {
        return tipo.cast(Proxy.newProxyInstance(tipo.getClassLoader(), new Class<?>[]{tipo}, comportamento));
    }

    @ParameterizedTest
    @CsvSource({"4, 6", "10, 0"})
    void registraSaidaComSaldoSuficiente(int quantidade, int saldoFinal) {
        service.registrarSaida(new MovimentacaoEstoqueDto(1L, quantidade), operador);

        assertThat(produto.getEstoqueAtual()).isEqualTo(saldoFinal);
        assertThat(produtoGravado).isTrue();
        assertThat(movimento.getQuantidade()).isEqualTo(quantidade);
        assertThat(movimento.getTipoMovimentacao()).isEqualTo(TipoMovimentacaoEstoque.SAIDA);
        assertThat(movimento.getUsuario()).isSameAs(operador);
        assertThat(movimento.getDataMovimentacao()).isNotNull();
    }

    @Test
    void recusaSaidaAcimaDoSaldoSemGravarMovimentacao() {
        assertThatThrownBy(() -> service.registrarSaida(new MovimentacaoEstoqueDto(1L, 11), operador))
                .isInstanceOf(EstoqueInsuficienteException.class)
                .hasMessage("Saída não permitida: estoque insuficiente. Disponível: 10. Solicitado: 11.");

        assertThat(produto.getEstoqueAtual()).isEqualTo(10);
        assertThat(produtoGravado).isFalse();
        assertThat(movimento).isNull();
    }
}
