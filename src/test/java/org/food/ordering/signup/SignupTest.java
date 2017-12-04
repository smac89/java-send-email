package org.food.ordering.signup;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SignupTest {

    @Test
    public void confirmationCode_willReturnConfirmationCode() {
        SignUp signUp = new SignUp("myusername@myhost.com");
        String confirmationCode = signUp.sendConfirmationEmail();
        assertNotNull(confirmationCode);
    }

    @Test
    public void confirmationCodeWithCallback_willCallTheCallbackWithConfirmationCode() {
        SignUp signUp = new SignUp("myusername@myhost.com");
        final boolean[] callbackCalled = {false};
        signUp.sendConfirmationEmail(new EmailCallback() {
            @Override
            public void emailSent(String confirmationCode) {
                callbackCalled[0] = true;
            }
        });
        assertTrue(callbackCalled[0]);
    }
}
