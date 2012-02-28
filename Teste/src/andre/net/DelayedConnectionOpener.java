package andre.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DelayedConnectionOpener implements ConnectionOpener {
	
	private long lastInvacation = -1;
	private int delay;
	
	public DelayedConnectionOpener(int delay) {
		this.delay = delay;
	}

	@Override
	public URLConnection openConnection(URL url) throws IOException {
		if(lastInvacation + delay > System.currentTimeMillis()) {
			lastInvacation = System.currentTimeMillis();
			return url.openConnection();
		}
		try {
			Thread.sleep((lastInvacation + delay) - System.currentTimeMillis());
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return url.openConnection();
	}

	@Override
	public InputStream openStream(URL url) throws IOException {
		if(lastInvacation + delay < System.currentTimeMillis()) {
			lastInvacation = System.currentTimeMillis();
			return url.openStream();
		}
		try {
			Thread.sleep((lastInvacation + delay) - System.currentTimeMillis());
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return url.openStream();
	}

}
