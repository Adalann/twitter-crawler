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

public class IndexorThread extends Thread
{
    private static final int CONF_CODE = 1;
    private String tweet;
    private Socket soc;
    private boolean state;
    private int index;

    private BufferedReader in;
    private PrintWriter out;

    /**
    *   Constructeur de la classe, initialise les flux à partir du socket passé en paramètre.
    *   @param s    Le socket de la connexions avec le serveur.
    */
    public IndexorThread(Socket s)
    {
        this.tweet = "";
        this.soc = s;
        this.state = false;
        this.index = 0;
        try
        {
            in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(soc.getOutputStream())), true);
        }
        catch(IOException e)
        {
            e.printStackTrace(System.err);
        }
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
            tweet = requestNextTweet();
            // try
            // {
                Gson gson = new GsonBuilder().create();
                Tweet t = gson.fromJson(tweet, Tweet.class);
                System.out.println(t);
            // }
            // catch(UnsupportedEncodingException e)
            // {
                // e.printStackTrace();
            // }
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
            out.println("NEXT");
            s = in.readLine();
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
            out.println("STOP");
            in.close();
            out.close();
            soc.close();
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
