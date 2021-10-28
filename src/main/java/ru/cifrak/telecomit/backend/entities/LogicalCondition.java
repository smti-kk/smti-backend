package ru.cifrak.telecomit.backend.entities;

public enum LogicalCondition {
    OR,
    AND;

    @Override
    public String toString() {
        String result;
        if (this == LogicalCondition.OR) {
            result = "ИЛИ";
        } else {
            result = "И";
        }
        return result;
    }
}
