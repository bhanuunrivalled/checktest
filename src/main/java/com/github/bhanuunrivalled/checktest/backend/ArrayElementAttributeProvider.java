package com.github.bhanuunrivalled.checktest.backend;

@FunctionalInterface
public interface ArrayElementAttributeProvider {
    String getAttribute(Object array, int index);
}
