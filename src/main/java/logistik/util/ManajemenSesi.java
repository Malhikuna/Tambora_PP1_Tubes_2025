package logistik.util;

import logistik.model.User;

public class ManajemenSesi {
    private static User userYangLogin;

    public static User getUserYangLogin() {
        return userYangLogin;
    }

    public static void setUserYangLogin(User userYangLogin) {
        ManajemenSesi.userYangLogin = userYangLogin;
    }

    public static void hapusSession() {
        userYangLogin = null;
    }
}
