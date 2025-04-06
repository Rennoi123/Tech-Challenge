package com.example.techchallenge.mapper;

public abstract class BaseMapper<T> {
    public abstract T create(T source);
    public abstract void update(T source, T destination);
}
