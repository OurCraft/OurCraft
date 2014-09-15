package utils;

import java.io.*;

/**
 * VERY VERY OLD, NEEDS TO BE UPDATED
 * but I put it there because it's shorter
 */
public final class IO
{

    /**
     * @param is
     * @param os
     * @param bufferLength
     * @throws IOException
     */
    public static OutputStream copy(InputStream is, OutputStream os) throws IOException
    {
        int i = 0;
        byte[] buffer = new byte[65565];
        while((i = is.read(buffer, 0, buffer.length)) != -1)
        {
            os.write(buffer, 0, i);
        }
        return os;
    }

    public static OutputStream copy(InputStream in, String output) throws FileNotFoundException, IOException
    {
        return copy(in, new BufferedOutputStream(new FileOutputStream(output)));
    }

    public static String readString(InputStream in, String charset) throws UnsupportedEncodingException, IOException
    {
        return new String(read(in), charset);
    }

    public static byte[] read(InputStream in) throws IOException
    {
        byte[] buffer = new byte[65565];
        ByteArrayOutputStream ous = new ByteArrayOutputStream(buffer.length);
        int i = 0;
        while((i = in.read(buffer, 0, buffer.length)) != -1)
        {
            ous.write(buffer, 0, i);
        }
        ous.close();
        return ous.toByteArray();
    }

    public static void deleteFolderContents(File folder)
    {
        File[] files = folder.listFiles();
        if(files != null) for(File f : files)
        {
            if(f.isDirectory())
            {
                deleteFolderContents(f);
                f.delete();
            }
            else
                f.delete();
        }
    }

}
