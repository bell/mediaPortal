package no.tobeit.mediaportal;

import org.teleal.cling.*;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.message.header.STAllHeader;
import org.teleal.cling.model.meta.DeviceDetails;
import org.teleal.cling.model.meta.DeviceService;
import org.teleal.cling.model.meta.RemoteDevice;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.model.types.ServiceId;
import org.teleal.cling.model.types.UDAServiceId;
import org.teleal.cling.registry.DefaultRegistryListener;
import org.teleal.cling.registry.Registry;
import org.teleal.cling.registry.RegistryListener;
import org.teleal.cling.transport.Router;


public class UpnpTester implements Runnable {
    public void run() {
        UpnpService upnpService = new UpnpServiceImpl();

        // Add a listener for device registration events
        upnpService.getRegistry().addListener(
                createRegistryListener(upnpService)
        );


        // Broadcast a search message for all devices
        upnpService.getControlPoint().search(
                new STAllHeader()
        );
    }


    RegistryListener createRegistryListener(final UpnpService upnpService) {
        return new DefaultRegistryListener() {

            ServiceId serviceId = new UDAServiceId("SwitchPower");

            @Override
            public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
                System.out.println("Device: " + device.getDisplayString());
                DeviceDetails dd = device.getDetails();
                System.out.println("BaseURL: " + dd.getBaseURL());
                DeviceService switchPower;
                if ((switchPower = device.findDeviceService(serviceId)) != null) {
                    System.out.println("Service discovered: " + switchPower);
                    executeAction(upnpService, switchPower.getService());
                }

            }

            @Override
            public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
                DeviceService switchPower;
                if ((switchPower = device.findDeviceService(serviceId)) != null) {
                    System.out.println("Service disappeared: " + switchPower);
                }
            }

        };
    }

    void executeAction(UpnpService upnpService, Service switchPowerService) {
    }         

    public static void main(String[] args) throws Exception {
        Thread client = new Thread(new UpnpTester());
        client.setDaemon(false);
        client.start();
        Thread.sleep(5000);
    }
}
