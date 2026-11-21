package com.senai.sistema_almoxarifado.service;

import com.senai.sistema_almoxarifado.dto.produto.ProdutoAtualizarDto;
import com.senai.sistema_almoxarifado.dto.produto.ProdutoDto;
import com.senai.sistema_almoxarifado.dto.produto.ProdutoRespostaDto;
import com.senai.sistema_almoxarifado.entity.ProdutoEntity;
import com.senai.sistema_almoxarifado.exceptions.ProdutoCadastradoException;
import com.senai.sistema_almoxarifado.exceptions.ProdutoComMovimentacaoException;
import com.senai.sistema_almoxarifado.exceptions.ProdutoNaoEncontradoException;
import com.senai.sistema_almoxarifado.repository.MovimentacaoEstoqueRepository;
import com.senai.sistema_almoxarifado.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository repository;
    private final MovimentacaoEstoqueRepository movimentacaoRepository;

    public Page<ProdutoRespostaDto> listarProdutosPaginados(String nome, LocalDate inicio, LocalDate fim,
                                                          int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(Math.max(0, pagina), Math.clamp(tamanho, 1, 100),
                Sort.by("id"));
        String filtroNome = nome == null || nome.isBlank() ? null : nome.strip();
        LocalDateTime inicioInclusivo = inicio == null ? null : inicio.atStartOfDay();
        LocalDateTime fimExclusivo = fim == null ? null : fim.plusDays(1).atStartOfDay();
        return repository.buscar(filtroNome, inicioInclusivo, fimExclusivo, pageable)
                .map(ProdutoRespostaDto::toProdutoRespostaDto);
    }

    public List<ProdutoRespostaDto> listarProdutos(){
        return repository.findAll()
                .stream()
                .map(ProdutoRespostaDto::toProdutoRespostaDto)
                .toList();
    }

    public List<ProdutoRespostaDto> buscarPersonalizada(String termo){
        List<ProdutoEntity> produtos = repository.
                findByCodigoContainingIgnoreCaseOrNomeContainingIgnoreCase(termo, termo);

        return produtos.stream()
                .map(ProdutoRespostaDto::toProdutoRespostaDto)
                .toList();
    }

    public void cadastrarProduto(ProdutoDto produtoDto){
        if (repository.existsByCodigo(produtoDto.codigo())){
            throw new ProdutoCadastradoException("Produto já cadastrado");
        }

        repository.save(produtoDto.toProduto());
    }

    public ProdutoEntity buscarProdutoPorId(Long id){
        return repository.findById(id)
                .orElseThrow(() ->
                        new ProdutoNaoEncontradoException("Produto não encontrado"));
    }

    @Transactional
    public void atualizarProduto(Long id, ProdutoAtualizarDto produtoAtualizarDto){
        ProdutoEntity produto = repository.buscarParaAtualizar(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado"));

        produto.setNome(produtoAtualizarDto.nome());
        produto.setCaracteristicas(produtoAtualizarDto.caracteristicas());
        produto.setEstoqueMinimo(produtoAtualizarDto.estoqueMinimo());

        repository.save(produto);
    }

    @Transactional
    public void excluirProduto(Long id){
        repository.buscarParaAtualizar(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado"));

        if (movimentacaoRepository.existsByProdutoId(id)) {
            throw new ProdutoComMovimentacaoException("Não é possível excluir o produto pois ele possui movimentações registradas");
        }
        repository.deleteById(id);
    }
}