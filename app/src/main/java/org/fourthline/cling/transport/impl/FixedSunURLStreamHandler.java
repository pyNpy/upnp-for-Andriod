/*
 * Copyright (C) 2013 4th Line GmbH, Switzerland
 *
 * The contents of this file are subject to the terms of either the GNU
 * Lesser General Public License Version 2 or later ("LGPL") or the
 * Common Development and Distribution License Version 1 or later
 * ("CDDL") (collectively, the "License"). You may not use this file
 * except in compliance with the License. See LICENSE.txt for more
 * information.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

package org.fourthline.cling.transport.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.logging.Logger;

/**
 *
 * The SUNW morons restrict the JDK handlers to GET/POST/etc for "security" reasons.
 * <p>
 * They do not understand HTTP. This is the hilarious comment in their source:
 * </p>
 * <p>
 * "This restriction will prevent people from using this class to experiment w/ new
 * HTTP methods using java.  But it should be placed for security - the request String
 * could be arbitrarily long."
 * </p>
 *
 * @author Christian Bauer
 */
public class FixedSunURLStreamHandler implements URLStreamHandlerFactory {

    final private static Logger log = Logger.getLogger(FixedSunURLStreamHandler.class.getName());

    public URLStreamHandler createURLStreamHandler(String protocol) {
        log.fine("Creating new URLStreamHandler for protocol: " + protocol);
        if ("http".equals(protocol)) {

            return  new URLStreamHandler() {
                @Override
                protected URLConnection openConnection(URL u) throws IOException {
                     return u.openConnection();
                }
            };
        } else {
            return null;
        }
    }


}
