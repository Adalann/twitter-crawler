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

import java.util.concurrent.locks.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.*;

class IndexorThread extends Thread
{
    private static final int CONF_CODE = 1;
    private static AtomicInteger atomicIndex = new AtomicInteger();
    private static int nbInstance = 1;
    private Socket socketCrawler;
    private ConfigurationIndexor conf;
    private BufferedReader inCrawler;
    private PrintWriter outCrawler;
    private Socket socketAnalyser;
    private ObjectOutputStream outAnalyser;
    private Semaphore sema;
    private boolean state;

    /**
    *   Constructeur de la classe, initialise les flux à partir du socket passé en paramètre.
    */
    public IndexorThread()
    {
        this.conf = (ConfigurationIndexor)ConfigFactory.getConf(CONF_CODE);
        try
        {
            // On tente de se connecter au Crawler
            this.socketCrawler = new Socket(conf.HOSTNAME_CRAWLER, conf.PORT_CRAWLER);
            System.out.println(conf.ANSI_GREEN + "[" + getName() + "] Connection to the crawler established." + conf.ANSI_RESET);
            // On peut alors initialiser les flux grace au socket
            inCrawler = new BufferedReader(new InputStreamReader(socketCrawler.getInputStream()));
            outCrawler = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketCrawler.getOutputStream())), true);
        }
        catch(IOException e)
        {
            // Il n'a pas ete possible se connecter au Crawler
            e.printStackTrace(conf.ERROR_STREAM());
            System.out.println(conf.ANSI_RED + "[" + getName() + "] Impossible to reach the crawler, check your connection and your configuration" + conf.ANSI_RESET);
            this.socketCrawler = null;
        }

        try
        {
            // On tente de se connecter a l'Analyser
            this.socketAnalyser = new Socket(conf.HOSTNAME_ANALYSER, conf.PORT_ANALYSER);
            System.out.println(conf.ANSI_GREEN + "[" + getName() + "] Connection to the analyser established." + conf.ANSI_RESET);
            // On peut alors initialiser le flux ObjectOutputStream
            outAnalyser = new ObjectOutputStream(socketAnalyser.getOutputStream());
        }
        catch(IOException e)
        {
            // Il n'a pas ete possible se connecter au Crawler
            e.printStackTrace(conf.ERROR_STREAM());
            System.out.println(conf.ANSI_RED + "[" + getName() + "] Impossible to reach the analyser, check your connection and your configuration" + conf.ANSI_RESET);
            this.socketAnalyser = null;
        }

        this.sema = new Semaphore(1);
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
        // Il faut que les deux connexions soient etablies pour que l'indexeur demarre
        if(socketCrawler != null && socketAnalyser != null)
        {
            String tweetString = "";
            Gson gson = new GsonBuilder().create();
            while(state)
            {
                tweetString = requestNextTweet(); // On demande le prochain JSON
                if(tweetString == null)
                {
                    // Si le résultat est null, le crawler est injoignable, on ferme le Thread
                    System.out.println(conf.ANSI_RED + "[" + getName() + "] Connection to the crawler server lost, stoping the thread" + conf.ANSI_RESET);
                    close();
                }
                else if(tweetString.equals("STOP"))
                {
                    // On a atteint la limite de tweet, le Thread peut s'arreter
                    System.out.println(conf.ANSI_BLUE + "Tweet limit reached, stoping the thread" + conf.ANSI_RESET);
                    close();
                }
                else if(!tweetString.equals("")) // Une chaine JSON a bien ete recue
                {
                    // Permet de s'assurer que le tweet en cours de traitement a bien
                    // été envoyé a l'analyser quand on stop le thread
                    try
                    {
                        // On s'assure que
                        sema.acquire();

                        // On genere l'objet tweet
                        Tweet tweet = gson.fromJson(tweetString, Tweet.class);
                        if(tweet != null && !socketAnalyser.isClosed()) // On s'assure que l'object n'est pas null avant l'envoi
                        {
                            // On ecrit l'objet sur le flux
                            outAnalyser.writeObject(tweet);
                        }
                    }
                    catch(InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    catch(IOException e)
                    {
                        System.out.println(conf.ANSI_RED + "[" + getName() + "] Connection to the analyser server lost, closing the indexor, please press [ENTER]" + conf.ANSI_RESET);
                        e.printStackTrace(conf.ERROR_STREAM());
                        close();
                    }
                    catch(JsonSyntaxException e)
                    {
                        System.out.println(conf.ANSI_YELLOW + "Recieved a malformed JSON" + conf.ANSI_RESET);
                        e.printStackTrace(conf.ERROR_STREAM());
                    }
                    finally
                    {
                        sema.release();
                    }
                }
            }
        }
        // On s'assure que les flux sont bien fermes
        close();
    }
    /**
    *   Méthode qui envoie une requête au serveur pour le prochain tweet JSON à traiter
    *   @return     Le prochain tweet à traiter
    */
    public String requestNextTweet()
    {
        String s = "";
        try
        {
            sema.acquire();
            if(!socketCrawler.isClosed())
            {
                outCrawler.println("NEXT");
                s = inCrawler.readLine();
                if(s == null)
                    return null;
                else if(s.startsWith("{")) // Une chaine JSON a bien ete recue
                    atomicIndex.getAndIncrement();
            }
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            System.out.println(conf.ANSI_RED + "An error occured, please check the last log file." + conf.ANSI_RESET);
            e.printStackTrace(conf.ERROR_STREAM());
        }
        finally
        {
            sema.release();
        }

        return s;
    }

    /**
    *   Méthode pour fermer tous les flux et le socket de la connexion.
    */
    public void close()
    {
        if(state)
        {
            try
            {
                sema.acquire();

                outCrawler.println("STOP");
                inCrawler.close();
                outCrawler.close();
                socketCrawler.close();

                outAnalyser.close();
                socketAnalyser.close();
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
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
                sema.release();
            }
        }
    }

    /**
    *   Méthode qui renvoie le nombre de JSON traités par le Thread
    *   @return     Le nombre de JSON traités par le Thread
    */
    public static int getIndex()
    {
        return atomicIndex.get();
    }
}
