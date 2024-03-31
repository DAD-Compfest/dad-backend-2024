package com.dadcompfest.backend.modules.authmodule.service;

import java.util.List;


public interface ICrudService<T>{
    T findByUsername(String username);
    List<T> getAll();
    T createOrUpdate(T entity);

    T create(T entity);
    void remove(T entity);
}
