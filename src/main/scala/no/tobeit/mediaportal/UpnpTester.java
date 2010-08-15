package no.tobeit.mediaportal;

import org.teleal.cling.*;
import org.teleal.cling.controlpoint.ActionCallback;
import org.teleal.cling.model.action.ActionException;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.message.UpnpResponse;
import org.teleal.cling.model.message.header.STAllHeader;
import org.teleal.cling.model.meta.*;
import org.teleal.cling.model.types.*;
import org.teleal.cling.registry.DefaultRegistryListener;
import org.teleal.cling.registry.Registry;
import org.teleal.cling.registry.RegistryListener;


public class UpnpTester implements Runnable {
    public void run() {
        UpnpService upnpService = new UpnpServiceImpl();
        // Add a listener for device registration events
        upnpService.getRegistry().addListener(
                createRegistryListener(upnpService)
        );


        // Broadcast a search message for all devices
        upnpService.getControlPoint().search(new STAllHeader());

    }


    RegistryListener createRegistryListener(final UpnpService upnpService) {
        return new DefaultRegistryListener() {
            ServiceId serviceId = new UDAServiceId("WANIPConn1");

            @Override
            public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
                RemoteService wanipconn;
                if ((wanipconn = device.findService(serviceId)) != null) {
                    System.out.println("Service discovered: " + wanipconn);
                    executeAction(upnpService, wanipconn);
                }

            }

            @Override
            public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
                RemoteService wanipconn;
                if ((wanipconn = device.findService(serviceId)) != null) {
                    System.out.println("Service disappeared: " + wanipconn);
                }
            }

        };
    }

    void executeAction(UpnpService upnpService, Service wanip) {
        ActionInvocation addPortMappingInvocation =  new AddPortMappingActionInvocation(wanip);
        // Executes asynchronous in the background
        upnpService.getControlPoint().execute(
                new ActionCallback(addPortMappingInvocation) {
                    public void success(ActionInvocation actionInvocation) {
                        assert actionInvocation.getOutput().getValues().length == 0;
                        System.out.println("Successfully called action!");
                    }

                    public void failure(ActionInvocation actionInvocation, UpnpResponse operation) {
                        System.out.println("Failed action");
                        System.err.println(createDefaultFailureMessage(actionInvocation, operation));
                    }
                }
        );
    }


    class AddPortMappingActionInvocation extends ActionInvocation {
        AddPortMappingActionInvocation(Service service) {
            super(service.getAction("AddPortMapping"));
            try {
                // This might throw an ActionException if the value is of wrong type
                getInput().addValue(null);                              // NewRemoteHost
                getInput().addValue(new UnsignedIntegerTwoBytes(1234)); // NewExternalPort
                getInput().addValue("TCP");                             // NewProtocol
                getInput().addValue(new UnsignedIntegerTwoBytes(1234)); // NewInternalPort

                // TODO: This may return null
                String localhost = NetUtilities.getNonLoopbackAddress().getHostAddress();
                System.out.println("Adding port to : " + localhost);

                getInput().addValue(localhost);                         // NewInternalClient
                getInput().addValue(true);                              // NewEnabled
                getInput().addValue("Description");                     // NewPortMappingDescription
                getInput().addValue(new UnsignedIntegerFourBytes(0));   // NewLeaseDuration
            } catch (ActionException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    class DeletePortMappingActionInvocation extends ActionInvocation {
        DeletePortMappingActionInvocation(Service service) {
            super(service.getAction("DeletePortMapping"));
            try {
                // This might throw an ActionException if the value is of wrong type
                getInput().addValue(null);                              // NewRemoteHost
                getInput().addValue(new UnsignedIntegerTwoBytes(1234)); // NewExternalPort
                getInput().addValue("TCP");                             // NewProtocol
            } catch (ActionException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }


    public static void main(String[] args) throws Exception {
        Thread client = new Thread(new UpnpTester());
        client.setDaemon(false);
        client.start();
        Thread.sleep(5000);
    }
}
