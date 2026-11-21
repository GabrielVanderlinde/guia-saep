package com.senai.sistema_almoxarifado.dto.usuario;

import com.senai.sistema_almoxarifado.entity.Papel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioCadastroDto(
        @NotBlank @Size(max = 50) String nome,
        @NotBlank @Size(max = 45) String login,
        @NotBlank @Size(min = 8, max = 72) String senha,
        @NotNull Papel papel) {
}
