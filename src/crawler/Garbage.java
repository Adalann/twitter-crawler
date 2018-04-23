/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui implémente le dépot de tweets au format JSON, récupérés par le crawler et récupérés par les indexeurs
*   pour les traiter
**/

package crawler;

import java.util.*;
import twitter4j.*;
import twitter4j.json.*;
import java.io.*;
import common.*;

class Garbage
{
    private static final int CONF_CODE = 0;
    // L'attribut de classe index indique quel est le numéro dans la liste du prochain JSON à traiter dans la liste tweet
    private static int index = 0;
    private List<String> tweets;
    private ConfigurationCrawler conf;

    /**
    *   Constructeur de la classe, instancie une ArrayList vide, récupère une instance de configuration et initie index à zero
    */
    public Garbage()
    {
        this.tweets = new ArrayList<String>();
        this.conf = (ConfigurationCrawler)ConfigFactory.getConf(CONF_CODE);
        if(conf.RESTORE_ON_START)
            restore();
    }

    /**
    *   Méthode pour ajouter une entrée de type String dans la liste.
    *   @param e L'élement à ajouter
    */
    public synchronized void addStringElement(String e)
    {
        tweets.add(e.replaceAll("\n", " "));
    }

    /**
    *   Méthode pour ajouter un entrée de type Status dans la liste de tweets.
    *   @param e L'élement à ajouter
    */
    public synchronized void addStatusElement(Status e)
    {
        tweets.add(DataObjectFactory.getRawJSON(e).replaceAll("\n", " "));
    }

    /**
    *   Méthode pour ajouter une liste de String dans la liste de tweets.
    *   @param c La collection à ajouter
    */
    public synchronized void addStringCollection(List<String> c)
    {
        tweets.addAll(c);
    }

    /**
    *   Méthode pour ajouter une liste de Status dans la liste de tweets.
    *   @param c La collection à ajouter
    */
    public synchronized void addStatusCollection(List<Status> c)
    {
        for(Status status : c)
            tweets.add(DataObjectFactory.getRawJSON(status).replaceAll("\n", " "));
    }

    /**
    *   Méthode pour récupérer un élément de la liste à l'indice i.
    *   @param i    L'indice de l'élément à récupérer.
    *   @return     Le JSON à la position i dans la liste de tweets.
    */
    public String getElementAt(int i)
    {
        if(i < tweets.size())
            return tweets.get(i);
        else
            return "";
    }

    /**
    *   Méthode utilisée par les indexeurs pour récupérer le prochain élément de la liste à traiter, indiqué par l'attribut index.
    *   @return     Le prochain élément à traiter ou un sigbal d'arrêt pour les indexeurs
    */
    public synchronized String getNextElement()
    {
        String r = "";
        if(conf.TWEET_LIMIT != -1 && index >= conf.TWEET_LIMIT)
            r = "STOP";
        else if(index < tweets.size())
            r = tweets.get(index++);
        return r;
    }

    /**
    *   Méthode pour récupérer la taille de la liste de tweets.
    *   @return     La taille de la liste.
    */
    public int size()
    {
        return tweets.size();
    }

    public int index()
    {
        return index;
    }

    /**
    *   Méthode pour sauvegarder les tweets stockés dans le Garbage dans un fichier
    */
    public synchronized void save()
    {
        PrintWriter out = null;
        try
        {
            System.out.print("Writing data (" + tweets.size() + " tweets)... ");
            // Mise en place du flux pour l'ecriture
            out = new PrintWriter(new BufferedWriter(new FileWriter("../" + conf.SAVEFILE_NAME)));
            // On ecrit le contenue de la liste dans le fichier
            for(String s : tweets)
                out.println(s + "\n");
            System.out.println(conf.ANSI_GREEN + "OK" + conf.ANSI_RESET);
        }
        catch(IOException e)
        {
            System.out.println(conf.ANSI_RED + "An error occured, please check the last log file." + conf.ANSI_RESET);
            e.printStackTrace(conf.ERROR_STREAM());
        }
        finally
        {
            // Si tout c'est bien passe on ferme le flux
            if(out != null)
                out.close();
        }
    }

    /**
    *   Méthode pour restaurer les données
    */
    public synchronized void restore()
    {
        System.out.println("Restoration from " + conf.RESTOREFILE_NAME + "...");
        BufferedReader in = null;
        try
        {
            // On met en place le flux pour la lecture
            in = new BufferedReader(new FileReader("../" + conf.RESTOREFILE_NAME));
            String line = "";
            // On lit ligne par ligne
            while((line = in.readLine()) != null)
            {
                // Si la ligne contient bien du JSON, on ajoute la ligne
                if(line.startsWith("{"))
                    this.addStringElement(line);
            }
            System.out.println(conf.ANSI_GREEN + "Restored " + tweets.size() + " element(s) with success." + conf.ANSI_RESET);
        }
        catch(FileNotFoundException e)
        {
            System.out.println(conf.ANSI_RED + "\nThe restore file " + conf.RESTOREFILE_NAME + " was not found.\nRestoration failed !\n" + conf.ANSI_RESET);
            e.printStackTrace(conf.ERROR_STREAM());
        }
        catch(IOException e)
        {
            System.out.println(conf.ANSI_RED + "\nAn error occured while restoration, please take a look at the logs.\nRestoration failed !\n" + conf.ANSI_RESET);
            e.printStackTrace(conf.ERROR_STREAM());
        }
        finally
        {
            if(in != null)
            {
                try
                {
                    in.close();
                }
                catch(IOException e)
                {
                    System.out.println(conf.ANSI_RED + "\nAn error occured while restoration, please take a look at the logs.\nRestoration failed !\n" + conf.ANSI_RESET);
                    e.printStackTrace(conf.ERROR_STREAM());
                }
            }
        }
    }
}
