package com.example.project.common;

import java.util.regex.Pattern;

public class Constants {
    public static final Pattern EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final int TAKE_PHOTO_CODE = 1;
    public static final int SELECT_LIBRARY_CODE = 2;
    public static final String USER = "id_user";
    public static final String PRODUCT_DETAILS = "product_details";
    public static final String USER_ID = "idUser";
    public static final String LIST_IMAGES = "listImage";
    public static final String PRODUCT_NAME = "name";
    public static final String PRODUCT_BRAND = "brand";
    public static final String PRODUCT_QUANTITY = "quantity";
    public static final String PRODUCT_COLOR = "color";
    public static final String PRODUCT_TYPE = "type";
    public static final String PRODUCT_PRICE = "price";
}
