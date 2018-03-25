/**String
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui contient les constantes du programme.
*/

package common;

import java.io.*;
import java.util.*;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;

public class ConfigFactory
{
    private static String[] CONF_PATH_TABLE = {"crawler/crawlerConf.json", "indexor/indexorConf.json", "analyser/analyserConf.json"};

    public static Configuration getConf(int conf_code)
    {
        Gson gson = new Gson();
        JsonReader reader = null;
        try
        {
            reader = new JsonReader(new FileReader(CONF_PATH_TABLE[conf_code]));
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        Configuration conf = gson.fromJson(reader, Configuration.class);
        return conf;
    }
}
