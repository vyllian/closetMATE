package com.vylitkova.closetMATE.entity.item.enums;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EnumUtils {
    public static <T extends Enum<T>> String enumToString(Class<T> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .map(String::toLowerCase)
                .collect(Collectors.joining(", "));
    }


}