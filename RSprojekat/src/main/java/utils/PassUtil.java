package utils;

import org.mindrot.jbcrypt.BCrypt;

public class PassUtil {
    // Hashiranje sifre prilikom registracije
    public static String hashPassword(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(10));
    }

    // Provjera prilikom logina
    public static boolean checkPassword(String rawPassword, String hashFromDb) {
        return BCrypt.checkpw(rawPassword, hashFromDb);
    }
}
