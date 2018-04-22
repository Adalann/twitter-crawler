/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui encapsule les Hastables
**/

package analyser;

import common.*;
import java.util.*;
import java.io.*;

class DataContainer implements Serializable
{
    private static final int CONF_CODE = 2;
    private static final long serialVersionUID = 50000l;
    //ID du tweet en cle
    private Map<String, String> retweetCounts;
    private Map<String, List<Url>> urls;
    private Map<String, List<Hashtag>> hashtags;
    //ID du compte en cle
    private Map<String, String> idToNames;
    private Map<String, List<String>> idToTweets;
    private Map<String, List<String>> incomingNeighbors;
    private Map<String, List<String>> outcomingNeighbors;

    public DataContainer()
    {
        ConfigurationAnalyser conf = (ConfigurationAnalyser)ConfigFactory.getConf(CONF_CODE);
        if(conf.RESTORE_ON_START)
        {
            restore();
        }
        else
        {
            this.retweetCounts = new HashMap<String, String>();
            this.urls = new HashMap<String, List<Url>>();
            this.hashtags = new HashMap<String, List<Hashtag>>();
            this.idToNames = new HashMap<String, String>();
            this.idToTweets = new HashMap<String, List<String>>();

            this.incomingNeighbors = new HashMap<String, List<String>>();
            this.outcomingNeighbors = new HashMap<String, List<String>>();
        }
    }

    public synchronized void add(Tweet t)
    {
        // Si le tweet contient un retweet
        if(t.retweeted_status != null)
        {
            Retweet rt = t.retweeted_status;
            // Ajout du nombre de retweet
            retweetCounts.put(rt.id_str, "" + rt.retweet_count);
            // Ajout des liens
            urls.put(rt.id_str, new ArrayList<Url>(Arrays.asList(rt.entities.urls)));
            // Ajout des hashtags
            hashtags.put(rt.id_str, new ArrayList<Hashtag>(Arrays.asList(rt.entities.hashtags)));
            // Ajout du pseudo s'il est inconnu
            if(!idToNames.containsKey(rt.user.id_str))
                idToNames.put(rt.user.id_str, rt.user.screen_name);
            // Ajout du tweet dans la liste des tweets de l'utilisateur
            List<String> listTweet = idToTweets.get(rt.user.id_str);
            if(listTweet != null && listTweet.indexOf(rt.id_str) == -1)
            {
                listTweet.add(rt.id_str);
            }
        }

        // De même si le tweet contient un tweet cite
        if(t.quoted_status != null)
        {
            QuotedTweet qt = t.quoted_status;
            retweetCounts.put(qt.id_str, "" + qt.retweet_count);

            urls.put(qt.id_str, new ArrayList<Url>(Arrays.asList(qt.entities.urls)));

            hashtags.put(qt.id_str, new ArrayList<Hashtag>(Arrays.asList(qt.entities.hashtags)));

            if(!idToNames.containsKey(qt.user.id_str))
            idToNames.put(qt.user.id_str, qt.user.screen_name);

            List<String> listTweet = idToTweets.get(qt.user.id_str);
            if(listTweet != null && listTweet.indexOf(qt.id_str) == -1)
            {
                listTweet.add(qt.id_str);
            }
        }

        // Ajout du nombre de retweet
        retweetCounts.put(t.id_str, "" + t.retweet_count);

        if(t.retweeted_status != null) // Un lien entre deux users existe
        {
            // incomingNeighbors
            // On ajoute le tweet aux incomingNeighbors de l'utilisateur
            if(incomingNeighbors.containsKey(t.retweeted_status.user.id_str))
                incomingNeighbors.get(t.retweeted_status.user.id_str).add(t.user.id_str);
            else
            {
                // On cree une entree dans la Hashtable si c'est le premier
                List<String> l = new ArrayList<String>();
                l.add(t.user.id_str);
                incomingNeighbors.put(t.retweeted_status.user.id_str, l);
            }

            // outcomingNeighbors
            // On ajoute le tweet aux outcomingNeighbors de l'utilisateur
            if(outcomingNeighbors.containsKey(t.user.id_str))
                outcomingNeighbors.get(t.user.id_str).add(t.retweeted_status.user.id_str);
            else
            {
                // On cree une entree dans la Hashtable si c'est le premier
                List<String> l = new ArrayList<String>();
                l.add(t.retweeted_status.user.id_str);
                outcomingNeighbors.put(t.user.id_str, l);
            }
        }

        // On ajoute les urls
        urls.put(t.id_str, new ArrayList<Url>(Arrays.asList(t.entities.urls)));

        // On ajoute les hashtags
        hashtags.put(t.id_str, new ArrayList<Hashtag>(Arrays.asList(t.entities.hashtags)));

        // On ajoute le pseudo de l'utilisateur
        if(!idToNames.containsKey(t.user.id_str))
            idToNames.put(t.user.id_str, t.user.screen_name);

        // On ajoute ce tweet a la liste de tweet de l'utilisateur
        if(idToTweets.containsKey(t.user.id_str))
            idToTweets.get(t.user.id_str).add(t.id_str);
        else
        {
            // On cree une entree dans la Hashtable si c'est le premier
            List<String> l = new ArrayList<String>();
            l.add(t.id_str);
            idToTweets.put(t.user.id_str, l);
        }
    }

    public int getRTCounts(String id)
    {
        return Integer.parseInt(retweetCounts.get(id));
    }

    public List<String> getIncomingNeighbors(String id)
    {
        return incomingNeighbors.get(id);
    }

    public List<String> getOutcomingNeighbors(String id)
    {
        return outcomingNeighbors.get(id);
    }

    public List<Url> getUrls(String id)
    {
        return urls.get(id);
    }

    public List<Hashtag> hashtags(String hashtag)
    {
        return hashtags.get(hashtag);
    }

    public String getName(String id)
    {
        return idToNames.get(id);
    }

    public List<String> getTweets(String id)
    {
        return idToTweets.get(id);
    }

    public Set<String> getIDS()
    {
        return idToNames.keySet();
    }

    public synchronized void save()
    {
        ConfigurationAnalyser conf = (ConfigurationAnalyser)ConfigFactory.getConf(CONF_CODE);
        try
        {
            // On cree le flux pour serialiser
            ObjectOutputStream saveStream = new ObjectOutputStream(new FileOutputStream("../" + conf.SAVEFILE_NAME));
            // On ecrit l'objet
            saveStream.writeObject(this);
            System.out.println(conf.ANSI_GREEN + "Successfully saved dataContainer !" + conf.ANSI_RESET);
        }
        catch(IOException e)
        {
            System.out.println(conf.ANSI_RED + "An error occured, please check the last log file." + conf.ANSI_RESET);
            e.printStackTrace(conf.ERROR_STREAM());
        }
    }

    public synchronized void restore()
    {
        ConfigurationAnalyser conf = (ConfigurationAnalyser)ConfigFactory.getConf(CONF_CODE);
        System.out.println("Restoration from " + conf.RESTOREFILE_NAME + "...");
        try
        {
            // On cree le flux pour lire le fichier de sauvegarde
            ObjectInputStream restoreStream = new ObjectInputStream(new FileInputStream("../" + conf.RESTOREFILE_NAME));
            // On cree le nouvel object
            DataContainer restoredObject = (DataContainer)restoreStream.readObject();
            // On restaure les Hashtables depuis restoredObject
            if(restoredObject != null)
            {
                this.retweetCounts = new HashMap<String, String>(restoredObject.retweetCounts);
                this.urls = new HashMap<String, List<Url>>(restoredObject.urls);
                this.hashtags = new HashMap<String, List<Hashtag>>(restoredObject.hashtags);
                this.idToNames = new HashMap<String, String>(restoredObject.idToNames);
                this.idToTweets = new HashMap<String, List<String>>(restoredObject.idToTweets);
                this.incomingNeighbors = new HashMap<String, List<String>>(restoredObject.incomingNeighbors);
                this.outcomingNeighbors = new HashMap<String, List<String>>(restoredObject.outcomingNeighbors);
            }
            System.out.println(conf.ANSI_GREEN + "Restored  with success." + conf.ANSI_RESET);
        }
        catch(FileNotFoundException e)
        {
            System.out.println(conf.ANSI_RED + "\nThe restore file " + conf.RESTOREFILE_NAME + " was not found.\nRestoration failed !\n" + conf.ANSI_RESET);
            e.printStackTrace(conf.ERROR_STREAM());
        }
        catch(ClassNotFoundException e)
        {
            System.out.println(conf.ANSI_RED + "\nAn error occured while restoration, please take a look at the logs.\nRestoration failed !\n" + conf.ANSI_RESET);
            e.printStackTrace(conf.ERROR_STREAM());
        }
        catch(IOException e)
        {
            System.out.println(conf.ANSI_RED + "\nAn error occured while restoration, please take a look at the logs.\nRestoration failed !\n" + conf.ANSI_RESET);
            e.printStackTrace(conf.ERROR_STREAM());
        }
    }

    @Override
    public String toString()
    {
        return retweetCounts.toString() + "\n\n" + urls.toString() + "\n\n" + hashtags.toString() + "\n\n" + idToNames.toString() + "\n\n" + idToTweets.toString();
    }



}
