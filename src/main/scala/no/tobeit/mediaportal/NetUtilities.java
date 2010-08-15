package no.tobeit.mediaportal;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mikael
 */
public class NetUtilities {

    public static InetAddress getFirstNonLoopbackAddress(boolean preferIpv4, boolean preferIPv6) {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();

            while (en.hasMoreElements()) {
                NetworkInterface i = en.nextElement();
                System.out.println("NI: " + i.getDisplayName());
                if(i.getDisplayName().contains("virbr"))
                    continue;
                
                for (Enumeration<InetAddress> en2 = i.getInetAddresses(); en2.hasMoreElements();) {
                    InetAddress addr = en2.nextElement();
                    if (!addr.isLoopbackAddress()) {
                        if (addr instanceof Inet4Address) {
                            if (preferIPv6) {
                                continue;
                            }

                            return addr;
                        }

                        if (addr instanceof Inet6Address) {
                            if (preferIpv4) {
                                continue;
                            }

                            return addr;
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            Logger.getLogger(NetUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }


    public static void main(String[] args) throws Exception {
        System.out.println("Localhost: " + getFirstNonLoopbackAddress(true,false));
    }
}
