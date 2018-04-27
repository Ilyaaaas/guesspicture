package biz.letsoft.guesspicture;

/**
 * Created by Ильяс on 2-Jan-18.
 */
public class User {
    public static String login;
    public static String password;

    User(String login_param, String password_param)
    {
        this.login = login_param;
        this.password = password_param;
    }
}
