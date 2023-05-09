package com.sjbaek.chapter3.domain.format;

public interface Formatter<T> {
    String of(T target);
}
