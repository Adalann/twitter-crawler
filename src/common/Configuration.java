/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui contient les constantes du programme.
*/

package common;

import java.io.PrintStream;

public abstract class Configuration
{
    // Constantes pour l'affichage coloré dans la console
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    // Flux qui sera utilisé pour écrire les stack tarce dans des fichiers afin de garder la console le plus propre possible
    protected static PrintStream ERROR_STREAM = null;

    @Override
    public abstract String toString();
}
