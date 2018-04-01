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
import twitter4j.*;
import indexor.*;
import common.*;

public class Indexor extends Thread
{
    private static final int CONF_CODE = 1;
    private static int nbInstance = 0;
    private ConfigurationIndexor conf = (ConfigurationIndexor)ConfigFactory.getConf(CONF_CODE);
    private IndexorThread indexorThread;
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
            System.out.println(conf.ANSI_GREEN + "Connection to the crawler established." + conf.ANSI_RESET);
        }
        catch(IOException e)
        {
            e.printStackTrace(conf.ERROR_STREAM());
            System.out.println(conf.ANSI_RED + "Impossible to reach the crawler, check your connection and your configuration" + conf.ANSI_RESET);
            this.connectionCrawler = null;
        }

        try
        {
            this.connectionAnalyser = new Socket(conf.HOSTNAME_ANALYSER, conf.PORT_ANALYSER);
            System.out.println(conf.ANSI_GREEN + "Connection to the analyser established." + conf.ANSI_RESET);
        }
        catch(IOException e)
        {
            e.printStackTrace(conf.ERROR_STREAM());
            System.out.println(conf.ANSI_RED + "Impossible to reach the analyser, check your connection and your configuration" + conf.ANSI_RESET);
            this.connectionAnalyser = null;
        }

        this.indexorThread = new IndexorThread(connectionCrawler, connectionAnalyser);
        this.state = false;
        this.sc = new Scanner(System.in);
        this.quiet = quiet;
    }

    public Indexor()
    {
        this(false);
    }

    public void run()
    {
        state = true;
        indexorThread.start();
        String query = "";
        while(state)
        {
            if(connectionCrawler.isClosed())
            {
                System.out.println("Connection close by the host");
                state = false;
                break;
            }

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
                    System.out.println(indexorThread.getIndex());
                    break;
                }
                case "stop":  //  Stop le module
                {
                    stopIndexor();
                    break;
                }
                default:
                {
                    System.out.println("Type 'help' to print the available commands");
                }
            }
        }
    }

    public void stopIndexor()
    {
        try
        {
            if(!connectionCrawler.isClosed())
                connectionCrawler.close();
            if(!connectionAnalyser.isClosed())
                connectionAnalyser.close();
        }
        catch(IOException e)
        {
            System.out.println("Error trying to close the sockets.");
            e.printStackTrace(conf.ERROR_STREAM());
        }
        finally
        {
            state = false;
        }
    }
}
