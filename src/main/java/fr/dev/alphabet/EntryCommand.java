package fr.dev.alphabet;

import fr.dev.alphabet.neuronal.*;
import io.quarkus.picocli.runtime.annotations.TopCommand;
import picocli.CommandLine;

@TopCommand
@CommandLine.Command(mixinStandardHelpOptions = true, subcommands = { HelloCommand.class, GoodByeCommand.class,
                                                                      InitCommand.class, NewCommand.class,
                                                                      CalculCommand.class, EntraineCommand.class,
                                                                      CoutCommand.class, TauxCommand.class })
public class EntryCommand {
    // nothing
}