package com.senai.sistema_almoxarifado.controller.produto;

import com.senai.sistema_almoxarifado.dto.produto.ProdutoAtualizarDto;
import com.senai.sistema_almoxarifado.dto.produto.ProdutoDto;
import com.senai.sistema_almoxarifado.entity.ProdutoEntity;
import com.senai.sistema_almoxarifado.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ProdutoPageController {

    private final ProdutoService service;

    @GetMapping("/produtolista")
    public String getProdutoLista(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho,
            @RequestParam(value = "termo", required = false) String termo,
            @RequestParam(value = "dataInicio", required = false) String dataInicio,
            @RequestParam(value = "dataFim", required = false) String dataFim,
            Model model) {

        try {
            LocalDate inicio = lerData(dataInicio);
            LocalDate fim = lerData(dataFim);
            if (inicio != null && fim != null && inicio.isAfter(fim)) {
                throw new IllegalArgumentException("A data inicial deve ser anterior ou igual à final.");
            }
            model.addAttribute("pagina", service.listarProdutosPaginados(termo, inicio, fim, pagina, tamanho));
        } catch (DateTimeParseException e) {
            model.addAttribute("erroFiltro", "Informe datas válidas.");
        } catch (IllegalArgumentException e) {
            model.addAttribute("erroFiltro", e.getMessage());
        }
        model.addAttribute("termo", termo);
        model.addAttribute("dataInicio", dataInicio);
        model.addAttribute("dataFim", dataFim);
        return "produtos/produtolista";
    }

    private LocalDate lerData(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        LocalDate data = LocalDate.parse(valor);
        if (data.getYear() < 1000 || data.getYear() > 9998) {
            throw new IllegalArgumentException("Informe um ano entre 1000 e 9998.");
        }
        return data;
    }

    @GetMapping("/produtocadastrar")
    public String getCadastrarProduto(Model model) {
        model.addAttribute("produtoDto", new ProdutoDto(null, null, null, null, null));
        return "produtos/produtocadastrar";
    }

    @GetMapping("/produtoatualizar/{id}")
    public String getProdutoAtualizar(@PathVariable("id") Long id, Model model) {
        ProdutoEntity produto = service.buscarProdutoPorId(id);

        ProdutoAtualizarDto produtoAtualizarDto = new ProdutoAtualizarDto(
                produto.getId(),
                produto.getCodigo(),
                produto.getNome(),
                produto.getCaracteristicas(),
                produto.getEstoqueAtual(),
                produto.getEstoqueMinimo()
        );

        model.addAttribute("produtoAtualizacao", produtoAtualizarDto);
        return "produtos/produtoatualizar";
    }
}