
package org.onebeartoe.network.mail;

import java.io.IOException;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * @author Roberto Marquez
 */
public class Sendette extends CommandLineInterfaceApplet
{
    private final String SMTP_FORCE_PASSWORD = "forceSmtpPassword";
//    private final String SMTP_PASSWORD = "smtpPassword";
    private final String SUBJECT = "subject";
    private final String TO = "to";
    
    @Override
    public Options buildOptions()
    {
        Option to = Option.builder()
                        .required()
                        .hasArg()
                        .longOpt(TO)
                        .desc("This is the email address of the recipient.")
                        .build();
        
        Option smtpPassword = Option.builder()
                                .hasArg()
                                .longOpt(SMTP_FORCE_PASSWORD)
                                .build();
        
        Options options = new Options();
        options.addOption(smtpPassword);
        options.addOption(to);
        
        return options;
    }    
    
    @Override
    public CommandLineInterfaceApplet getApplet()
    {
        return new Sendette();
    }
    
    @Override
    protected AppletService getService() 
    {
        return new SendetteService();
    }
    
    public static void main(String [] args) throws IOException, Exception
    {
        CommandLineInterfaceApplet app = new Sendette();
        app.execute(args);
    }

    @Override
    protected RunProfile parseRunProfile(final String[] args, Options options) throws ParseException
    {
        CommandLineParser parser = new DefaultParser();
        CommandLine cl = parser.parse(options, args);

        String to = cl.getOptionValue(TO);
        String subject = cl.getOptionValue(SUBJECT, "Wonderful Subject");
        
        SendetteRunProfile runProfile = new SendetteRunProfile();
        runProfile.to = to;
        runProfile.subject = subject;
        
        if( cl.hasOption(SMTP_FORCE_PASSWORD) )
        {
            runProfile.forceSmtpPassword = true;
            runProfile.smtpPassword = cl.getOptionValue(SMTP_FORCE_PASSWORD);
        }
        
        List<String> remainingArgs = cl.getArgList();
        if(remainingArgs.size() > 0)
        {
            remainingArgs.forEach(System.out::println);
        }        
        
        return runProfile;
    }
    
    class SendetteRunProfile extends RunProfile
    {
        String subject;
        String to;
        boolean forceSmtpPassword;
        String smtpPassword;
    }
}
