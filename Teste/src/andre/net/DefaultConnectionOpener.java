package andre.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DefaultConnectionOpener implements ConnectionOpener {

	@Override
	public URLConnection openConnection(URL url) throws IOException {
		return url.openConnection();
	}

	@Override
	public InputStream openStream(URL url) throws IOException {
		return url.openStream();
	}

}
