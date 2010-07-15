package no.tobeit.mediaportal;

import net.sbbi.upnp.Discovery;
import net.sbbi.upnp.devices.UPNPRootDevice;

import java.io.IOException;

public class UpnpTester {
    public static void main(String[] args) throws IOException {
        UPNPRootDevice[] devices = Discovery.discover();
        if(devices!=null) {
            for (int i = 0; i < devices.length; i++) {
                System.out.println("Found device " + devices[i].getModelName());
            }
        } else {
            System.out.println("Fant ingen devices...");
        }
    }
}
