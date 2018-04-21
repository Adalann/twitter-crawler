/**
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
    private static String[] CONF_PATH_TABLE = {"../crawlerConf.json", "../indexorConf.json", "../analyserConf.json"};
    private static Configuration confInstance = null; // Evite de recréer l'instance de configuration et assure que toutes les classes utilisent la même configuration

    public static Configuration getConf(int conf_code)
    {
        if(confInstance == null)
        {
            Gson gson = new Gson();
            JsonReader reader = null;
            try
            {
                reader = new JsonReader(new FileReader(CONF_PATH_TABLE[conf_code]));
            }
            catch(FileNotFoundException e)
            {
                System.out.println("Configuration file not found !");
                e.printStackTrace();
            }
            Configuration conf = null;
            switch(conf_code)
            {
                // La méthode fromJson de la librairie permet de créer une instance en passant en paramètre le JsonReader et la classe désirée
                case 0:
                {
                    conf = gson.fromJson(reader, ConfigurationCrawler.class);
                    break;
                }
                case 1:
                {
                    conf = gson.fromJson(reader, ConfigurationIndexor.class);
                    break;
                }
                case 2:
                {
                    conf = gson.fromJson(reader, ConfigurationAnalyser.class);
                    break;
                }
            }
            confInstance = conf;
        }
        return confInstance;
    }
}
