package opendata.scholia.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Uname {
    public String sysname;
    public String nodename;
    public String release;
    public String version;
    public String machine;
    public String domainname;
    public String scholiaid = "_unknown";

    public Uname() {
        sysname = System.getProperty("os.name");
        release = System.getProperty("os.version");
        machine = System.getProperty("os.arch");
        if (machine.equals("amd64")) {
            machine = "x86_64";
        }
        if (release == null) {
            release = getRelease("1.0");
        }

        nodename = getHostname("(none)");
        version = getVersion("1.0");
        domainname = getDomainName("localdomain");
        scholiaid = ConfigManager.instance().getConfig().getString("id", "");
        scholiaid = scholiaid.contentEquals("") ? "_unknown" : scholiaid;
    }

    private static String readFile(String name, String fallback) {
        try {
            Path path = Paths.get(name);
            if (Files.exists(path)) {
                return new String(Files.readAllBytes(path)).trim();
            }
        } catch (Exception e) {
        }
        return fallback;
    }

    private static String getHostname(String fallback) {
        String hostname = readFile("/proc/sys/kernel/hostname", null);
        if (hostname != null) {
            return hostname;
        }
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return fallback;
        }
    }

    private static String getVersion(String fallback) {
        return readFile("/proc/sys/kernel/version", fallback);
    }

    private static String getDomainName(String fallback) {
        return readFile("/proc/sys/kernel/domainname", fallback);
    }

    private static String getRelease(String fallback) {
        return readFile("/proc/sys/kernel/osrelease", fallback);
    }


}