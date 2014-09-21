package org.craft.loader;

import java.io.*;
import java.net.*;
import java.util.*;

import org.craft.utils.*;

public class OurClassLoader extends URLClassLoader
{
    private List<URL> sources;

    public OurClassLoader(URL[] sources)
    {
        super(sources, null);
        this.sources = Arrays.asList(sources);
    }

    @Override
    public void addURL(final URL url)
    {
        super.addURL(url);
        sources.add(url);
    }

    public void addFile(File modFile) throws MalformedURLException
    {
        URL url = modFile.toURI().toURL();
        this.addURL(url);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException
    {
        Log.message("Loading " + name + " ...");
        return super.loadClass(name);
    }

}
