package ru.cifrak.telecomit.backend.entities.locationsummary;

public enum FeatureEditAction {
    UPDATE,
    CREATE,
    DELETE;

    @Override
    public String toString() {
        String result = null;
        switch (this) {
            case UPDATE:
                result = "Изменение";
                break;
            case CREATE:
                result = "Создание";
                break;
            case DELETE:
                result = "Удаление";
                break;
        }
        return result;
    }
}
