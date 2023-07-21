package com.bluefoxhost.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

public class LogHelper {
    public static Logger setupLogger(String name, String file) throws IOException {
        Logger logger = Logger.getLogger(name);
        logger.setUseParentHandlers(false);

        Level level = Level.INFO;
        logger.setLevel(level);

        File logsDirectory = new File("logs");
        if (!logsDirectory.exists()) {
            logsDirectory.mkdir();
        }

        FileHandler fh = new FileHandler(logsDirectory + File.separator + file, true);
        fh.setFormatter(new CustomFormatter());
        logger.addHandler(fh);

        return logger;
    }
}

