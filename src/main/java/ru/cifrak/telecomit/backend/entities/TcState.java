package ru.cifrak.telecomit.backend.entities;


/**
 * Состояние Технической возможности
 */
public enum TcState {
    ARCHIVE,
    ACTIVE,
    PLAN1YEAR,
    PLAN2YEAR,
    WAIT_FOR_STATE_TO_BE_SET,
    PLAN;

    @Override
    public String toString() {
        switch (this) {
            case ARCHIVE :   return "Архив";
            case ACTIVE:  return "Текущая";
            case WAIT_FOR_STATE_TO_BE_SET:  return "Статус не установлен";
            case PLAN:  return "Плановая";
            case PLAN1YEAR:  return "План 1 год";
            case PLAN2YEAR:  return "План 2 года";
            default:    throw new IllegalArgumentException();
        }
    }
}
