package ru.cifrak.telecomit.backend.entities.locationsummary;

public enum ChangeSource {

    IMPORT,
    REQUEST,
    EDITING;

    @Override
    public String toString() {
        String result = null;
        switch (this) {
            case IMPORT:
                result = "Импорт";
                break;
            case REQUEST:
                result = "Уточняющая заявка";
                break;
            case EDITING:
                result = "Ручной ввод";
                break;
        }
        return result;
    }
}
