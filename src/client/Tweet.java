public class Tweet
{
    private String id_str;
    private String text;
    private String created_at;
    private Retweet retweeted_status;


    public String toString()
        {
            return "\n ID du Tweet: "+id_str+"text: "+text+" \n \n RT:"+retweeted_status.toString();
        }


}
