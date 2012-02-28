package andre.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public interface ConnectionOpener {

	URLConnection openConnection(URL url) throws IOException;

	InputStream openStream(URL url) throws IOException;
}
