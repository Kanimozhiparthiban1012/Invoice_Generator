package in.kanimozhi.invoicegeneratorapi.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendInvoiceEmail(
            String customerEmail,
            MultipartFile file,
            String clerkUserId
    ) throws Exception {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper =
                new MimeMessageHelper(message, true);

        helper.setTo(customerEmail);
        helper.setSubject("Your Invoice");
        helper.setText(
                "Hello,\n\nPlease find your invoice attached.\n\nSent by user: "
                        + clerkUserId +
                        "\n\nThank you."
        );

        helper.addAttachment(
                file.getOriginalFilename(),
                file
        );

        mailSender.send(message);
    }
}