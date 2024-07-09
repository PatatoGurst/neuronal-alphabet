package fr.dev.alphabet;

import picocli.CommandLine;

@CommandLine.Command(name = "goodbye", description = "Say goodbye to World!")
class GoodByeCommand implements Runnable {

    @Override
    public void run() {
        System.out.println("Goodbye World!");
    }
}
