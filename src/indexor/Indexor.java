/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe Principale des modules indexeurs
**/

import java.util.*;
import java.io.*;
import java.net.*;
import twitter4j.*;
import indexor.*;

public class Indexor
{
    private static final int CONF_CODE = 1;

    public static void main(String[] args)
    {
        //  Déclaration des variables
        IndexorThread indexor = null;

        boolean state = true;
        Scanner sc = new Scanner(System.in);

        if(args.length != 1)
        {
            System.out.println("Usage : java Indexor <address>");
        }
        else
        {
            try
            {
                //  Création du socket et du thread
                ADDRESS = args[0];
                Socket connection = new Socket(ADDRESS, PORT);
                indexor = new IndexorThread(connection);
                indexor.start();
                String query;

                //  Boucle pour gérer l'interface console de l'indexeur
                while(state && !connection.isClosed())
                {
                    System.out.print("> ");
                    query = sc.nextLine();

                    switch(query)
                    {
                        case "help":  //  Affiche la liste des commandes et leurs descriptions
                        {
                            System.out.println("help : show this help\nindex : show the number of tweets processed by this indexor\nstop : stop this indexor");
                            break;
                        }
                        case "index":  //  Affiche le nombre de tweets traités
                        {
                            System.out.println(indexor.getIndex());
                            break;
                        }
                        case "stop":  //  Stop le module
                        {
                            indexor.close();
                            state = false;
                            break;
                        }
                        default:
                        {
                            System.out.println("Type 'help' to print the available commands");
                        }
                    }
                }
                if(connection.isClosed() && state)
                    System.out.println("Connection closed by the Crawler Server.");
            }
            catch(IOException e)
            {
                e.printStackTrace(System.err);
            }
            finally
            {
                indexor.close();
                sc.close();
            }
        }
    }
}
