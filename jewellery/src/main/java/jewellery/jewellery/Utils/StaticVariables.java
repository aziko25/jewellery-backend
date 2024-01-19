package jewellery.jewellery.Utils;

import jewellery.jewellery.Models.Users;

import java.time.format.DateTimeFormatter;

public class StaticVariables {

    // USERS

    public static String secretKeyString;

    public static Integer USER_ID;
    public static String USERNAME;
    public static String ROLE;
    public static Users USER;

    // DATE TIME FORMATTER

    public static DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
}