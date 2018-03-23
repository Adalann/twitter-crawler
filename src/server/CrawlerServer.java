/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui gère le serveur pour les connexions avec les indexeurs
*   Initialise le serveur sur le port 2200
**/

package server;

import java.util.*;
import java.io.*;
import java.net.*;
import util.*;

public class CrawlerServer extends Thread
{
    private ServerSocket server;
    private List<Connection> clients;
    private Garbage garbage;
    private boolean state;
    private Configuration conf;

    /**
    *   Constructeur de la classe, créé un socket serveur sur le port 2200, initialise une liste de Connexion,
    *   récupère une instance de Garbage et met le boolean d'état à false
    *   @param g    une instance de Garbage (celle du crawler)
    */
    public CrawlerServer(Garbage g)
    {
        conf = ConfigFactory.getConf();
        this.clients = new ArrayList<Connection>();
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
        while(state && (conf.CLIENT_LIMIT != -1 && clients.size() < conf.CLIENT_LIMIT))
        {
            try
            {
                Connection c = new Connection(server.accept(), garbage);
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
            for(Connection c : clients)
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
    *   Affiche la liste de toutes les connexions au serveur
    */
    public void listClients()
    {
        for(Connection c : clients)
        {
            System.out.println(c.toString());
        }
    }

}
