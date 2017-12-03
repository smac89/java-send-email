package org.food.ordering;

import org.food.ordering.signup.SignUp;

import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
//        SignUp signUp = new SignUp("some-email@somehost.com");
        SignUp signUp = new SignUp("myusername@myhost.com");
        if (signUp.isEmailValid()) {
            String confirmationCode = signUp.sendConfirmationEmail();
            Logger.getAnonymousLogger().info("Email is sending with confirmation code: " + confirmationCode);
        } else {
            Logger.getAnonymousLogger().warning("Invalid email supplied: " + signUp.email);
        }
    }
}
