
package org.onebeartoe.network.mail;

import java.io.IOException;
import java.time.Instant;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.onebeartoe.application.duration.DurationService;

/**
 * @author Roberto Marquez
 */
public class CommandLineInterfaceApplet 
{
    protected CommandLineInterfaceApplet applet;
    
    public Options buildOptions()
    {
        return new Options();
    }
    
    public CommandLineInterfaceApplet getApplet()
    {
        return new CommandLineInterfaceApplet();
    }
    
    public void execute(String [] args)
    {
        Options options = buildOptions();
        
        try
        {
            RunProfile runProfile = parseRunProfile(args, options);

            Instant start = Instant.now();

            AppletService appletService = getService();
            appletService.serviceRequest(runProfile);

            Instant end = Instant.now();
        
            DurationService durationService = new DurationService();
            String message = durationService.durationMessage(start, end);
            System.out.println();
            System.out.println(message);
        }
        catch(ParseException uoe)
        {
            uoe.printStackTrace();
            
            String usage = applet.getUsage();
            
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(usage, options);
        }         
    }
    
    public static void main(String [] args) throws IOException
    {
        CommandLineInterfaceApplet app = new CommandLineInterfaceApplet();
        app.execute(args);
    }

    protected RunProfile parseRunProfile(final String[] args, Options options) throws ParseException
    {
        return new RunProfile();
    }
    
    protected String getUsage()
    {
        return "No usage documentation is available.";
    }

    protected AppletService getService() 
    {
        return new AppletService();
    }
}
