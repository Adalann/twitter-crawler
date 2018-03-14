/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui gère le serveur pour les connexions avec les indexeurs
*   Initialise le serveur sur le port 2200
**/

import java.util.*;
import java.io.*;
import java.net.*;

public class CrawlerServer extends Thread
{
    private static final int PORT = 2200;
    private ServerSocket server;
    private List<Connection> clients;
    private Garbage garbage;
    private boolean state;

    /**
    *   Constructeur de la classe, créé un socket serveur sur le port 2200, initialise une liste de Connexion,
    *   récupère une instance de Garbage et met le boolean d'état à false
    *   @param g    une instance de Garbage (celle du crawler)
    */
    public CrawlerServer(Garbage g)
    {
        try
        {
            this.server = new ServerSocket(PORT);
            this.clients = new ArrayList<Connection>();
            this.garbage = g;
            this.state = true;
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
        while(state)
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
        close();
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
