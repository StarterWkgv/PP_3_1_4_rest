package ru.kata.spring.boot_security.demo.mapper;

public interface Mapper<F, T> {
    T map(F obj);
}
