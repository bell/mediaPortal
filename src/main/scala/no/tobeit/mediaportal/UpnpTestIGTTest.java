package no.tobeit.mediaportal;

import net.sbbi.upnp.impls.InternetGatewayDevice;
import net.sbbi.upnp.messages.UPNPResponseException;

import java.io.IOException;
import java.net.InetAddress;

/**
 * User: mikael
 * Date: Jul 16, 2010
 * Time: 11:31:18 AM
 */
public class UpnpTestIGTTest {
    public static void main(String[] args) {
        int discoveryTimeout = 5000; // 5 secs to receive a response from devices
        try {
            InternetGatewayDevice[] IGDs = InternetGatewayDevice.getDevices(discoveryTimeout);
            if (IGDs != null) {
                // let's the the first device found
                InternetGatewayDevice testIGD = IGDs[0];
                System.out.println("Found device " + testIGD.getIGDRootDevice().getModelName());

                // now let's open the port
                String localHostIP = InetAddress.getLocalHost().getHostAddress();
                // we assume that localHostIP is something else than 127.0.0.1
                boolean mapped = testIGD.addPortMapping("Some mapping description",  null, 9090, 9090, localHostIP, 0, "TCP");
                if (mapped) {
                    System.out.println("Port 9090 mapped to " + localHostIP);
                    // and now close it
                    boolean unmapped = testIGD.deletePortMapping(null, 9090, "TCP");
                    if (unmapped) {
                        System.out.println("Port 9090 unmapped");
                    }
                }
            } else {
                System.out.println("Error: No UPnp devices found");
            }
        } catch (IOException ex) {
            // some IO Exception occured during communication with device
            ex.printStackTrace();
        } catch (UPNPResponseException respEx) {
            // oups the IGD did not like something !!
            respEx.printStackTrace();
        }
    }
}
