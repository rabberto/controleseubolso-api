package com.rbbsolucoes.controleseubolso.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rbbsolucoes.controleseubolso.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	boolean existsByEmail(String email);
	
	Optional<Usuario> findByEmail(String email);
	
}
