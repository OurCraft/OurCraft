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

    public short getShort(String key, short defaultValue)
    {
        try
        {
            return Short.parseShort(get(key));
        }
        catch(NumberFormatException e)
        {
            return defaultValue;
        }
    }

    public byte getByte(String key, byte defaultValue)
    {
        try
        {
            return Byte.parseByte(get(key));
        }
        catch(NumberFormatException e)
        {
            return defaultValue;
        }
    }

    public long getLong(String key, long defaultValue)
    {
        try
        {
            return Long.parseLong(get(key));
        }
        catch(NumberFormatException e)
        {
            return defaultValue;
        }
    }

    public int getInt(String key, int defaultValue)
    {
        try
        {
            return Integer.parseInt(get(key));
        }
        catch(NumberFormatException e)
        {
            return defaultValue;
        }
    }

    public float getFloat(String key, float defaultValue)
    {
        try
        {
            return Float.parseFloat(get(key));
        }
        catch(NumberFormatException e)
        {
            return defaultValue;
        }
    }

    public double getDouble(String key, double defaultValue)
    {
        try
        {
            return Double.parseDouble(get(key));
        }
        catch(NumberFormatException e)
        {
            return defaultValue;
        }
    }

    public boolean getBoolean(String key, boolean defaultValue)
    {
        try
        {
            return Boolean.parseBoolean(get(key));
        }
        catch(Exception e)
        {
            return defaultValue;
        }
    }

    public String get(String key)
    {
        return get(key, "");
    }

    public String get(String key, String defaultValue)
    {
        if(!properties.containsKey(key))
            return defaultValue;
        return properties.getProperty(key);
    }
}
