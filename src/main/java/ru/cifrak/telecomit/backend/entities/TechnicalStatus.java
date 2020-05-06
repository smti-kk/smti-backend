package ru.cifrak.telecomit.backend.entities;


public enum TechnicalStatus {
    PENDING,
    CONFIRMED,
    DECLINED,
    OUTDATED;

    @Override
    public String toString() {
        switch (this) {
            case PENDING:   return "В ожидании";
            case CONFIRMED:  return "Подтверждена";
            case DECLINED:  return "Отклонена";
            case OUTDATED:  return "Устарела";
            default:    throw new IllegalArgumentException();
        }
    }
}
