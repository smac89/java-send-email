package org.food.ordering.signup;

import org.hazlewood.connor.bottema.emailaddress.EmailAddressCriteria;
import org.hazlewood.connor.bottema.emailaddress.EmailAddressValidator;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import static java.util.EnumSet.of;

public class SignUp {
    private static final String EMAIL_CONFIRMATION_TEMPLATE = "Your confirmation code is: %s";
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public final String email;

    public SignUp(String email) {
        this.email = email;
    }

	public boolean isEmailValid() {
        return EmailAddressValidator.isValid(email, of(EmailAddressCriteria.ALLOW_QUOTED_IDENTIFIERS));
	}

    public String sendConfirmationEmail() {
        if (!isEmailValid()) {
            throw new IllegalArgumentException("Invalid email address supplied!");
        }
        final String confirmationCode = UUID.randomUUID().toString();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                Logger.getAnonymousLogger().info("Email about to be sent");
                sendConfirmationEmail(confirmationCode, email);
                Logger.getAnonymousLogger().info("Email has been sent to " + email);
            }
        });
        return confirmationCode;
    }

    private static Email constructEmailMessage(String messageCode, String newUserEmail) {
        return new EmailBuilder()
                .to(newUserEmail)
                .text(String.format(EMAIL_CONFIRMATION_TEMPLATE, messageCode))
                .addHeader("X-Priority", 5)
                .build();
    }

	private static void sendConfirmationEmail(String messageCode, String newUserEmail) {
	    Email email = constructEmailMessage(messageCode, newUserEmail);
	    new Mailer(null, null, null, System.getenv("EMAIL_PASSWORD")).sendMail(email);
	}
}
