package ru.cifrak.telecomit.backend.entities;

public enum TcType {
    INET,
    TV,
    POST,
    RADIO,
    PAYPHONE,
    MOBILE,
    ATS,
    INFOMAT;

    @Override
    public String toString() {
        switch (this) {
            case INET:  return "Интернет";
            case MOBILE:  return "Сотовая связь";
            default:  return super.toString();
        }
    }
}
