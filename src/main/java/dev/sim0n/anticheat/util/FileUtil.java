package dev.sim0n.anticheat.util;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.Channels;

@UtilityClass
public class FileUtil {
    public ClassLoader CLASS_LOADER = FileUtil.class.getClassLoader();

    /**
     * Downloads a file to {@param file}
     * @param file Where to download the file to
     * @param from Where to download the file from
     */
    public void download(File file, String from) throws Exception {
        FileOutputStream out = new FileOutputStream(file);

        out.getChannel().transferFrom(Channels.newChannel(new URL(from).openStream()), 0L, Long.MAX_VALUE);
    }

    public void injectUrl(URL url) {
        try {
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);

            method.setAccessible(true);
            method.invoke(CLASS_LOADER, url);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}


