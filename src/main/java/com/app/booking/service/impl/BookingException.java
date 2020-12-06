package com.app.booking.service.impl;

import java.awt.print.Book;

public class BookingException extends Exception {
    public BookingException(){
    }

    public BookingException(String message){
        super(message);
    }

    public BookingException(Throwable cause){
        super(cause);
    }

    public BookingException(String message, Throwable cause){
        super(message, cause);
    }
}
