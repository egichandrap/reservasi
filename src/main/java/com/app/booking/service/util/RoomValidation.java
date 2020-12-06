package com.app.booking.service.util;

public class RoomValidation {
    public static boolean isValidRoom(String target, String namaRoomBaru, String namaRoomLama) {
        boolean baru = target.compareTo(namaRoomBaru) >= 0;
        boolean lama = target.compareTo(namaRoomLama) <= 0;
        boolean result = baru && lama;

        return result;
    }
}
