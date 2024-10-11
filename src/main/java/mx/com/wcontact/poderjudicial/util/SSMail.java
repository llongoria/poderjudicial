package mx.com.wcontact.poderjudicial.util;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import mx.com.wcontact.poderjudicial.bl.BulletinBL;
import mx.com.wcontact.poderjudicial.listener.PJContextListener;

import java.util.Properties;

public class SSMail {

    private static final org.jboss.logging.Logger log = org.jboss.logging.Logger.getLogger(BulletinBL.class.getName());

    Properties prop = new Properties();
    private Message message = null;

    public SSMail() {
        prop.put("mail.smtp.host", PJContextListener.getCfg().getMailSmtpHost());
        prop.put("mail.smtp.port", PJContextListener.getCfg().getMailSmtpPort());
        prop.put("mail.smtp.auth", PJContextListener.getCfg().isMailSmtpAuth());
        //prop.put("mail.smtp.socketFactory.port", PJContextListener.getCfg().getMailSmtpPort());
        //prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.starttls.enable", PJContextListener.getCfg().isMailUseSsl()); //TLS
    }


    public  void buildMessage(String subject, String mailTo, String htmlContent) {

        Session session = Session.getInstance(prop,
                new jakarta.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(PJContextListener.getCfg().getMailUser(), PJContextListener.getCfg().getMailPassword());
                    }
                });

        try {

            this.message = new MimeMessage(session);
            this.message.setFrom(new InternetAddress(PJContextListener.getCfg().getMailFrom())); // reemplazar con tu correo de Gmail
            this.message.setRecipients( Message.RecipientType.TO, InternetAddress.parse(mailTo)  );
            this.message.setRecipients( Message.RecipientType.BCC, InternetAddress.parse( PJContextListener.getCfg().getMailFrom()) );
            this.message.setSubject(subject);
            this.message.setContent(htmlContent, "text/html");

        } catch (MessagingException ex) {
            log.error( "Error al enviar correo!", ex);
        }
    }



    public void sendMessage() throws MessagingException {
        jakarta.mail.Transport.send(this.message);
    }

    public static void main(String[] args) {
        SSMail mail = new SSMail();
        mail.buildMessage(
                "Prueba de correo",
                "tuzka@hotmail.com",
                EmailTemplate.Coincidencia("Boletin Poder Judicial del Estado", "Usuario", "2024-09-19 14:39:03.543", "2024-09-19 14:39:03.543", "1135/2024","Comentario de Prueba","Banregio", "LUIS LEONEL LONGORIA CASTILLO")
        );
        try {
            mail.sendMessage();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


}
