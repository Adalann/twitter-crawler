/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui implémente le dépot de tweets au format JSON, récupérés par le crawler et récupérés par les indexeurs
*   pour les traiter
**/

package server;

import java.util.*;
import twitter4j.*;
import twitter4j.json.*;
import java.io.*;

public class Garbage
{
    private List<String> tweets;
    private int index;  // L'attribut index indique quel est le numéro dans la liste du prochain JSON dans la liste tweet

    /**
    *   Constructeur de la classe, instancie une ArrayList vide et initie index à zero
    */
    public Garbage()
    {
        this.tweets = new ArrayList<String>();
        this.index = 0;
    }

    /**
    *   Méthode pour ajouter une entrée de type String dans la liste.
    */
    public synchronized void addStringElement(String e)
    {
        tweets.add(e.replaceAll("\n", " "));
    }

    /**
    *   Méthode pour ajouter un entrée de type Status dans la liste de tweets.
    */
    public synchronized void addStatusElement(Status e)
    {
        tweets.add(DataObjectFactory.getRawJSON(e).replaceAll("\n", " "));
    }

    /**
    *   Méthode pour ajouter une liste de String dans la liste de tweets.
    */
    public synchronized void addStringCollection(List<String> c)
    {
        tweets.addAll(c);
    }

    /**
    *   Méthode pour ajouter une liste de Status dans la liste de tweets.
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
    *   Méthode pour récupérer le dernier élément de la liste.
    *   @return     Le dernier élément de la liste de tweets.
    */
    public String getLastElement()
    {
        return getElementAt(tweets.size() - 1);
    }

    /**
    *   Méthode utilisée par les indexeurs pour récupérer le prochain élément de la liste à traiter, indiqué par l'attribut index.
    *   @return     Le prochain élément à traiter
    */
    public synchronized String getNextElement()
    {
        String r = "";
        if(index < tweets.size())
            r = tweets.get(index++);
        return r;
    }

    /**
    *   Méthode pour récupérer la taille de la liste de tweets.
    *   @return     La taille de la liste.
    */
    public synchronized int size()
    {
        return tweets.size();
    }

    /**
    *   Méthode pour sauvegarder les tweets stockés dans le Garbage dans un fichier output.data
    */
    public synchronized void save()
    {
        PrintWriter out = null;
        try
        {
            System.out.print("Writing data (" + tweets.size() + " tweets)... ");
            out = new PrintWriter(new BufferedWriter(new FileWriter("../output.data")));
            for(String s : tweets)
                out.println(s + "\n");
            System.out.println("OK");

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
