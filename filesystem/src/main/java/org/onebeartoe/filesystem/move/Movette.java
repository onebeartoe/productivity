
package org.onebeartoe.filesystem.move;

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
import org.onebeartoe.filesystem.find.Findette;

/**
 * @author Roberto Marquez
 */
public class Movette
{
    private final String REMOVE_SPECIAL_CHARACTER_FILENAMES = "removeSpecialCharacterFilenames";
    
    private final String REPLACEMENT_TARGET = "replacementTarget";
    
    private final String REPLACEMENT_STRING = "replacmentString";
    
    private final String JUST_PRINT_RENAME_COMMANDS = "justPrintRenameCommands";
    
    private Options buildOptions()
    {
        Option justPrintRenameCommands = Option.builder()
                                            .longOpt(JUST_PRINT_RENAME_COMMANDS)
                                            .desc("Instead of actually moving the files, this only prints the mv commands to rename the files.")
                                            .build();
                
        Option outfile = Option.builder()
                        .required(false)
                        .longOpt(REMOVE_SPECIAL_CHARACTER_FILENAMES)
                        .hasArg(true)
                        .desc("Specifiying this paramter put the application in replace character mode and a")
                        .build();

        Option replacementTarget = Option.builder()
                                    .required(false)
                                    .longOpt(REPLACEMENT_TARGET)
                                    .hasArg()
                                    .desc("This is the String target to be replaced.")
                                    .build();
        
        Options options = new Options();
        options.addOption(justPrintRenameCommands);
        options.addOption(outfile);
        options.addOption(replacementTarget);
        
        return options;
    }
    
    public static void main(String [] args) throws IOException
    {
        Movette findette = new Movette();
        Options options = findette.buildOptions();
        
        try
        {
            MovetteRunProfile runProfile = findette.parseRunProfile(args, options);

            Instant start = Instant.now();

            MoveService movetteService = new MoveService();
            movetteService.serviceRequest(runProfile);

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

    private MovetteRunProfile parseRunProfile(final String[] args, Options options) throws ParseException
    {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        
        MovetteRunProfile runProfile = new MovetteRunProfile();
        
        if( cmd.hasOption(REMOVE_SPECIAL_CHARACTER_FILENAMES) )
        {
            runProfile.mode = MovetteRunProfile.RunMode.RENAME_SPECIAL_CHARACTER_FILENAMES;
            
            if( !cmd.hasOption(REPLACEMENT_TARGET) )
            {
                String message = "The " + REPLACEMENT_TARGET + " parameter and value are needed when the " 
                                 + REMOVE_SPECIAL_CHARACTER_FILENAMES +" parameter is given.";
                
                throw new ParseException(message);
            }
            else
            {
                String s = cmd.getOptionValue(REPLACEMENT_TARGET);
                if(s.length() != 1)
                {
                    String message = "The value of the " + REPLACEMENT_TARGET + " shold be a single character.";
                    
                    throw new ParseException(message);
                }
                runProfile.specialCharTarget = s.charAt(0);
            }
        }        
        
        if(runProfile.mode == null)
        {
            runProfile.mode = MovetteRunProfile.RunMode.RENAME_SPECIAL_CHARACTER_FILENAMES;
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
