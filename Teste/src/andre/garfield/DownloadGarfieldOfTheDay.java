package andre.garfield;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import andre.Downloader;
import andre.net.DelayedConnectionOpener;

public class DownloadGarfieldOfTheDay {

	public static void main(String[] args) throws MalformedURLException {
		URL domain = new URL("http://images.ucomics.com/comics/ga/2011/");
		String namePattern= "ga%1$02d%2$02d%3$02d";
		File destin = new File("C:\\temp\\garfield");
		String[] extensions = new String[] {"gif", "jpg"};
		new Downloader(domain, namePattern, destin, new DelayedConnectionOpener(2000), extensions).download();
	}
}
