package org.craft.loader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;

public class OurClassLoader extends URLClassLoader
{
    private List<URL> sources;
    private URLClassLoader parent;

    public OurClassLoader(URL[] sources)
    {
        super(sources, null);
        this.sources = Arrays.asList(sources);
        parent = (URLClassLoader) getClass().getClassLoader();
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
        //TODO : search into custom sourceURL
        return parent.loadClass(name);
    }

}
