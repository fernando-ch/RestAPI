package br.com.fcamacho.springrestapi.terminal;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface TerminalRepository extends PagingAndSortingRepository<Terminal, Integer> {
    Optional<Terminal> findByLogic(Integer logic);
    boolean existsByLogic(Integer logic);
}
