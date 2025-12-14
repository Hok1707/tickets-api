package com.kimhok.tickets.config.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendResetPasswordEmail(String to, String resetLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Reset Your Password");

            String html = getResetPasswordTemplate()
                    .replace("{{reset_link}}", resetLink)
                    .replace("{{year}}", String.valueOf(LocalDate.now().getYear()));

            helper.setText(html, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendEmailVerify(String to, String verifyLink) {
        try {

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject("Verify Register Email");
            String html = getVerifyEmailTemplate()
                    .replace("{{verify_link}}", verifyLink)
                    .replace("{{year}}", String.valueOf(LocalDate.now().getYear()));
            helper.setText(html, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException exception) {
            throw new RuntimeException("Error to send email ", exception);
        }
    }

    private String getResetPasswordTemplate() {
        return """
                <!DOCTYPE html>
                <html lang="en" style="margin:0;padding:0;">
                  <head>
                    <meta charset="UTF-8" />
                    <meta name="viewport" content="width=device-width" />
                    <title>Password Reset</title>
                  </head>

                  <body style="margin:0; padding:0; background-color:#f5f7fa; font-family:Arial, sans-serif;">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0" style="background-color:#f5f7fa; padding:40px 0;">
                      <tr>
                        <td align="center">

                          <table width="600" cellspacing="0" cellpadding="0" style="background:white; border-radius:10px; overflow:hidden; box-shadow:0 4px 20px rgba(0,0,0,0.1);">

                            <tr>
                              <td style="background:#2563eb; padding:20px; text-align:center;">
                                <h2 style="color:white; margin:0; font-size:22px;">
                                  Reset Your Password
                                </h2>
                              </td>
                            </tr>

                            <tr>
                              <td style="padding:30px; color:#333;">

                                <p style="font-size:16px; margin:0 0 15px;">
                                  Hi,
                                </p>

                                <p style="font-size:16px; margin:0 0 20px;">
                                  We received a request to reset your password. Click the button below to reset it:
                                </p>

                                <div style="text-align:center; margin:30px 0;">
                                  <a 
                                    href="{{reset_link}}" 
                                    style="background:#2563eb; padding:14px 28px; color:white; text-decoration:none; font-size:16px; border-radius:6px; display:inline-block;">
                                    Reset Password
                                  </a>
                                </div>

                                <p style="font-size:15px; margin:0 0 15px;">
                                  If the button doesn't work, copy and paste this link into your browser:
                                </p>

                                <p style="background:#f1f5f9; padding:10px; border-radius:6px; font-size:13px; color:#2563eb; word-break:break-all;">
                                  {{reset_link}}
                                </p>

                                <p style="font-size:14px; margin-top:25px; color:#666;">
                                  This link is valid for 15 minutes. If you didn’t request a password reset, you can safely ignore this email.
                                </p>

                              </td>
                            </tr>

                            <tr>
                              <td style="background:#f8fafc; padding:15px; text-align:center; color:#94a3b8; font-size:12px;">
                                © {{year}} hengkimhok — All Rights Reserved.
                              </td>
                            </tr>

                          </table>

                        </td>
                      </tr>
                    </table>
                  </body>
                </html>
                """;
    }

    private String getVerifyEmailTemplate() {
        return """
                <!DOCTYPE html>
                <html lang="en" style="margin:0;padding:0;">
                  <head>
                    <meta charset="UTF-8" />
                    <meta name="viewport" content="width=device-width" />
                    <title>Verify Email</title>
                  </head>

                  <body style="margin:0; padding:0; background-color:#f5f7fa; font-family:Arial, sans-serif;">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0" style="background-color:#f5f7fa; padding:40px 0;">
                      <tr>
                        <td align="center">

                          <table width="600" cellspacing="0" cellpadding="0" style="background:white; border-radius:10px; overflow:hidden; box-shadow:0 4px 20px rgba(0,0,0,0.1);">

                            <tr>
                              <td style="background:#2563eb; padding:20px; text-align:center;">
                                <h2 style="color:white; margin:0; font-size:22px;">
                                  Verify Your Email
                                </h2>
                              </td>
                            </tr>

                            <tr>
                              <td style="padding:30px; color:#333;">

                                <p style="font-size:16px; margin:0 0 15px;">
                                  Hi,
                                </p>

                                <p style="font-size:16px; margin:0 0 20px;">
                                  We received a request to verify your email. Click the button below to verify it:
                                </p>

                                <div style="text-align:center; margin:30px 0;">
                                  <a 
                                    href="{{reset_link}}" 
                                    style="background:#2563eb; padding:14px 28px; color:white; text-decoration:none; font-size:16px; border-radius:6px; display:inline-block;">
                                    Verify Email
                                  </a>
                                </div>

                                <p style="font-size:15px; margin:0 0 15px;">
                                  If the button doesn't work, copy and paste this link into your browser:
                                </p>

                                <p style="background:#f1f5f9; padding:10px; border-radius:6px; font-size:13px; color:#2563eb; word-break:break-all;">
                                  {{verify_link}}
                                </p>

                                <p style="font-size:14px; margin-top:25px; color:#666;">
                                  This link is valid for 15 minutes. If you didn’t request a password reset, you can safely ignore this email.
                                </p>

                              </td>
                            </tr>

                            <tr>
                              <td style="background:#f8fafc; padding:15px; text-align:center; color:#94a3b8; font-size:12px;">
                                © {{year}} hengkimhok — All Rights Reserved.
                              </td>
                            </tr>

                          </table>

                        </td>
                      </tr>
                    </table>
                  </body>
                </html>
                """;
    }

}
