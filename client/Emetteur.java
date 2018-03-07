public class Emetteur
{
    private String name;
    private String screenName;
    private String id;

    public Emetteur(String n, String sN, String i)
    {
        this.name = n;
        this.screenName = sN;
        this.id = i;
    }

    public String getName()
    {
        return name;
    }

    public String getScrennName()
    {
        return screenName;
    }

    public String getId()
    {
        return id;
    }
}
