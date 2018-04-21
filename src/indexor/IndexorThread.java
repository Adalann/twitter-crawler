/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui gère la connxion avec le serveur et traite les JSONs reçus.
**/

package indexor;

import java.io.*;
import java.net.*;
import com.google.gson.*;
import common.*;

class IndexorThread extends Thread
{
    private static final int CONF_CODE = 1;
    private static int index = 0;
    private static int nbInstance = 1;
    private Socket socketCrawler;
    private ConfigurationIndexor conf;
    private BufferedReader inCrawler;
    private PrintWriter outCrawler;
    private Socket socketAnalyser;
    private ObjectOutputStream outAnalyser;
    private boolean state;

    // private String color;

    /**
    *   Constructeur de la classe, initialise les flux à partir du socket passé en paramètre.
    *   @param s    Le socket de la connexions avec le serveur.
    */
    public IndexorThread()
    {
        this.conf = (ConfigurationIndexor)ConfigFactory.getConf(CONF_CODE);
        try
        {
            this.socketCrawler = new Socket(conf.HOSTNAME_CRAWLER, conf.PORT_CRAWLER);
            System.out.println(conf.ANSI_GREEN + "[" + getName() + "] Connection to the crawler established." + conf.ANSI_RESET);
            inCrawler = new BufferedReader(new InputStreamReader(socketCrawler.getInputStream()));
            outCrawler = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketCrawler.getOutputStream())), true);
        }
        catch(IOException e)
        {
            e.printStackTrace(conf.ERROR_STREAM());
            System.out.println(conf.ANSI_RED + "[" + getName() + "] Impossible to reach the crawler, check your connection and your configuration" + conf.ANSI_RESET);
            this.socketCrawler = null;
        }

        try
        {
            this.socketAnalyser = new Socket(conf.HOSTNAME_ANALYSER, conf.PORT_ANALYSER);
            System.out.println(conf.ANSI_GREEN + "[" + getName() + "] Connection to the analyser established." + conf.ANSI_RESET);
            outAnalyser = new ObjectOutputStream(socketAnalyser.getOutputStream());
        }
        catch(IOException e)
        {
            e.printStackTrace(conf.ERROR_STREAM());
            System.out.println(conf.ANSI_RED + "[" + getName() + "] Impossible to reach the analyser, check your connection and your configuration" + conf.ANSI_RESET);
            this.socketAnalyser = null;
        }

        this.setName("IndexorThread-" + nbInstance++);
        this.state = false;
    }

    /**
    *   Méthode run de la classe Thread, traite le JSON reçu du serveur.
    */
    @Override
    public void run()
    {
        state = true;
        if(socketCrawler != null && socketAnalyser != null)
        {
            // System.out.println("\n" + conf.ANSI_BLUE + getName() + " started." + conf.ANSI_RESET);
            String tweetString = "";
            while(state)
            {
                synchronized(this) // Permet de s'assurer que le tweet en cours de traitement à bien été envoyé à
                {                  // l'analyser quand on stop le thread
                    tweetString = requestNextTweet();
                    if(tweetString == null)
                    {
                        System.out.println("\n" + conf.ANSI_RED + "[" + getName() + "] Connection to the crawler server lost, stoping the thread" + conf.ANSI_RESET);
                        close();
                    }
                    else if(tweetString.equals("STOP"))
                    {
                        System.out.println("\n" + conf.ANSI_BLUE + "Tweet limit reached, stoping the thread" + conf.ANSI_RESET);
                        close();
                    }
                    else if(!tweetString.equals(""))
                    {
                        Gson gson = new GsonBuilder().create();
                        Tweet tweet = gson.fromJson(tweetString, Tweet.class);
                        try
                        {
                            if(tweet != null)
                            {
                                outAnalyser.writeObject(tweet);
                                // System.out.println(getName() + color + "JOSN : " + tweetString + "\n" + conf.ANSI_RESET);
                            }
                        }
                        catch(IOException e)
                        {
                            e.printStackTrace(conf.ERROR_STREAM());
                            System.out.println("\n" + conf.ANSI_RED + "[" + getName() + "] Connection to the analyser server lost, closing the indexor, please press [ENTER]" + conf.ANSI_RESET);
                            close();
                        }
                    }
                }
            }
        }
    }

    /**
    *   Méthode qui envoie une requête au serveur pour le prochain tweet JSON à traiter
    *   @return     Le prochain tweet à traiter
    */
    public synchronized String requestNextTweet()
    {
        String s = "";
        try
        {
            outCrawler.println("NEXT");
            s = inCrawler.readLine();
            if(s == null)
                return null;
            else if(!s.equals(""))
                index++;
        }
        catch(IOException e)
        {
            System.out.println(conf.ANSI_RED + "An error occured, please check the last log file." + conf.ANSI_RESET);
            e.printStackTrace(conf.ERROR_STREAM());
        }
        return s;
    }

    /**
    *   Méthode pour fermer tous les flux et le socket de la connexion.
    */
    public synchronized void close()
    {
        if(state)
        {
            try
            {
                outCrawler.println("STOP");
                inCrawler.close();
                outCrawler.close();
                if(socketCrawler.isConnected())
                    socketCrawler.close();

                outAnalyser.close();
                if(socketAnalyser.isConnected())
                    socketAnalyser.close();

            }
            catch(NullPointerException e)
            {
                // Est levée si le client n'était pas connecté aux serveurs
                e.printStackTrace(conf.ERROR_STREAM());
            }
            catch(IOException e)
            {
                System.out.println(conf.ANSI_RED + "An error occured, please check the last log file." + conf.ANSI_RESET);
                e.printStackTrace(conf.ERROR_STREAM());
            }
            finally
            {
                this.state = false;
                System.out.println(conf.ANSI_BLUE + getName() + " stoped." + conf.ANSI_RESET);
            }
        }
    }

    // Test de syncronisation
    public synchronized boolean state()
    {
        if(state)
            notify();
        return state;
    }

    public static int getIndex()
    {
        return index;
    }
}
