package com.example.operum.advisor.repository;

import com.example.operum.advisor.domain.entity.Cliente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByCpfValue(String cpf);

    boolean existsByEmail(String email);

    Optional<Cliente> findByCpfValue(String cpf);

    Optional<Cliente> findByEmail(String email);
}
