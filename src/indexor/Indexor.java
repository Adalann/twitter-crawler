/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe Principale des modules indexeurs
**/

package indexor;

import java.util.*;
import java.io.*;
import java.net.*;
import common.*;

public class Indexor extends Thread
{
    private static final int CONF_CODE = 1;
    private ConfigurationIndexor conf;
    private List<IndexorThread> indexorThreads;
    private boolean state;
    private Scanner sc;

    /**
    *   Constructeur de la classe, initialise les attributs et génère les Threads de traitement
    */
    public Indexor()
    {
        this.conf = (ConfigurationIndexor)ConfigFactory.getConf(CONF_CODE);
        this.indexorThreads = new ArrayList<IndexorThread>();
        for(int i = 0; i < conf.THREAD_NUMBER; i++)
            indexorThreads.add(new IndexorThread());
        this.state = false;
        this.sc = new Scanner(System.in);
    }

    /**
    *   Méthode run du la classe Thread, contient la boucle pour l'interface qui lit les commandes de l'utilisateur
    */
    public void run()
    {
        state = true;
        // On demarre les Threads crees
        for(IndexorThread thread : indexorThreads)
            thread.start();

        String query = "";
        while(state)
        {
            System.out.print("> ");
            query = sc.nextLine();
            switch(query)
            {
                case "help":  //  Affiche la liste des commandes et leurs descriptions
                {
                    System.out.println("help : show this help\nindex : show the number of tweets processed by this indexor\nstop : stop this indexorThread");
                    break;
                }
                case "index":  //  Affiche le nombre de tweets traités
                {
                    System.out.println(IndexorThread.getIndex());
                    break;
                }
                case "stop":  //  Stop le module
                {
                    shutdown();
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
    public void shutdown()
    {
        if(state)
        {
            for(IndexorThread thread : indexorThreads)
                thread.close();
            state = false;
        }
    }
}
