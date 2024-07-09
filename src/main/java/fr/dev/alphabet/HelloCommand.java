package fr.dev.alphabet;

import picocli.CommandLine;

@CommandLine.Command(name = "hello", description = "Greet World!")
class HelloCommand implements Runnable {

    @Override
    public void run() {
        System.out.println("Hello World!");
    }
}
