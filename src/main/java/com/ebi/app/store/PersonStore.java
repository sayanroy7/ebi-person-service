package com.ebi.app.store;

import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PersonStore<T> {

    Optional<T> findById(String id);

    T save(T t);

    T update(T t);

    void deleteById(String id);

    Page<T> findAll(int page, int size);

}
