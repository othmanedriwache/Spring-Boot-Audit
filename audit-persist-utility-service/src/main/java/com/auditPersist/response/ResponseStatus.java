package com.auditPersist.response;

public enum ResponseStatus {

    SUCCESS(200, "SUCCESS"),
    BAD_REQUEST(400, "BAD REQUEST"),
    WARNING(406, "WARNING");


    private final int code;
    private final String reasonPhrase;

    ResponseStatus(int code, String reasonPhrase) {
        this.code = code;
        this.reasonPhrase = reasonPhrase;
    }

    public int code() {
        return this.code;
    }
}
