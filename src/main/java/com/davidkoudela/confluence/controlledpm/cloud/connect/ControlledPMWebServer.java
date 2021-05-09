package com.davidkoudela.confluence.controlledpm.cloud.connect;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Description: Web Service
 *              Right now it is for testing purposes
 * Copyright (C) 2016 David Koudela
 *
 * @author dkoudela
 * @since 2016-09-30
 */
public class ControlledPMWebServer extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException
    {
        handleRequest(httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
    {
        handleRequest(httpServletRequest, httpServletResponse);
    }

    private void handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
    {
        String contextPath = httpServletRequest.getServletPath();
        if (contextPath.endsWith(".png")) {
            httpServletResponse.setContentType("application/octet-stream;charset=UTF-8");
        } else if (contextPath.endsWith(".xml")) {
            httpServletResponse.setContentType("application/xml");
        } else {
            httpServletResponse.setContentType("text/plain");
        }
        httpServletResponse.addHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");

        OutputStream out = null;
        InputStream in = null;
        try {
            out = httpServletResponse.getOutputStream();
            in = ControlledPMResourceManager.getResourceStream(contextPath);
            try {
                byte[] buffer = new byte[1024]; // Adjust if you want
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            } catch (Exception e) {
                System.out.println("Writing to out stream failed.");
                System.out.println(e);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        finally {
            if (null != out) {
                try {
                    out.close();
                } catch (Exception e) {
                    System.out.println("Closing out stream failed.");
                    System.out.println(e);
                }
            }
            if (null != in) {
                try {
                    in.close();
                } catch (Exception e) {
                    System.out.println("Closing in stream failed.");
                    System.out.println(e);
                }
            }
        }
    }
}
