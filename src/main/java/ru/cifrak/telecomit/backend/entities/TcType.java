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
        String result;
        switch (this) {
            case INET:
                result = "Интернет";
                break;
            case MOBILE:
                result = "Мобильная связь";
                break;
            case ATS:
                result = "АТС";
                break;
            case PAYPHONE:
                result = "Таксофоны";
                break;
            case INFOMAT:
                result = "Инфомат";
                break;
            case POST:
                result = "Почта";
                break;
            case RADIO:
                result = "Радио";
                break;
            case TV:
                result = "ТВ";
                break;
            default:
                result = super.toString();
        }
        return result;
    }
}
