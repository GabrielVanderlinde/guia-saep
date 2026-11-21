package com.senai.sistema_almoxarifado.service;

import com.senai.sistema_almoxarifado.entity.UsuarioEntity;
import com.senai.sistema_almoxarifado.repository.UsuarioRepository;
import com.senai.sistema_almoxarifado.dto.usuario.UsuarioCadastroDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    public void cadastrar(UsuarioCadastroDto dto) {
        if (dto.senha().getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new IllegalArgumentException("A senha deve ter até 72 bytes.");
        }
        try {
            usuarioRepository.saveAndFlush(new UsuarioEntity(null, dto.nome().strip(), dto.login().strip(),
                    passwordEncoder.encode(dto.senha()), dto.papel()));
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Este login já está em uso.", e);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioEntity usuario = usuarioRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        return new User(
                usuario.getLogin(),
                usuario.getSenha(),
                List.of(new SimpleGrantedAuthority(usuario.getPapel().name()))
        );
    }
}