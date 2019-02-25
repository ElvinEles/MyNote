package mynote.com.mynote.Common;

import mynote.com.mynote.Database.Local.MyDatabase;
import mynote.com.mynote.Database.Model.MyInMoney;
import mynote.com.mynote.Database.Model.MyOutMoney;
import mynote.com.mynote.Database.Model.Note;
import mynote.com.mynote.Database.Model.User;

public class Common {

    public static User current_user;
    public static Note deleted_note;

    public static String KEY_LOGIN = "LOGIN";
    public static String KEY_PASSWORD = "PASSWORD";
    public static MyInMoney deleted_myinmoney;
    public static MyOutMoney deleted_myoutmoney;
}
