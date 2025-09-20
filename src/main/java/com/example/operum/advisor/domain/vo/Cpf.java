package com.example.operum.advisor.domain.vo;

import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Cpf {

    private String value;

    protected Cpf() {
        // JPA only
    }

    private Cpf(String value) {
        this.value = value;
    }

    public static Cpf of(String rawValue) {
        String digits = rawValue == null ? null : rawValue.replaceAll("\\D", "");
        if (!isValid(digits)) {
            throw new IllegalArgumentException("CPF invalido");
        }
        return new Cpf(digits);
    }

    public String getValue() {
        return value;
    }

    private static boolean isValid(String cpf) {
        if (cpf == null || cpf.length() != 11 || cpf.chars().distinct().count() == 1) {
            return false;
        }
        return checkDigit(cpf, 9) == Character.getNumericValue(cpf.charAt(9))
            && checkDigit(cpf, 10) == Character.getNumericValue(cpf.charAt(10));
    }

    private static int checkDigit(String cpf, int position) {
        int sum = 0;
        for (int i = 0; i < position; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (position + 1 - i);
        }
        int mod = sum % 11;
        return mod < 2 ? 0 : 11 - mod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cpf cpf = (Cpf) o;
        return Objects.equals(value, cpf.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
