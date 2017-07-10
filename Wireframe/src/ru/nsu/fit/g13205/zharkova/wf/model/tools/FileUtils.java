package ru.nsu.fit.g13205.zharkova.wf.model.tools;

import ru.nsu.fit.g13205.zharkova.wf.view.MainView;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

/**
 * Created by Yana on 23.03.16.
 */
public class FileUtils {
    private static File dataDirectory = null;
    public static File getDataDirectory()
    {
        if(dataDirectory == null)
        {
            try
            {
                String path = URLDecoder.decode(MainView.class.getProtectionDomain().getCodeSource().getLocation().getFile(), Charset.defaultCharset().toString());
                dataDirectory = new File(path).getParentFile().getParentFile().getParentFile();
            }
            catch (UnsupportedEncodingException e)
            {
                dataDirectory = new File(".");
            }
            if(dataDirectory == null || !dataDirectory.exists()) dataDirectory = new File(".");
            for(File f: dataDirectory.listFiles())
            {
                if(f.isDirectory() && f.getName().endsWith("_Data"))
                {
                    dataDirectory = f;
                    break;
                }
            }
        }
        return dataDirectory;
    }
}