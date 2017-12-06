package org.food.ordering.signup;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SignupTest {

    @Before
    public void verifyPropertiesFound() {
        System.out.printf("Working directory: %s\n\n", System.getProperty("user.dir"));
        System.out.printf("Class path: %s\n\n",
                System.getProperty("java.class.path").replace(File.pathSeparator, "\n"));

        InputStream stream = SignupTest.class.getResourceAsStream("/simplejavamail.properties");
        if (stream != null) {
            System.out.println("Properties file found!");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                    System.out.println(line);
                }
            } catch (IOException ioe) {
                System.out.println("Error reading the file!!\n\n");
            }
        } else {
            System.out.println("Could not find the file");
        }
    }

    @Test
    public void confirmationCode_willReturnConfirmationCode() {
        SignUp signUp = new SignUp("myusername@myhost.com");
        String confirmationCode = signUp.sendConfirmationEmail();

        assertNotNull(confirmationCode);
    }

    @Test
    public void confirmationCodeWithCallback_willCallTheCallbackWithConfirmationCode() throws InterruptedException {
        final CountDownLatch lock = new CountDownLatch(1);
        final boolean[] callbackCalled = {false};

        SignUp signUp = new SignUp("myusername@myhost.com");
        signUp.sendConfirmationEmail(new EmailCallback() {
            @Override
            public void emailSent(String confirmationCode) {
                callbackCalled[0] = true;
                lock.countDown();
            }
        });

        lock.await(20, TimeUnit.SECONDS);
        assertTrue(callbackCalled[0]);
    }
}
