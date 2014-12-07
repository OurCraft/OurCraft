package org.craft.modding;

import java.io.*;
import java.util.*;

public class Configuration
{

    private File       propertiesFile;
    private Properties properties;

    public Configuration(File file)
    {
        this.propertiesFile = file;
        properties = new Properties();
        try
        {
            if(propertiesFile.exists())
            {
                FileInputStream in = new FileInputStream(propertiesFile);
                properties.load(in);
                in.close();
            }
            else
                propertiesFile.createNewFile();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public File getPropertiesFile()
    {
        return propertiesFile;
    }

    public void save() throws IOException
    {
        FileOutputStream out = new FileOutputStream(propertiesFile);
        properties.store(out, "");
        out.flush();
        out.close();
    }

    public void setInt(String key, int value)
    {
        properties.setProperty(key, "" + value);
    }

    public void set(String key, String value)
    {
        properties.setProperty(key, value);
    }

    public void setFloat(String key, float value)
    {
        properties.setProperty(key, "" + value);
    }

    public void setDouble(String key, double value)
    {
        properties.setProperty(key, "" + value);
    }

    public void setBoolean(String key, boolean value)
    {
        properties.setProperty(key, "" + value);
    }

    public void setLong(String key, long value)
    {
        properties.setProperty(key, "" + value);
    }

    public void setByte(String key, byte value)
    {
        properties.setProperty(key, "" + value);
    }

    public void setShort(String key, short value)
    {
        properties.setProperty(key, "" + value);
    }

    public long getShort(String key)
    {
        return Short.parseShort(properties.getProperty(key));
    }

    public long getByte(String key)
    {
        return Byte.parseByte(properties.getProperty(key));
    }

    public long getLong(String key)
    {
        return Long.parseLong(properties.getProperty(key));
    }

    public int getInt(String key)
    {
        return Integer.parseInt(properties.getProperty(key));
    }

    public float getFloat(String key)
    {
        return Float.parseFloat(properties.getProperty(key));
    }

    public double getDouble(String key)
    {
        return Double.parseDouble(key);
    }

    public boolean getBoolean(String key)
    {
        return Boolean.parseBoolean(properties.getProperty(key));
    }

    public String get(String key)
    {
        return properties.getProperty(key);
    }
}
