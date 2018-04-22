/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe Principale du module d'analyse
*   Gère l'interface console et exécute les actions
**/

package analyser;

import common.*;
import java.io.*;
import java.util.*;

public class Analyser extends Thread
{
    private static final int CONF_CODE = 2;
    private ConfigurationAnalyser conf;
    private AnalyserServer server;
    private DataContainer dataContainer;
    private HITS hits;
    private Scanner sc;
    private boolean state;

    /**
    *   Constructeur de la classe, initialise les attributs.
    */
    public Analyser()
    {
        this.conf = (ConfigurationAnalyser)ConfigFactory.getConf(CONF_CODE);
        this.dataContainer = new DataContainer();
        this.server = new AnalyserServer(dataContainer);
        this.hits = new HITS(dataContainer);
        this.sc = new Scanner(System.in);
        this.state = false;
    }

    /**
    *   Méthode run du la classe Thread, contient la boucle pour l'interface qui lit les commandes de l'utilisateur
    */
    @Override
    public void run()
    {
        state = true;
        server.start();

        try
        {
            Thread.sleep(5); // Temporisation pour raison d'affichage
        }
        catch(InterruptedException e)
        {
            e.printStackTrace(conf.ERROR_STREAM());
        }

        while(state)
        {
            System.out.print("> ");

            String q = sc.nextLine().toLowerCase();
            switch(q)
            {
                case "help":
                {
                    System.out.println("authresults : generate auth-sorted results\n" +
                                       "hubresults : generate hub-sorted results\n" +
                                       "help : show this help\n" +
                                       "savedata : save dataContainer\n" +
                                       "start : launch HITS\n" +
                                       "stop : close this application");
                    break;
                }
                case "authresults":
                {
                    hits.authSortedResults();
                    break;
                }
                case "hubresults":
                {
                    hits.hubSortedResults();
                    break;
                }
                case "savedata":
                {
                    dataContainer.save();
                    break;
                }
                case "start":
                {
                    hits.start();
                    System.out.println(conf.ANSI_BLUE + "HITS started" + conf.ANSI_RESET);
                    break;
                }
                case "stop":
                {
                    shutdow();
                    break;
                }
                default:
                {
                    System.out.println("Type 'help' to print the available commands");
                }
            }
        }
    }

    /**
    *   Stop tous les Threads
    */
    public void shutdow()
    {
        server.close();
        sc.close();
        state = false;
    }

}
