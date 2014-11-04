package org.craft;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.jar.*;
import java.util.jar.Attributes.Name;

import com.google.common.collect.*;

import org.craft.utils.*;
import org.craft.utils.asm.*;
import org.craft.utils.crash.*;

/**
 * Adapted from Mojang AB's Legacy Launcher code:<br/>
 * <a href="https://github.com/Mojang/LegacyLauncher/">https://github.com/Mojang/LegacyLauncher/</a>
 */
public class OurClassLoader extends URLClassLoader
{
    private List<URL>                  sources;
    private List<IClassTransformer>    transformers;
    private List<String>               invalidNames;
    private List<String>               transformerExclusions;
    private List<String>               classloaderExclusions;
    private HashMap<String, Class<?>>  cached;
    private HashMap<Package, Manifest> packageManifests;
    private Map<String, byte[]>        resourceCache;

    private final static int           BUFFER_SIZE           = 1 << 12;
    private Set<String>                negativeResourceCache = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

    private static final Manifest      EMPTY                 = new Manifest();

    private final ThreadLocal<byte[]>  loadBuffer            = new ThreadLocal<byte[]>();

    private static final String[]      RESERVED_NAMES        =
                                                             {
            "CON", "PRN", "AUX", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"
                                                             };

    public static OurClassLoader       instance              = null;

    public OurClassLoader(ClassLoader loader)
    {
        this(((URLClassLoader) loader).getURLs(), loader);
    }

    private OurClassLoader(URL[] sources, ClassLoader parent)
    {
        super(sources, null);
        OurClassLoader.instance = this;
        resourceCache = Maps.newConcurrentMap();
        cached = Maps.newHashMap();
        packageManifests = Maps.newHashMap();
        invalidNames = Lists.newArrayList();
        classloaderExclusions = Lists.newArrayList();
        transformerExclusions = Lists.newArrayList();
        this.sources = Lists.newArrayList(sources);

        addClassLoaderExclusion("java.");
        addClassLoaderExclusion("sun.");
        addClassLoaderExclusion("org.lwjgl.");
        //        addClassLoaderExclusion("org.craft.modding.");
        addClassLoaderExclusion("org.apache.logging.");
        addClassLoaderExclusion("org.objectweb.asm.");
        addClassLoaderExclusion("org.reflections.");
        addClassLoaderExclusion("javassist.");
        addClassLoaderExclusion("com.google.");
        addClassLoaderExclusion("org.slf4j.");

        // transformer exclusions
        addTransformerExclusion("javax.");
        addTransformerExclusion("argo.");
        addTransformerExclusion("org.objectweb.asm.");
        addTransformerExclusion("com.google.common.");
        addTransformerExclusion("org.bouncycastle.");

        cached.put(getClass().getCanonicalName(), getClass());
        cached.put(IClassTransformer.class.getCanonicalName(), IClassTransformer.class);

    }

    public void addClassLoaderExclusion(String packageStart)
    {
        this.classloaderExclusions.add(packageStart);
    }

    public void addTransformerExclusion(String packageStart)
    {
        this.transformerExclusions.add(packageStart);
    }

    public void addTransformer(IClassTransformer transformer)
    {
        if(transformers == null)
            transformers = Lists.newArrayList();
        transformer.init(this);
        transformers.add(transformer);
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
    public Class<?> findClass(final String name) throws ClassNotFoundException
    {
        if(cached.containsKey(name))
        {
            return cached.get(name);
        }
        if(invalidNames.contains(name))
        {
            throw new ClassNotFoundException(name);
        }
        for(String exclusion : classloaderExclusions)
        {
            if(name.startsWith(exclusion))
            {
                try
                {
                    Class<?> clazz = super.findClass(name);
                    cached.put(name, clazz);
                    return clazz;
                }
                catch(ClassNotFoundException e)
                {
                    invalidNames.add(name);
                    throw e;
                }
            }
        }
        try
        {
            final String transformedName = transformName(name);
            if(cached.containsKey(transformedName))
            {
                return cached.get(transformedName);
            }
            Log.message("Loading " + transformedName);

            final String untransformedName = untransformName(name);

            final int lastDot = untransformedName.lastIndexOf('.');
            final String packageName = lastDot == -1 ? "" : untransformedName.substring(0, lastDot);
            final String fileName = untransformedName.replace('.', '/').concat(".class");
            URLConnection urlConnection = findCodeSourceConnectionFor(fileName);

            CodeSigner[] signers = null;

            if(lastDot > -1 && !untransformedName.startsWith("net.minecraft."))
            {
                if(urlConnection instanceof JarURLConnection)
                {
                    final JarURLConnection jarURLConnection = (JarURLConnection) urlConnection;
                    final JarFile jarFile = jarURLConnection.getJarFile();

                    if(jarFile != null && jarFile.getManifest() != null)
                    {
                        final Manifest manifest = jarFile.getManifest();
                        final JarEntry entry = jarFile.getJarEntry(fileName);

                        Package pkg = getPackage(packageName);
                        getClassBytes(untransformedName);
                        signers = entry.getCodeSigners();
                        if(pkg == null)
                        {
                            pkg = definePackage(packageName, manifest, jarURLConnection.getJarFileURL());
                            packageManifests.put(pkg, manifest);
                        }
                        else
                        {
                            if(pkg.isSealed() && !pkg.isSealed(jarURLConnection.getJarFileURL()))
                            {
                                Log.error("The jar file " + jarFile.getName() + " is trying to seal already secured path " + packageName);
                            }
                            else if(isSealed(packageName, manifest))
                            {
                                Log.error("The jar file " + jarFile.getName() + " has a security seal for path " + packageName + ", but that path is defined and not secure");
                            }
                        }
                    }
                }
                else
                {
                    Package pkg = getPackage(packageName);
                    if(pkg == null)
                    {
                        pkg = definePackage(packageName, null, null, null, null, null, null, null);
                        packageManifests.put(pkg, EMPTY);
                    }
                    else if(pkg.isSealed())
                    {
                        Log.error("The URL " + urlConnection.getURL() + " is defining elements for sealed path " + packageName);
                    }
                }
            }

            final byte[] transformedClass = runTransformers(untransformedName, transformedName, getClassBytes(untransformedName));
            final CodeSource codeSource = urlConnection == null ? null : new CodeSource(urlConnection.getURL(), signers);
            Class<?> clazz = defineClass(transformedName, transformedClass, 0, transformedClass.length, codeSource);
            return clazz;
        }
        catch(Throwable e)
        {
            invalidNames.add(name);
            new CrashReport(e).printStack();
            throw new ClassNotFoundException(name, e);
        }
    }

    public Class<?> define(String name, byte[] bytes, int start, int len)
    {
        final byte[] transformedClass = runTransformers(name, name, bytes);
        final CodeSource codeSource = null;
        Class<?> clazz = defineClass(name, transformedClass, 0, transformedClass.length, codeSource);
        cached.put(name, clazz);
        return clazz;
    }

    private boolean isSealed(final String path, final Manifest manifest)
    {
        Attributes attributes = manifest.getAttributes(path);
        String sealed = null;
        if(attributes != null)
        {
            sealed = attributes.getValue(Name.SEALED);
        }

        if(sealed == null)
        {
            attributes = manifest.getMainAttributes();
            if(attributes != null)
            {
                sealed = attributes.getValue(Name.SEALED);
            }
        }
        return "true".equalsIgnoreCase(sealed);
    }

    private byte[] runTransformers(String untransformedName, String transformedName, byte[] classBytes)
    {
        if(transformers != null)
            for(IClassTransformer transformer : transformers)
            {
                classBytes = transformer.apply(untransformedName, transformedName, classBytes);
            }
        return classBytes;
    }

    private byte[] readFully(InputStream stream)
    {
        try
        {
            byte[] buffer = getOrCreateBuffer();

            int read;
            int totalLength = 0;
            while((read = stream.read(buffer, totalLength, buffer.length - totalLength)) != -1)
            {
                totalLength += read;

                // Extend our buffer
                if(totalLength >= buffer.length - 1)
                {
                    byte[] newBuffer = new byte[buffer.length + BUFFER_SIZE];
                    System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
                    buffer = newBuffer;
                }
            }

            final byte[] result = new byte[totalLength];
            System.arraycopy(buffer, 0, result, 0, totalLength);
            return result;
        }
        catch(Throwable t)
        {
            Log.error("Problem loading class: " + t.getMessage());
            return new byte[0];
        }
    }

    private byte[] getOrCreateBuffer()
    {
        byte[] buffer = loadBuffer.get();
        if(buffer == null)
        {
            loadBuffer.set(new byte[BUFFER_SIZE]);
            buffer = loadBuffer.get();
        }
        return buffer;
    }

    public List<IClassTransformer> getTransformers()
    {
        return Collections.unmodifiableList(transformers);
    }

    public byte[] getClassBytes(String name) throws IOException
    {
        if(negativeResourceCache.contains(name))
        {
            return null;
        }
        else if(resourceCache.containsKey(name))
        {
            return resourceCache.get(name);
        }
        if(name.indexOf('.') == -1)
        {
            for(final String reservedName : RESERVED_NAMES)
            {
                if(name.toUpperCase(Locale.ENGLISH).startsWith(reservedName))
                {
                    final byte[] data = getClassBytes("_" + name);
                    if(data != null)
                    {
                        resourceCache.put(name, data);
                        return data;
                    }
                }
            }
        }

        InputStream classStream = null;
        try
        {
            final String resourcePath = name.replace('.', '/').concat(".class");
            final URL classResource = findResource(resourcePath);

            if(classResource == null)
            {
                negativeResourceCache.add(name);
                return null;
            }
            classStream = classResource.openStream();

            final byte[] data = readFully(classStream);
            resourceCache.put(name, data);
            return data;
        }
        finally
        {
            closeSilently(classStream);
        }
    }

    private static void closeSilently(Closeable closeable)
    {
        if(closeable != null)
        {
            try
            {
                closeable.close();
            }
            catch(IOException ignored)
            {
            }
        }
    }

    public void clearNegativeEntries(Set<String> entriesToClear)
    {
        negativeResourceCache.removeAll(entriesToClear);
    }

    private URLConnection findCodeSourceConnectionFor(String fileName)
    {
        final URL resource = findResource(fileName);
        if(resource != null)
        {
            try
            {
                return resource.openConnection();
            }
            catch(IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private String untransformName(String name)
    {
        // Do we need a rename transformer?
        return name;
    }

    private String transformName(String name)
    {
        // Do we need a rename transformer?
        return name;
    }

    public void unload(String toModifyClass)
    {
        if(cached.containsKey(toModifyClass))
        {
            cached.remove(toModifyClass);
        }
    }
}
