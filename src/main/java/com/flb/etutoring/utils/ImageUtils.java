package com.flb.etutoring.utils;

import java.util.Base64;

public class ImageUtils {
    public static String getImgData(byte[] byteData) {
        if (byteData != null) {
            return Base64.getMimeEncoder().encodeToString(byteData);
        } else {
            return null;
        }
    }
}
