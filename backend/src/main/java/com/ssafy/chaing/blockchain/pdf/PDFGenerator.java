package com.ssafy.chaing.blockchain.pdf;

@FunctionalInterface
public interface PDFGenerator<T> {
    byte[] generate(T t);
}
