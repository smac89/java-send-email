package org.food.ordering.signup;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class SignupTest {

    @Test
    public void signup_willReturnConfirmationCode() {
        SignUp signUp = new SignUp("myusername@myhost.com");
        String confirmationCode = signUp.sendConfirmationEmail();
        assertNotNull(confirmationCode);
    }
}
