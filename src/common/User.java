/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui contient les informations des Users dans les Tweets traités
**/

package common;

public class User
{
    public final String id_str;
    public final String lang;
    public final boolean verified;
    public final int followers_count;
    public final String screen_name;
    public final String name;

    public User()
    {
        this.id_str = "";
        this.lang = "";
        this.verified = false;
        this.followers_count = 0;
        this.screen_name = "";
        this.name = "";
    }

}
