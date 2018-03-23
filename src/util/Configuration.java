/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui contient les constantes du programme.
*/

package util;

public class Configuration
{
    public int PORT = 0;
    public int CLIENT_LIMIT = 0;
    public String FILTER = "";
    public int TWEET_LIMIT = 0;
    public String SAVEFILE_NAME = "";

    @Override
    public String toString()
    {
        return "Port : " + PORT + " Client limit : " + CLIENT_LIMIT + " Filter : " + FILTER + " Tweet limit : " + TWEET_LIMIT + " FileName : " + SAVEFILE_NAME;
    }
}
