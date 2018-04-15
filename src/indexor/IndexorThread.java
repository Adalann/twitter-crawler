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
    private Socket socketCrawler;
    private Socket socketAnalyser;
    private boolean state;
    private ConfigurationIndexor conf;
    private int index;

    private BufferedReader inCrawler;
    private PrintWriter outCrawler;

    private ObjectOutputStream outAnalyser;

    /**
    *   Constructeur de la classe, initialise les flux à partir du socket passé en paramètre.
    *   @param s    Le socket de la connexions avec le serveur.
    */
    public IndexorThread(Socket sC, Socket sA)
    {
        this.conf = (ConfigurationIndexor)ConfigFactory.getConf(CONF_CODE);
        this.socketCrawler = sC;
        try
        {
            inCrawler = new BufferedReader(new InputStreamReader(socketCrawler.getInputStream()));
            outCrawler = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketCrawler.getOutputStream())), true);
        }
        catch(IOException e)
        {
            e.printStackTrace(conf.ERROR_STREAM());
        }
        this.socketAnalyser = sA;
        try
        {
            outAnalyser = new ObjectOutputStream(socketAnalyser.getOutputStream());
        }
        catch(IOException e)
        {
            e.printStackTrace(conf.ERROR_STREAM());
        }
        this.state = false;
        this.index = 0;
    }

    /**
    *   Méthode run de la classe Thread, traite le JSON reçu du serveur.
    */
    @Override
    public void run()
    {
        state = true;
        String tweetString = "";
        while(state)
        {
            tweetString = requestNextTweet();
            if(tweetString == null)
            {
                System.out.println(conf.ANSI_RED + "Connection to the crawler server lost, closing the indexor, please press [ENTER]" + conf.ANSI_RESET);
                close();
            }
            else if(tweetString.equals("STOP"))
            {
                System.out.println(conf.ANSI_BLUE + "Tweet limit reached, closing the indexor, please press [ENTER]" + conf.ANSI_RESET);
                close();
            }
            else
            {
                Gson gson = new GsonBuilder().create();
                Tweet tweet = gson.fromJson(tweetString, Tweet.class);
                try
                {
                    if(tweet != null && !tweetString.equals(""))
                    {
                        outAnalyser.writeObject(tweet);
                        // System.out.println("JOSN : " + tweetString + "\n");
                    }
                }
                catch(IOException e)
                {
                    System.out.println(conf.ANSI_RED + "An error occured, please check the last log file." + conf.ANSI_RESET);
                    e.printStackTrace(conf.ERROR_STREAM());
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
            {
                return null;
            }
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
                socketCrawler.close();
            }
            catch(IOException e)
            {
                System.out.println(conf.ANSI_RED + "An error occured, please check the last log file." + conf.ANSI_RESET);
                e.printStackTrace(conf.ERROR_STREAM());
            }
            finally
            {
                this.state = false;
            }
        }
    }

    public int getIndex()
    {
        return index;
    }
}
