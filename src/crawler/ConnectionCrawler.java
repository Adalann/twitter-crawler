/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui représente les connection avec les indexeurs et qui permet de gérer les flux associés.
*/

package crawler;

import java.io.*;
import java.net.*;
import common.*;

class ConnectionCrawler extends Connection
{
    private static final int CONF_CODE = 0;
    private BufferedReader in;
    private PrintWriter out;
    private Garbage tweets;
    private ConfigurationCrawler conf;

    private static int id = 0;

    /**
    *   Constructeur de la classe qui initialise le socket, les flux réseaux et le garbage.
    *   @param s    Le socket issu de la connexion avec le sokect serveur.
    *   @param g    Le gargabe qui stocke les tweets
    */
    public ConnectionCrawler(Socket s, Garbage g)
    {
        this.connection = s;
        this.tweets = g;
        this.conf = (ConfigurationCrawler)ConfigFactory.getConf(CONF_CODE);
        this.setName("Client-" + id++);
        try
        {
            // On initialise les flux grace au socket client
            this.in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(connection.getOutputStream())), true);
        }
        catch(IOException e)
        {
            System.out.println(conf.ANSI_RED + "An error occured, please check the last log file." + conf.ANSI_RESET);
            e.printStackTrace(conf.ERROR_STREAM());
        }
        this.state = false;
    }

    /**
    *   Le thread écoute en permance l'indexeur et lui envoie le prochain tweet à traiter.
    *   Stop le thread s'il reçoit STOP.
    */
    @Override
    public void run()
    {
        state = true;
        try
        {
            String query = "";
            while(state)
            {
                // On ecoute la requete de l'indexor
                query = in.readLine();
                // Si le resultat est null, un probleme de connexion entre le Crawler et l'Indexor est survenu,
                // on ferme alors la connexion
                if (query == null)
                {
                    System.out.println(conf.ANSI_RED + "Connection lost with " + this.getName() +  conf.ANSI_RESET);
                    close();
                    break;
                }
                switch(query)
                {
                    case "NEXT": // Demande du prochain tweet
                    {
                        // Appel de la méthode getNextElement de Garbage
                        out.println(tweets.getNextElement());
                        break;
                    }
                    case "STOP": // Demande de fin de la communication
                    {
                        System.out.println(conf.ANSI_BLUE + "Connection close by " + this.getName() + conf.ANSI_RESET);
                        close();
                        break;
                    }
                }
            }
        }
        catch(IOException e)
        {
            // L'erreur n'est affichee que si la connexion est ouverte car elle peut etre levee
            // a l'appel de la methode close a cause de la methode readLine
            if(state)
                System.out.println(conf.ANSI_RED + "Connection lost with " + this.getName() + conf.ANSI_RESET);
            e.printStackTrace(conf.ERROR_STREAM());
        }
        finally
        {
            // On s'assure de femer tous les flux
            close();
        }
    }

    /**
    *   Méthode pour fermer tous les flux et le socket pour mettre un terme à la connexion.
    */
    @Override
    public void close()
    {
        if(state)
        {
            state = false;
            try
            {
                in.close();
                out.close();
                // if(!connection.isClosed())
                    connection.close();
            }
            catch(IOException e)
            {
                System.out.println(conf.ANSI_RED + "Connection close by " + this.getName() + conf.ANSI_RESET);
                e.printStackTrace(conf.ERROR_STREAM());
            }
        }
    }

    /**
    *   Méthode toString, pour afficher les informations de l'instance de Connection.
    */
    @Override
    public String toString()
    {
        return this.getName() + " => " + connection.toString();
    }

}
