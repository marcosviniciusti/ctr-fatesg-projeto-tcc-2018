package br.com.senai.ctr;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CTRLogs {

    private static final SimpleDateFormat fmt;
    private static final PrintStream log;

    static {
        fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        log = System.out;
    }

    public static void log(String msg) {
        log.println(fmt.format(new Date()) + " - " + msg);
    }
}
