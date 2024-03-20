package org.koregin.socks_app.exceptions;

public class NotEnoughSocksInWarehouseException extends RuntimeException {

    public NotEnoughSocksInWarehouseException(String message) {
        super(message);
    }
}
