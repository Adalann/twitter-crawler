import java.util.*;

public class Content
{
    private String text;
    private String tDate;
    private int retweet;
    private String quote;
    private List<String> hashtag;
    private String ref;
    private String idTweet;

    public Content(String t, String d, ArrayList<String> l, String id)
    {
        this.text = t;
        this.tDate = d;
        this.retweet = 0;
        this.quote = " ";
        this.hashtag = l;
        this.idTweet = id;

    }

    public Content(String t, String d, int r, ArrayList<String> l, String id)
    {
        this.text = t;
        this.tDate = d;
        this.retweet = r;
        this.quote = "";
        this.hashtag = l;
        this.idTweet = id;
    }

    public Content(String t, String d, int r, String q, ArrayList<String> l, String id)
    {
        this.text = t;
        this.tDate = d;
        this.retweet = r;
        this.quote = q;
        this.hashtag = l;
        this.idTweet = id;

    }



    public String getText()
    {
        return this.text;
    }

    public String getDate()
    {
        return this.tDate;
    }

    public int getRetweet()
    {
        return this.retweet;
    }

    public String getQuote()
    {
        return this.quote;
    }

    public List<String> getHashtag()
    {
        return hashtag;
    }


}
