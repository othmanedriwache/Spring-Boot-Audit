package com.demo.response;

/**
 * The enum Response status.
 */
public enum ResponseStatus {

    /**
     * Success response status.
     */
    SUCCESS(200, "SUCCESS"),
    /**
     * The Bad request.
     */
    BAD_REQUEST(400, "BAD REQUEST"),
    /**
     * Warning response status.
     */
    WARNING(406, "WARNING");


    private final int code;

    ResponseStatus(int code, String reasonPhrase) {
        this.code = code;
    }

    /**
     * Code int.
     *
     * @return the int
     */
    public int code() {
        return this.code;
    }
}
