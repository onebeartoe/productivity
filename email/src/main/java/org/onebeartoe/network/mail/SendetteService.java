
package org.onebeartoe.network.mail;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

/**
 * @author Roberto Marquez
 */
public class SendetteService extends AppletService
{
    public void serviceRequest(RunProfile runProfile) throws AddressException, MessagingException
    {
        String user = "duplicator-pi";
        
        // The password is looked up as an environment variable.
        String key = "SMTP_PASSWORD";
        String pw = System.getProperty(key, "");
        System.out.println("pw: " + pw);
        
        pw = System.getenv(key);
        System.out.println("env pw: " + pw);
        
        if( pw.trim().equals("") )
        {
            throw new IllegalArgumentException("The password is blank.  Try setting the " 
                                + key + " environment variable.");
        }            
        
        String to = "onebeartoe@gmail.com";
        String subject = "calling beto from att";
        String body = "real far far out body";
        
        AttSender sender = new AttSender(user, pw);
        sender.sendMail(subject, body, to);        
        
        System.out.println("The sendette service is great!");
    }
}
