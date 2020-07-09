package ru.cifrak.telecomit.backend.utils;

import com.querydsl.core.types.dsl.BooleanExpression;

import javax.annotation.Nullable;

public class LogicalAndOrLogicalOrFilter {
    private final LogicalAndOrLogicalOrFilterType type;
    private BooleanExpression filter;

    public LogicalAndOrLogicalOrFilter(LogicalAndOrLogicalOrFilterType type) {
        this.type = type;
    }

    public LogicalAndOrLogicalOrFilter with(@Nullable BooleanExpression right) {
        if (right == null) {
            throw new IllegalArgumentException("expression must not be a null");
        }
        if (this.filter == null) {
            this.filter = right;
        }
        if (type == LogicalAndOrLogicalOrFilterType.AND) {
            filter = filter.and(right);
        } else {
            filter = filter.or(right);
        }
        return this;
    }

    @Nullable
    public BooleanExpression build() {
        return this.filter;
    }
}
