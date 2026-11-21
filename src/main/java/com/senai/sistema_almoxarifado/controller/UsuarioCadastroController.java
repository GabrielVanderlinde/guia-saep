package com.senai.sistema_almoxarifado.controller;

import com.senai.sistema_almoxarifado.dto.usuario.UsuarioCadastroDto;
import com.senai.sistema_almoxarifado.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class UsuarioCadastroController {

    private final UsuarioService usuarios;

    @GetMapping("/usuarios/cadastrar")
    public String formulario(@ModelAttribute("usuario") UsuarioCadastroDto usuario) {
        return "usuarios/cadastro";
    }

    @PostMapping("/usuarios/cadastrar")
    public String cadastrar(@Valid @ModelAttribute("usuario") UsuarioCadastroDto usuario,
                            BindingResult erros, RedirectAttributes redirect) {
        if (erros.hasErrors()) {
            return "usuarios/cadastro";
        }
        try {
            usuarios.cadastrar(usuario);
        } catch (IllegalArgumentException e) {
            erros.reject("cadastro.invalido", e.getMessage());
            return "usuarios/cadastro";
        }
        redirect.addFlashAttribute("sucesso", "Usuário cadastrado.");
        return "redirect:/usuarios/cadastrar";
    }
}
