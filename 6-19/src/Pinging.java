import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Pinging extends Thread {
	private Object[] msg;
	private String ip;

	public Pinging(String ip) {
		this.ip = ip;
		msg = new Object[5];
	}

	public void run() {
		InputStream is = null;
		BufferedReader br = null;
		try {
			Runtime run = Runtime.getRuntime();
			Process p = run.exec("ping -a " + ip);
			msg[0] = ip;
			is = p.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = br.readLine()) != null) {
				// System.out.println(line); // line을 임시 출력해본다.
				if (line.indexOf("[") >= 0) {
					msg[3] = line.substring(5, line.indexOf("["));
				}
				if (line.indexOf("ms") >= 0) {
					// Pattern pattern =
					// Pattern.compile("(\\d+ms)(\\s+)(TTL=\\d+)",Pattern.CASE_INSENSTIVE);
					// Matcher matcher = pattern.matcher(line);
					// System.out.println(matcher.group(1));
					msg[1] = line.substring(line.indexOf("ms") - 1, line.indexOf("ms") + 2);
					msg[2] = line.substring(line.indexOf("TTL=") + 4, line.length());

					final ExecutorService es = Executors.newFixedThreadPool(20);
					final String ip = "127.0.0.1";
					final int timeout = 200;
					final List<Future<ScanResult>> futures = new ArrayList<>();
					// 65535, 1024
					for (int port = 1; port <= 1024; port++) {
						// for (int port = 1; port <= 80; port++) {
						futures.add(portIsOpen(es, ip, port, timeout));
					}
					try {
						es.awaitTermination(200L, TimeUnit.MILLISECONDS);
						int openPorts = 0;
						for (final Future<ScanResult> f : futures) {
							if (f.get().isOpen()) {
								openPorts++;
								msg[4] = f.get().getPort();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				}
			}
			if (line != null) {
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// if

	public static Future<ScanResult> portIsOpen(final ExecutorService es, final String ip, final int port,
			final int timeout) {
		return es.submit(new Callable<ScanResult>() {
			public ScanResult call() {
				try {
					Socket socket = new Socket();
					socket.connect(new InetSocketAddress(ip, port), timeout);
					socket.close();
					return new ScanResult(port, true);
				} catch (Exception ex) {
					return new ScanResult(port, false);
				}
			}
		});
	}

	public Object[] getMsg() {
		try {
			join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return msg;
	}

	public static void main(String[] args) {

	}
}