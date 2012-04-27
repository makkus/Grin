package grisu.jcommons.model.info.rest;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class RestHelpers {


	public static void main(String[] args) throws Exception {


		URL url = new URL("http://cluster.ceres.auckland.ac.nz/rest/node");
		// String url = "http://cluster.ceres.auckland.ac.nz/rest/job",


		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();

		InputStream responseStream = connection.getInputStream();

		System.out.println(IOUtils.toString(responseStream));

		// JSONJAXBContext jc = new JSONJAXBContext(ListMusic.class);
		// JSONUnmarshaller u = jc.createJSONUnmarshaller();
		//
		// ListMusic m = u.unmarshalFromJSON(responseStream, ListMusic.class);
		// connection.disconnect();

		// System.out.println(m.getMusic());

	}
}
