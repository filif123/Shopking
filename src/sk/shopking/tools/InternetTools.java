package sk.shopking.tools;

import java.net.*;
import java.util.Enumeration;
import java.util.NoSuchElementException;

public class InternetTools {
    public static String getLocalIP() throws SocketException, UnknownHostException {
        Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
        NetworkInterface ni;
        while (nis.hasMoreElements()) {
            ni = nis.nextElement();
            if (!ni.isLoopback()/*not loopback*/ && ni.isUp()/*it works now*/) {
                for (InterfaceAddress ia : ni.getInterfaceAddresses()) {
                    //filter for ipv4/ipv6
                    if (ia.getAddress().getAddress().length == 4) {
                        //4 for ipv4, 16 for ipv6
                        String thisIP = ia.getAddress().getHostAddress();
                        if (isInRange("10.0.0.0","10.255.255.255",thisIP) || isInRange("172.16.0.0","172.31.255.255",thisIP) || isInRange("192.168.0.0","192.168.255.255",thisIP)){
                            return thisIP;
                        }
                    }
                }
            }
        }
        return null;
    }

    private static long ipToLong(InetAddress ip) {
        byte[] octets = ip.getAddress();
        long result = 0;
        for (byte octet : octets) {
            result <<= 8;
            result |= octet & 0xff;
        }
        return result;
    }

    private static boolean isInRange(String ipLoS,String ipHiS,String ipToTestS) throws UnknownHostException {
        long ipLo = ipToLong(InetAddress.getByName(ipLoS));
        long ipHi = ipToLong(InetAddress.getByName(ipHiS));
        long ipToTest = ipToLong(InetAddress.getByName(ipToTestS));
        return ipToTest >= ipLo && ipToTest <= ipHi;
    }
}
