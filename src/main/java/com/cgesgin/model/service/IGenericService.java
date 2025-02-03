package com.cgesgin.model.service;

import java.util.List;

public interface IGenericService<T> {
    T save(T value);
    Boolean remove(T value);
    T update(T value);
    List<T> getAll();
    T get(int id);
}
