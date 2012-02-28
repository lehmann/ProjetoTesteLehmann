package andre;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import andre.net.ConnectionOpener;
import andre.net.DefaultConnectionOpener;

public class Downloader {

	private URL domain;
	private String namePattern;
	private File destinFolder;
	private String[] acceptedExtensions;
	private ConnectionOpener opener;

	public Downloader(URL domain, String namePattern, File destinFolder, String ... acceptedExtensions) {
		this(domain, namePattern, destinFolder, new DefaultConnectionOpener(), acceptedExtensions);
	}

	public Downloader(URL domain, String namePattern, File destinFolder, ConnectionOpener opener, String ... acceptedExtensions) {
		this.domain = domain;
		this.namePattern = namePattern;
		this.destinFolder = destinFolder;
		this.acceptedExtensions = acceptedExtensions;
		this.opener = opener;
	}

	public void download() {
		try {
			Date startDateToDownload = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2011");
			Date endDateToDownload = new Date();
			download(startDateToDownload, endDateToDownload);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public void download(Date startDateToDownload, Date endDateToDownload) {
		List<String> filesToDownload = new LinkedList<String>();
		StringBuilder out = new StringBuilder();
		Formatter fileNamePattern = new Formatter(out);
		Calendar dayToDownload = GregorianCalendar.getInstance();
		dayToDownload.setTime(startDateToDownload);
		Calendar limitDayToDownload = GregorianCalendar.getInstance();
		limitDayToDownload.setTime(endDateToDownload);
		while(dayToDownload.compareTo(limitDayToDownload) < 1) {
			String fileName = fileNamePattern.format(this.namePattern, dayToDownload.get(Calendar.YEAR) % 100,
					dayToDownload.get(Calendar.MONTH) + 1, dayToDownload.get(Calendar.DAY_OF_MONTH)).toString();
			out.delete(0, out.length());
			filesToDownload.add(fileName);
			dayToDownload.add(Calendar.DATE, 1);
		}
		try {
			for (String fileName : filesToDownload) {
				URL url;
				url = new URL(domain, fileName);
				URL file = null;
				for (String extension : acceptedExtensions) {
					file = new URL(url.toExternalForm() + '.' + extension);
					try {
						download(file, destinFolder);
						break;
					} catch (IOException e) {
						file = null;
						// tenta a próxima extensão
					}
				}
				if (file == null) {
					new FileNotFoundException(new Formatter().format(
							"The remote file %s doesn't exists", fileName)
							.toString()).printStackTrace();
				}
			}
		} catch (MalformedURLException e1) {
			throw new RuntimeException(e1);
		}
	}

	private void download(URL url, File destinFolder2) throws IOException {
		String fileName = url.getPath();
		fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
		File file = new File(destinFolder2, fileName);
		if(!file.exists()) {
			FileOutputStream fos = new FileOutputStream(file);
			try {
				writeTo(opener.openStream(url), fos);
			} catch(IOException e) {
				fos.close();
				boolean deleted = file.delete();
				assert deleted;
				throw e;
			}
		}
	}

	private byte[] read(InputStream stream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int r = -1;
		byte[] buff = new byte[1024];
		while((r = stream.read(buff)) > 0) {
			baos.write(buff, 0, r);
		}
		return baos.toByteArray();
	}

	private void writeTo(InputStream is, OutputStream stream) throws FileNotFoundException,
			IOException {
		byte[] buff = new byte[1024];
		int r = -1;
		while((r = is.read(buff, 0, 1024)) > 0) {
			stream.write(buff, 0, r);
		}
		is.close();
		stream.flush();
		stream.close();
	}

}
