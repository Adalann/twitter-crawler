/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui gère le serveur pour les connexions avec les indexeurs
*   Initialise le serveur sur le port 2200
**/

package crawler;

import java.util.*;
import java.io.*;
import java.net.*;
import common.*;

class CrawlerServer extends Thread
{
    private static final int CONF_CODE = 0;
    private ServerSocket server;
    private List<ConnectionCrawler> clients;
    private Garbage garbage;
    private ConfigurationCrawler conf;
    private boolean state;

    /**
    *   Constructeur de la classe, créé un socket serveur sur le port 2200, initialise une liste de Connexion,
    *   récupère une instance de Garbage et met le boolean d'état à false
    *   @param g    une instance de Garbage (celle du crawler)
    */
    public CrawlerServer(Garbage g)
    {
        this.conf = (ConfigurationCrawler)ConfigFactory.getConf(CONF_CODE);
        this.clients = new ArrayList<ConnectionCrawler>();
        try
        {
            this.server = new ServerSocket(conf.PORT);
            this.garbage = g;
            this.state = false;
        }
        catch(BindException e)
        {
            System.out.println(conf.ANSI_RED + "Impossible to use the port " + conf.PORT + conf.ANSI_RESET);
            e.printStackTrace(conf.ERROR_STREAM());
        }
        catch(IOException e)
        {
            e.printStackTrace(conf.ERROR_STREAM());
        }
        setName("CrawlerServer");
    }

    /**
    *   Méthode run de la class Thread, écoute en boucle les demandes de connexions,
    *   ajoute les nouvelles instances de Connection dans la liste et les démarre
    */
    @Override
    public void run()
    {
        state = true;
        if(server != null) // Si le serveur a bien ete cree
        {
            // On boucle tant que la limite de client n'a pas ete atteinte, si elle a ete definie
            while(state && (conf.CLIENT_LIMIT == -1 || (conf.CLIENT_LIMIT != -1 && clients.size() < conf.CLIENT_LIMIT)))
            {
                try
                {
                    Socket s = server.accept();
                    ConnectionCrawler c = new ConnectionCrawler(s, garbage);
                    clients.add(c);
                    c.start();
                }
                catch(IOException e)
                {
                    // A tester
                    if(state)
                    System.out.println(conf.ANSI_RED + "An error occured, please check the last log file." + conf.ANSI_RESET);
                    e.printStackTrace(conf.ERROR_STREAM());
                }
            }
        }
        close();
    }

    /**
    *   Ferme toutes les instances de Connection dans la liste et ferme le socket serveur
    */
    public void close()
    {
        if(state)
        {
            try
            {
                for(ConnectionCrawler c : clients)
                {
                    if(c != null)
                        c.close();
                }
                if(!server.isClosed())
                    server.close();
            }
            catch(NullPointerException e)
            {
                // Si le serveur etait null
                e.printStackTrace(conf.ERROR_STREAM());
            }
            catch(IOException e)
            {
                System.out.println(conf.ANSI_RED + "An error occured, please check the last log file." + conf.ANSI_RESET);
                e.printStackTrace(conf.ERROR_STREAM());
            }
            finally
            {
                state = false;
            }
        }
    }

    /**
    *   Affiche la liste de toutes les connexions au serveur #DEBUG_PURPOSE
    */
    public void listClients()
    {
        for(ConnectionCrawler c : clients)
        {
            System.out.println(c);
        }
    }

}
