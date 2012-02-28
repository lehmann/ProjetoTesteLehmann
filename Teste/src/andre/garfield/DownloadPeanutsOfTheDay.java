package andre.garfield;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import andre.Downloader;
import andre.net.DelayedConnectionOpener;

public class DownloadPeanutsOfTheDay {

	public static void main(String[] args) throws MalformedURLException, ParseException {
		URL domain = new URL("http://images.ucomics.com/comics/pe/2011/");
		String namePattern= "pe%1$02d%2$02d%3$02d";
		File destin = new File("C:\\temp\\peanuts");
		destin.mkdirs();
		String[] extensions = new String[] {"gif", "jpg"};
		Date startDateToDownload = new SimpleDateFormat("dd/MM/yyyy").parse("28/03/2011");
		Date endDateToDownload = new Date();
		new Downloader(domain, namePattern, destin, new DelayedConnectionOpener(2000), extensions).download(startDateToDownload, endDateToDownload);
	}
}
