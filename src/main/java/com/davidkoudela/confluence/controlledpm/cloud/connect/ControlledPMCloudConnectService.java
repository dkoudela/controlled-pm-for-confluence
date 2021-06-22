package com.davidkoudela.confluence.controlledpm.cloud.connect;

import org.mortbay.http.SocketListener;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHttpContext;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Description: Cloud Connect Service
 *              Right now it is for testing purposes
 * Copyright (C) 2016 David Koudela
 *
 * @author dkoudela
 * @since 2016-09-16
 */
public class ControlledPMCloudConnectService {
    public static void main(String[] args) throws InterruptedException {

        try {
            Server server = new Server();
            SocketListener listener = new SocketListener();

            System.out.println("Max Thread :" + listener.getMaxThreads() + " Min Thread :" + listener.getMinThreads());

            listener.setHost("0.0.0.0");
            listener.setPort(Integer.valueOf(System.getenv("PORT")));
            listener.setMinThreads(5);
            listener.setMaxThreads(250);
            server.addListener(listener);

            ServletHttpContext context = (ServletHttpContext) server.getContext("/");
            for (String resource : ControlledPMResourceManager.getResources()) {
                context.addServlet(resource, ControlledPMWebServer.class.getCanonicalName());
            }
            context.addServlet("/", ControlledPMWebServer.class.getCanonicalName());
            System.out.println("Servlet set");

            server.start();
            server.join();
        } catch (Exception ex) {
            Logger.getLogger(ControlledPMCloudConnectService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
