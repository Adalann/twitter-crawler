/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe principale de la partie crawler du serveur
*   Lance les différents Threads et gère l'interface console pour l'utilisateur.
**/

package crawler;

import java.util.*;
import common.*;

public class Crawler extends Thread
{
    private static final int CONF_CODE = 0;
    private Garbage tweets;
    private CrawlerServer server;
    private TweetListener crawler;
    private Scanner sc;
    private ConfigurationCrawler conf;
    private boolean state;

    public Crawler()
    {
        this.tweets = new Garbage();
        this.server = new CrawlerServer(tweets);
        this.crawler = new TweetListener(tweets);
        this.sc = new Scanner(System.in);
        this.conf = (ConfigurationCrawler)ConfigFactory.getConf(CONF_CODE);
        this.state = false;
    }

    public void run()
    {
        state = true;

        // Démarrage du Thread crawler
        System.out.println("Starting the crawler...");
        crawler.start(conf.FILTER);
        System.out.println(conf.ANSI_GREEN + "Crawler started !" + conf.ANSI_RESET);

        // Temporisation pour une raison d'interface
        try
        {
            Thread.sleep(5000);
        }
        catch(InterruptedException e)
        {
            System.out.println(conf.ANSI_RED + "An error occured, please check the last log file." + conf.ANSI_RESET);
            e.printStackTrace(conf.ERROR_STREAM());
        }

        // Démarrage du serveur qui gère les connexions des modules indexeurs
        System.out.println("Starting the server...");
        server.start();
        System.out.println(conf.ANSI_GREEN + "Server started !" + conf.ANSI_RESET);

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
                case "showconf":
                {
                    System.out.println(conf);
                    break;
                }
                case "startc": // Démarre le crawler avec le filtre définit dans le fichier de configuration
                {
                    crawler.start(conf.FILTER);
                    try
                    {
                        Thread.sleep(5000);
                    }
                    catch(InterruptedException e)
                    {
                        System.out.println(conf.ANSI_RED + "An error occured, please check the last log file." + conf.ANSI_RESET);
                        e.printStackTrace(conf.ERROR_STREAM());
                    }
                    break;
                }
                case "stopc": // Stop le crawler
                {
                    crawler.stop();
                    break;
                }
                case "stop": // Stop l'application
                {
                    shutdown();
                    break;
                }
                case "size": // Affiche le nombre de tweets capturés
                {
                    System.out.println(crawler.getGarbageSize());
                    break;
                }
                case "help": // Affiche la liste des commandes et leurs descriptions
                {
                    System.out.println("listcl : list the connected clients\nhelp : show this help\nsave : save the tweets already captured in a output.data file\nshowconf : display the current configuration\nsize : print the number of tweets captured so far\nstartc : start the crawler\nstopc : stop the crawler\nstop : stop the application and save the data");
                    break;
                }
                default:
                {
                    System.out.println("Type 'help' to print the available commands");
                }
            }
        }
    }

    private void shutdown()
    {
        if(state)
        {
            tweets.save();
            crawler.stop();
            sc.close();
            server.close();
            state = false;
        }
    }
}
