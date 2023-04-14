package com.service.jewelry.model;

public enum OrderStatus {
    WAITING_APPROVE("Ожидание подтверждения"),
    IN_PROGRESS("В прогрессе"),
    CLOSED("Закрыт");

    final String russianName;

    public String getRussianName() {
        return russianName;
    }

    OrderStatus(String russianName) {
        this.russianName = russianName;
    }

}
