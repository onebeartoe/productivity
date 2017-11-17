
package org.onebeartoe.filesystem.find;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.onebeartoe.application.duration.DurationService;

/**
 * @author Roberto Marquez
 */
public class Findette 
{
    private final String SPECIAL_CHARACTER_FILENAMES = "specialCharacterFilenames";
    
    private Options buildOptions()
    {
        Option outfile = Option.builder()
                        .required(false)
                        .longOpt(SPECIAL_CHARACTER_FILENAMES)
                        .hasArg(true)
                        .build();

        Options options = new Options();
        options.addOption(outfile);
        
        return options;
    }
    
    public static void main(String [] args) throws IOException
    {
        Findette findette = new Findette();
        Options options = findette.buildOptions();
        
        try
        {
            FindetteRunProfile runProfile = findette.parseRunProfile(args, options);

            Instant start = Instant.now();

            FindetteService findetteService = new FindetteService();
            findetteService.serviceRequest(runProfile);

            Instant end = Instant.now();
        
            DurationService durationService = new DurationService();
            String message = durationService.durationMessage(start, end);
            System.out.println();
            System.out.println(message);
        }
        catch(UnrecognizedOptionException uoe)
        {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar findette.jar [path]", options);
        } 
        catch (ParseException ex) 
        {
            Logger.getLogger(Findette.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private FindetteRunProfile parseRunProfile(final String[] args, Options options) throws ParseException
    {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        
        FindetteRunProfile runProfile = new FindetteRunProfile();
        
        if( cmd.hasOption(SPECIAL_CHARACTER_FILENAMES) )
        {
            runProfile.mode = FindetteRunProfile.RunMode.SHOW_FILENAMES_WITH_SPECIAL_CHARACTERS;
        }
        
        if(runProfile.mode == null)
        {
            runProfile.mode = FindetteRunProfile.RunMode.SHOW_FILENAMES_WITH_SPECIAL_CHARACTERS;
        }
        
        List<String> remainingArgs = cmd.getArgList();
        
        System.out.println("Remaining args:");
        remainingArgs.forEach(a -> System.out.println(a));

        if( remainingArgs.isEmpty() )
        {
            // by default, use the current directory as the path if no 
            // argument is given
            runProfile.inpath = ".";
        }
        else
        {
            // use the first argument as the path to the .scad files
            runProfile.inpath = remainingArgs.get(0);
        }
        
        return runProfile;
    }
}
