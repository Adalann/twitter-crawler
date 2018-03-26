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

public class CrawlerServer extends Thread
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
        conf = (ConfigurationCrawler)ConfigFactory.getConf(CONF_CODE);
        this.clients = new ArrayList<ConnectionCrawler>();
        try
        {
            this.server = new ServerSocket(conf.PORT);
            this.garbage = g;
            this.state = false;
        }
        catch(IOException e)
        {
            e.printStackTrace(System.err);
        }
    }

    /**
    *   Méthode run de la class Thread, écoute en boucle les demandes de connexions,
    *   ajoute les nouvelles instances de Connection dans la liste et les démarre
    */
    @Override
    public void run()
    {
        state = true;
        while(state && (conf.CLIENT_LIMIT == -1 || (conf.CLIENT_LIMIT != -1 && clients.size() < conf.CLIENT_LIMIT)))
        {
            try
            {
                ConnectionCrawler c = new ConnectionCrawler(server.accept(), garbage);
                clients.add(c);
                c.start();
            }
            catch(IOException e)
            {
                if(state)
                    e.printStackTrace(System.err);
            }
        }
        // close();
    }

    /**
    *   Ferme toutes les instances de Connection dans la liste et ferme le socket serveur
    */
    public void close()
    {
        try
        {
            for(ConnectionCrawler c : clients)
            {
                c.close();
            }
            if(!this.server.isClosed())
                this.server.close();
        }
        catch(IOException e)
        {
            e.printStackTrace(System.err);
        }
        finally
        {
            this.state = false;
        }
    }

    /**
    *   Affiche la liste de toutes les connexions au serveur #DEBUG_PURPOSE
    */
    public void listClients()
    {
        for(ConnectionCrawler c : clients)
        {
            System.out.println(c.toString());
        }
    }

}
