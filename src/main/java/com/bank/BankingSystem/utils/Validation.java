package com.bank.BankingSystem.utils;


import org.springframework.beans.factory.support.BeanDefinitionValidationException;

@FunctionalInterface
public interface Validation {
    void validate(String value) throws BeanDefinitionValidationException;
}
