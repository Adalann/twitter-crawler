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
import common.*;

public class Indexor
{
    private static final int CONF_CODE = 1;
    private static int nbInstance = 0;
    private ConfigurationIndexor conf = (ConfigurationIndexor)ConfigFactory.getConf(CONF_CODE);
    private IndexorThread indexor;
    private Socket connectionCrawler;
    private Socket connectionAnalyser;
    //private "object qui gère le buffer"
    private boolean state;
    private boolean quiet;
    private Scanner sc;

    public Indexor(boolean quiet)
    {
        try
        {
            this.connectionCrawler = new Socket(conf.HOSTNAME_CRAWLER, conf.PORT_CRAWLER);
            System.out.println("Connection to the crawler established.");
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.out.println("Impossible to reach the crawler, check your connection and your configuration");
            this.connectionCrawler = null;
        }

        try
        {
            this.connectionAnalyser = new Socket(conf.HOSTNAME_ANALYSER, conf.PORT_ANALYSER);
            System.out.println("Connection to the analyser established.");
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.out.println("Impossible to reach the analyser, check your connection and your configuration");
            this.connectionAnalyser = null;
        }

        this.indexor = new IndexorThread(connectionCrawler, connectionAnalyser);
        this.state = false;
        this.sc = new Scanner(System.in);
        this.quiet = q;
    }

    public Indexor()
    {
        this.Indexor(false);
    }
}
//
// public class IndexorTemp
// {
//     private static final int CONF_CODE = 1;
//     private static ConfigurationIndexor conf = (ConfigurationIndexor)ConfigFactory.getConf(CONF_CODE);
//
//     public static void main(String[] args)
//     {
//         //Déclaration des variables
//         IndexorThread indexor = null;
//
//         boolean state = true;
//         Scanner sc = new Scanner(System.in);
//
//         try
//         {
//             //  Création du socket et du thread
//             Socket connection = new Socket(conf.HOSTNAME_CRAWLER, conf.PORT_CRAWLER);
//             indexor = new IndexorThread(connection);
//             indexor.start();
//             String query;
//             //  Boucle pour gérer l'interface console de l'indexeur
//             while(state && !connection.isClosed())
//             {
//                 System.out.print("> ");
//                 query = sc.nextLine();
//                 switch(query)
//                 {
//                     case "help":  //  Affiche la liste des commandes et leurs descriptions
//                     {
//                         System.out.println("help : show this help\nindex : show the number of tweets processed by this indexor\nstop : stop this indexor");
//                         break;
//                     }
//                     case "index":  //  Affiche le nombre de tweets traités
//                     {
//                         System.out.println(indexor.getIndex());
//                         break;
//                     }
//                     case "stop":  //  Stop le module
//                     {
//                         indexor.close();
//                         state = false;
//                         break;
//                     }
//                     default:
//                     {
//                         System.out.println("Type 'help' to print the available commands");
//                     }
//                 }
//             }
//             if(connection.isClosed() && state)
//                 System.out.println("Connection closed by the Crawler Server.");
//         }
//         catch(IOException e)
//         {
//             e.printStackTrace(System.err);
//         }
//         finally
//         {
//             indexor.close();
//             sc.close();
//         }
//     }
// }
