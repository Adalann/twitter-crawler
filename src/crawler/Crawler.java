/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe principale de la partie crawler du serveur
*   Lance les différents Threads et gère l'interface console pour l'utilisateur.
**/

import java.util.*;
import java.io.*;
import java.net.*;
import twitter4j.*;
import crawler.*;
import common.*;

public class Crawler
{
    private static final String CONF_CODE = "1000";
    public static void main(String[] args)
    {
        // Décalaration des variables et des Threads
        boolean state = true;
        TweetListener crawler = new TweetListener();
        Garbage tweets = crawler.getGarbage();
        CrawlerServer server = new CrawlerServer(tweets);
        Scanner sc = new Scanner(System.in);
        Configuration conf = ConfigFactory.getConf(CONF_CODE);

        // Démarrage du Thread crawler
        System.out.println("Starting the crawler...");
        crawler.start(conf.FILTER);
        System.out.println("Crawler started !");

        // Temporisation pour une raison d'interface
        try
        {
            Thread.sleep(5000);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace(System.err);
        }

        // Démarrage du serveur qui gère les connexions des modules indexeurs
        System.out.println("Starting the server...");
        server.start();
        System.out.println("Server started !");

        // Boucle pour l'interface console
        while(state)
        {
            System.out.print("> ");
            String query = sc.nextLine().toLowerCase();

            switch(query)
            {
                case "listcl": // Affiche la liste des clients
                {
                    server.listClients();
                    break;
                }
                case "save": // Sauvegarde les tweets déjà capturés
                {
                    tweets.save();
                    break;
                }
                case "startc": // Démarre le crawler avec le filtre définit plus haut
                {
                    crawler.start(conf.FILTER);
                    try
                    {
                        Thread.sleep(5000);
                    }
                    catch(InterruptedException e)
                    {
                        e.printStackTrace(System.err);
                    }
                    break;
                }
                case "stopc": // Stop le crawler
                {
                    crawler.stop();
                    break;
                }
                case "stop": // Stop l'application'
                {
                    state = false;
                    break;
                }
                case "size": // Affiche le nombre de tweets capturés
                {
                    System.out.println(crawler.getGarbageSize());
                    break;
                }
                case "help": // Affiche la liste des commandes et leurs descriptions
                {
                    System.out.println("help : show this help\nsave : save the tweets already captured in a output.data file\nsize : print the number of tweets captured so far\nstartc : start the crawler\nstopc : stop the crawler\nstop : stop the application and save the data");
                    break;
                }
                default:
                {
                    System.out.println("Type 'help' to print the available commands");
                }
            }
        }

        crawler.stop();
        sc.close();
        server.close();
        System.exit(0);
    }
}
