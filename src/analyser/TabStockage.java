package analyser;
import common.*;
import java.util.*;

class TabStockage
{

    private HashMap<String, String> Rt; // entrée ID tweet sortit String nombre de RT.
    private HashMap<String, List<Strings>> UrlExt; // entrée ID tweet sortit String url.
    private HashMap<String, List<String>> Hashtag; // entré hastag sortie arraylist des tweets.
    private HashMap<String, List<String>> IdToTweet; // entrée ID user en sortit arraylist de ses tweets.
    private HashMap<String, List<String>> IdToName;  // Entrée Id user sortit pseudo.
    //private HashMap<String, String> Media;

    public TabStockage()
    {
        Rt= new HashMap<String, String>();
        Media= new HashMap<String, String>();

        Hastag= new HashMap<String, List<String>>();
        UrlExt= new HashMap<String, List<String>>();

        IdToTweet= new HashMap<String, List<String>>();
        IdToName= new HashMap<String, List<String>>();
    }

    public void add(Tweet t)
    {
        // Ajout Retweet
        Rt.put(t.id_str,""+t.retweet_count);

        // IdToText
        if(IdToTweet.containsKey(t.user.id_str))
        {
            IdtoTweet.get(t.user.id_str).add(id_str);
        }
        else
        {
            List<String> l =new ArrayList<String>();
            l.add(id_str);
            IdtoTweet.put(t.user.id_str,l);
        }

        // IdToName
        if(IdToTweet.containsKey(t.user.id_str))
        {
            IdtoTweet.get(t.user.id_str).add(t.user.screen_name);
        }
        else
        {
            List<String> l =new ArrayList<String>();
            l.add(t.user.screen_name);
            IdtoTweet.put(t.user.id_str,l);
        }

        // hashtag
        Arraylist<Hashtag> lh = ArrayList<Hashtags>(Array.asList(t.entities.hashtags));
        Hashtag.put(t.id_str,lh);

        // UrlExt
        ArrayList<Url> lu= new ArrayList<Url>(Array.asList(t.entities.urls));
        UrlExt.put(t.id_str,lu);

    }

}
