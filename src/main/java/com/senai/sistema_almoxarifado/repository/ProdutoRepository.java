package com.senai.sistema_almoxarifado.repository;

import com.senai.sistema_almoxarifado.entity.ProdutoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import java.time.LocalDateTime;
import java.util.List;

public interface ProdutoRepository extends JpaRepository<ProdutoEntity,Long> {

    @Query("""
            select p from ProdutoEntity p
            where (:nome is null or lower(p.nome) like lower(concat('%', :nome, '%')))
              and (:inicio is null or p.dataCadastro >= :inicio)
              and (:fim is null or p.dataCadastro < :fim)
            """)
    Page<ProdutoEntity> buscar(@Param("nome") String nome, @Param("inicio") LocalDateTime inicio,
                               @Param("fim") LocalDateTime fim, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from ProdutoEntity p where p.id = :id")
    Optional<ProdutoEntity> buscarParaAtualizar(@Param("id") Long id);
    List<ProdutoEntity> findByCodigoContainingIgnoreCaseOrNomeContainingIgnoreCase(String codigo, String nome);

    boolean existsByCodigo(String codigo);

    List<ProdutoEntity> findAllByOrderByNomeAsc();
}
