package com.anh.movie;

import io.jsonwebtoken.impl.crypto.MacProvider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.util.Date;

@ApplicationPath("/")
public class Application extends ResourceConfig { 

	private static Key key;
	private static DatabaseReference  mDatabase;
	
	public Application() {
		this(MacProvider.generateKey());
	}

	public Application(final Key key) {
		Application.setKey(key);
		register(LoggingFilter.class);
		register(RolesAllowedDynamicFeature.class);
		register(JWTSecurityFilter.class);
		register(JacksonFeature.class);
		register(MultiPartFeature.class);
		packages("com.anh.movie");
		register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(getKey()).to(Key.class);
            }
        });
		property("jersey.config.beanValidation.enableOutputValidationErrorEntity.server", "true");
		property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
		Path path = null;
		path = Paths.get("webapp/WEB-INF/currencyserver240395-bd7933f24eae.json");
		System.out.println("file path2:" + path.toString());
		syncData(path);

	}

	public static Key getKey() {
		return key;
	}

	public static void setKey(Key key) {
		Application.key = key;
	}

	public static void syncData(Path path) {
		System.out.println("file path4:" + path.toString());
		FileInputStream serviceAccount = null;
		try {
			serviceAccount = new FileInputStream(path.toString());
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("file path3:" + path.toString());
		FirebaseOptions options = null;
		try {
			options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://currencyserver240395.firebaseio.com/").build();
		} catch (IOException e) {
			System.out.println("da vao day2");

			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		FirebaseApp.initializeApp(options);
		mDatabase = FirebaseDatabase.getInstance().getReference("currency");
		mDatabase.setValueAsync("©2016 androidhive. All rights Reserved");

	}
}
