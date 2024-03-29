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

    public static String TG_GROUP_ID = "-1001991811481";
    //public static String TG_GROUP_ID = "-4149040075";

    public static String DOCUMENTS_SAVING_DIR = "/var/www/jewellery-docs";
}