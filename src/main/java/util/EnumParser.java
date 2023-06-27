package util;

import domain.BasicEnum;

public class EnumParser {

    private EnumParser() {
    }

    public static <T extends BasicEnum> T fromValue(Class<T> enumType, String value) {

        for (T enumConstant : enumType.getEnumConstants()) {
            if (enumConstant.getValue().equals(value)) {
                return enumConstant;
            }
        }
        throw new IllegalArgumentException("There is no Enum like " + value);
    }

}
