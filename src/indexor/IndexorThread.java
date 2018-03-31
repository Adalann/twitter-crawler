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
    private String tweetString;
    private Socket socketCrawler;
    private Socket socketAnalyser;
    private boolean state;
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
        this.tweetString = "";
        this.socketCrawler = sC;
        try
        {
            inCrawler = new BufferedReader(new InputStreamReader(socketCrawler.getInputStream()));
            outCrawler = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketCrawler.getOutputStream())), true);
        }
        catch(IOException e)
        {
            e.printStackTrace(System.err);
        }
        this.socketAnalyser = sA;
        // try
        // {
        //     outAnalyser = new ObjectOutputStream(socketAnalyser.getOutputStream());
        // }
        // catch(IOException e)
        // {
        //     e.printStackTrace(System.err);
        // }
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
        while(state)
        {
            tweetString = requestNextTweet();

            Gson gson = new GsonBuilder().create();
            Tweet tweet = gson.fromJson(tweetString, Tweet.class);
            System.out.println("JOSN : " + tweetString);
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
            if(!s.equals(""))
                index++;
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return s;
    }

    /**
    *   Méthode pour fermer tous les flux et le socket de la connexion.
    */
    public synchronized void close()
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
            e.printStackTrace(System.err);
        }
        finally
        {
            this.state = false;
        }
    }

    public int getIndex()
    {
        return index;
    }
}
