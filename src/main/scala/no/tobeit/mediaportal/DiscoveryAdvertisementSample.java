package no.tobeit.mediaportal;

import net.sbbi.upnp.DiscoveryAdvertisement;
import net.sbbi.upnp.DiscoveryEventHandler;

import java.io.IOException;

/**
 * User: mikael
 * Date: Jul 16, 2010
 * Time: 11:55:15 AM
 */
public class DiscoveryAdvertisementSample {

    public final static void main(String args[]) throws IOException {

        AdvHandler handler = new AdvHandler();

        DiscoveryAdvertisement.getInstance().setDaemon(false);
        System.out.println("Registering EVENT_SSDP_ALIVE event");
        DiscoveryAdvertisement.getInstance().registerEvent(DiscoveryAdvertisement.EVENT_SSDP_ALIVE, "upnp:rootdevice", handler);
        System.out.println("Registering EVENT_SSDP_BYE_BYE event");
        DiscoveryAdvertisement.getInstance().registerEvent(DiscoveryAdvertisement.EVENT_SSDP_BYE_BYE, "upnp:rootdevice", handler);
        System.out.println("Waiting for incoming events");
    }

    private static class AdvHandler implements DiscoveryEventHandler {

        public void eventSSDPAlive(String usn, String udn, String nt, String maxAge, java.net.URL location) {
            System.out.println("Root device at " + location + " plugged in network, advertisement will expire in " + maxAge + " ms");
        }

        public void eventSSDPByeBye(String usn, String udn, String nt) {
            System.out.println("Bye Bye usn:" + usn + " udn:" + udn + " nt:" + nt);
        }

    }


}