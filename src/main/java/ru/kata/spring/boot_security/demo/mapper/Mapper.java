package ru.kata.spring.boot_security.demo.mapper;

public interface Mapper<F, T> {
    T map(F from);

    default F copy(T from, F to) {
        return to;
    }
}
