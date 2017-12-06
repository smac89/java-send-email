package org.food.ordering.signup;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
public void confirmationCodeWithCallback_willCallTheCallbackWithConfirmationCode() throws InterruptedException {
    final CountDownLatch lock = new CountDownLatch(1);
    SignUp signUp = new SignUp("myusername@myhost.com");
    final boolean[] callbackCalled = {false};
    signUp.sendConfirmationEmail(new EmailCallback() {
        @Override
        public void emailSent(String confirmationCode) {
            callbackCalled[0] = true;
            lock.countDown();
        }
    });

    lock.await(10, TimeUnit.SECONDS);
    assertTrue(callbackCalled[0]);
}
}
