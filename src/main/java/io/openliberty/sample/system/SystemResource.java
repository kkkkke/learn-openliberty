// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2017, 2019 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
package io.openliberty.sample.system;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.Response;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@RequestScoped
@Path("/properties")
public class SystemResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Timed(name = "getPropertiesTime", description = "Time needed to get the JVM system properties")
	@Counted(absolute = true, description = "Number of times the JVM system properties are requested")
	public Response getProperties() {
		SSLContext current;
		TrustManagerFactory tmf = null;
		String defaultAlgo = TrustManagerFactory.getDefaultAlgorithm();
		String defaultTrustType;
		try {
			defaultTrustType = KeyStore.getDefaultType();
			KeyStore ks = KeyStore.getInstance(defaultTrustType);
			tmf = TrustManagerFactory.getInstance(defaultAlgo);
			tmf.init(ks);
		} catch (NoSuchAlgorithmException | KeyStoreException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		TrustManager[] tms = tmf.getTrustManagers();
		TrustManager t = Arrays.stream(tms).filter(tm -> tm instanceof X509TrustManager).findFirst().map(X509TrustManager.class::cast).orElse(null);
		try {
			current = SSLContext.getDefault();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return Response.ok(System.getProperties()).build();
	}

}
