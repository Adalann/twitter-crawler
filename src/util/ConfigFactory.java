/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui contient les constantes du programme.
*/

package util;

import java.io.*;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;

public class ConfigFactory
{
    public static Configuration getConf()
    {
        Gson gson = new Gson();
        JsonReader reader = null;
        try
        {
            reader = new JsonReader(new FileReader("server/serverConf.json"));
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        Configuration conf = gson.fromJson(reader, Configuration.class);
        return conf;
    }
}
