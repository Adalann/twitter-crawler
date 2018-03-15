/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui implémente le crawler en lui même
*   Utilise l'API twitter4j en mode stream, hérite donc de la class StatusListener de l'API
**/

package server;

import java.util.*;
import twitter4j.*;

public class Crawler implements StatusListener
{
    private TwitterStream twitter;
    private Garbage tweets;
    private boolean state;

    /**
    *   Constructeur de la classe, créé une instance de Garbage et récupère l'instace Twitter de twitter4j
    *   Le boolean state mit à false indique que le crawler est à l'arrêt.
    */
    public Crawler()
    {
        this.tweets = new Garbage();
        this.twitter = new TwitterStreamFactory().getInstance();
        state = false;
    }

    /**
    *   Démarre le crawler avec le filtre passé en paramètre
    *   @param f   Le filtre à appliquer pour la récupération des tweets
    */
    public void start(String f)
    {
        this.twitter.addListener(this);
        this.twitter.filter(f);
        state = true;
    }

    /**
    *  Stop le crawler
    */
    public void stop()
    {
        this.twitter.shutdown();
        this.twitter.removeListener(this);
        state = false;
        System.out.println("Done, writing data (" + tweets.size() + " tweets)");
        tweets.save();
    }

    /**
    *   Appel la méthode size() du garbage et retourne le nombre de tweet déjà récupérés
    *   @return le nombre de tweets déjà récupérés
    */
    public int getGarbageSize()
    {
        return this.tweets.size();
    }

    /**
    *   Retourne l'instace de garbage du crawler
    *   @return l'instace du garbage
    */
    public Garbage getGarbage()
    {
        return this.tweets;
    }

    /**
    *   Retourne l'état du crawler, false pour arrêté et true pour un fonction
    *   @return l'état du crawler
    */
    public boolean getState()
    {
        return state;
    }

    /**
    *   Gestionnaire de récéption des tweets, appelée à chaque récéption
    *   @param status   le tweet reçu
    */
    @Override
    public void onStatus(Status status)
    {
        // System.out.println("@" + status.getUser().getName() + " : " + status.getText());
        synchronized(tweets)
        {
            this.tweets.addStatusElement(status);
        }
    }

    /**
    *   Méthodes héritées de StatusListener non utilisées ici
    */
    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}

    @Override
    public void onScrubGeo(long userId, long upToStatusId) {}

    @Override
    public void onStallWarning(StallWarning warning) {}

    @Override
    public void onException(Exception ex)
    {
        ex.printStackTrace();
    }
}
