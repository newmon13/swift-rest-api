package dev.jlipka.swiftrestapi.service;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface EntityService<T> {
    T save(T entity);
    Optional<T> findById(String id);
    List<T> findAll();
    void deleteById(String id);
}