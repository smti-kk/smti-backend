package ru.cifrak.telecomit.backend.entities;

public enum ExcelExportTypes {
    TECHCAP_INTERNET,
    TECHCAP_MOBILE,
    ORGANIZATION,
    CONTRACT;

    @Override
    public String toString() {
        switch (this) {
            case TECHCAP_INTERNET:  {
                return "Технические_возможности_интернет";
            }
            case TECHCAP_MOBILE:  {
                return "Технические_возможности_сотовая_связь";
            }
            case ORGANIZATION:  {
                return "Организации";
            }
            case CONTRACT:  {
                return "Контракты";
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }

    public static ExcelExportTypes fromTcType(TcType tcType) {
        switch (tcType) {
            case INET:
                return ExcelExportTypes.TECHCAP_INTERNET;
            case TV:
                return ExcelExportTypes.TECHCAP_MOBILE;
            default:
                throw new IllegalArgumentException("sorry, " + tcType + " not supported");
        }
    }
}
