/**
*   @author Theo Martos
*   @author Jules Perret
**/

import java.util.*;
import twitter4j.*;

public class Crawler implements StatusListener
{
    private TwitterStream twitter;
    private Garbage tweets;
    private boolean state;

    public Crawler()
    {
        this.tweets = new Garbage();
        this.twitter = new TwitterStreamFactory().getInstance();
        state = false;
    }

    public Crawler(Garbage g)
    {
        this.tweets = g;
        this.twitter = new TwitterStreamFactory().getInstance();
        state = false;
    }

    public void start(String f)
    {
        this.twitter.addListener(this);
        this.twitter.filter(f);
        state = true;
    }

    public void stop()
    {
        this.twitter.shutdown();
        this.twitter.removeListener(this);
        state = false;
        System.out.println("Done, writing data (" + tweets.size() + " tweets)");
        tweets.save();
    }

    public int getGarbageSize()
    {
        return this.tweets.size();
    }

    public Garbage getGarbage()
    {
        return this.tweets;
    }

    public boolean getState()
    {
        return state;
    }

    public void onStatus(Status status)
    {
        // System.out.println("@" + status.getUser().getName() + " : " + status.getText());
        synchronized(tweets)
        {
            this.tweets.addStatusElement(status);
        }
    }

    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}

    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}

    public void onScrubGeo(long userId, long upToStatusId) {}

    public void onStallWarning(StallWarning warning) {}

    public void onException(Exception ex)
    {
        ex.printStackTrace();
    }
}
