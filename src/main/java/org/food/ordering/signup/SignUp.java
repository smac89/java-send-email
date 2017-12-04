package org.food.ordering.signup;

import org.hazlewood.connor.bottema.emailaddress.EmailAddressValidator;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;

import java.util.UUID;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class SignUp {
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(10);
    private static final String EMAIL_CONFIRMATION_TEMPLATE = "Your confirmation code is: %s";

    public final String email;

    public SignUp(String email) {
        this.email = email;
    }

	public boolean isEmailValid() {
        return EmailAddressValidator.isValid(email);
    }

    private static Email constructEmailMessage(String messageCode, String newUserEmail) {
        return new EmailBuilder()
                .to(newUserEmail)
                .text(String.format(EMAIL_CONFIRMATION_TEMPLATE, messageCode))
                .addHeader("X-Priority", 5)
                .build();
    }

    public String sendConfirmationEmail() {
        if (!isEmailValid()) {
            throw new IllegalArgumentException("Invalid email address supplied!");
        }
        final String confirmationCode = UUID.randomUUID().toString();
        EXECUTOR_SERVICE.submit(new Runnable() {
            @Override
            public void run() {
                sendConfirmationEmail(confirmationCode, email);
            }
        });
        return confirmationCode;
	}

    public void sendConfirmationEmail(final EmailCallback callBack) {
        if (!isEmailValid()) {
            throw new IllegalArgumentException("Invalid email address supplied!");
        }
        EXECUTOR_SERVICE.submit(new Runnable() {
            @Override
            public void run() {
                final String confirmationCode = UUID.randomUUID().toString();
                sendConfirmationEmail(confirmationCode, email);
                callBack.emailSent(confirmationCode);
            }
        });
    }

	private static void sendConfirmationEmail(String messageCode, String email) {
        Logger.getAnonymousLogger().info("Email about to be sent");
	    Email emailMessage = constructEmailMessage(messageCode, email);
	    new Mailer(null, null, null, System.getenv("EMAIL_PASSWORD")).sendMail(emailMessage);
        Logger.getAnonymousLogger().info("Email has been sent to " + email);
	}
}
