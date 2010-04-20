package at.tuwien.hoppla.migration.installation.services.internal;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.output.TeeOutputStream;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.tuwien.hoppla.migration.installation.InstallationException;
import at.tuwien.hoppla.migration.installation.InstallationService;


/**
 * This installation allows the system-wide installation of a tool with a 
 * Debian package manager.  
 * @author mike
 *
 */

public class DebianPackageInstallation implements InstallationService {

	private static final Logger log = LoggerFactory.getLogger(DebianPackageInstallation.class);
	private static final String PACKAGE_NAME = "packageName";
	
	public DebianPackageInstallation() {
		// TODO Auto-generated constructor stub
	}
	
	private void exec(String packageName, String cmd) throws InstallationException  {
		try {
			//execute "sudo apt-get install ${packageName}
			Executor exec = new DefaultExecutor();
			CommandLine cl = new CommandLine("sudo");
			cl.addArgument("-S"); // -S  The -S (stdin) option causes sudo to read the password from the standard input instead of the terminal device.
			String pwdCheck = "PASS_REQUEST";
			String pwd = "tux.gnu";
			cl.addArgument("-p"); // -p  The -p (prompt) option allows you to override the default password prompt and use a custom one.
			cl.addArgument(pwdCheck);
			cl.addArgument("apt-get");
			cl.addArgument("-y"); // -y, --yes, --assume-yes Automatic yes to prompts;
			cl.addArgument(cmd);
			cl.addArgument(packageName);
			
			log.info("Executing:" + cl.toString());
			
			ProcessInputStream pis = new ProcessInputStream();
			ProcessOutputStream pos = new ProcessOutputStream(pwdCheck, pwd, pis);
			TeeOutputStream tos = new TeeOutputStream(pos, System.out);
			PumpStreamHandler handler = new PumpStreamHandler(tos, tos, pis);
			exec.setStreamHandler(handler);
						
			int exitvalue = exec.execute(cl);
			if (exitvalue != 0) {
				throw new InstallationException();
			}
		} catch (IOException e) {
			throw new InstallationException(e);
		}
	}
	
	protected void activate(ComponentContext context) {
		log.info("DebianPackageInstallation activated!");
	}

	protected void deactivate(ComponentContext context) {
		log.info("DebianPackageInstallation activated!");
	}

	
	public void install(Map<String, String> options) throws InstallationException {
		String packageName = options.get(PACKAGE_NAME);
		exec(packageName, "install");
	}
	
	
	public void uninstall(Map<String, String> options) throws InstallationException {
		String packageName = options.get(PACKAGE_NAME);
		exec(packageName, "remove");
	}
	
	@Override
	public boolean isInstalled(Map<String, String> options) {
		String packageName = options.get(PACKAGE_NAME);
		log.info("checking: " + packageName);
		try {
			Executor exec = new DefaultExecutor();
			CommandLine cl = new CommandLine("dpkg-query");
			cl.addArgument("-s");
			cl.addArgument(packageName);
			ByteArrayInputStream bis = new ByteArrayInputStream(new byte[0]);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			TeeOutputStream tos = new TeeOutputStream(bos, System.out);
			PumpStreamHandler handler = new PumpStreamHandler(tos, tos, bis);
			exec.setStreamHandler(handler);
			int exitvalue = exec.execute(cl);
			String output = bos.toString();
			BufferedReader r = new BufferedReader(new StringReader(output));
			String line = null;
			Pattern pattern = Pattern.compile("^Status(.)*installed$");
			boolean result = false;
			while ((line = r.readLine()) != null) {
				if (pattern.matcher(line).matches()) {
					result = true;
				}
			}
			return result;
			
		} catch (Exception e) {
			log.warn("Not installed", e);
			return false;
		}
	}
	
	public static class ProcessInputStream extends InputStream {
		
		@Override
		public int available() throws IOException {
			if (input == null) {
				return 0;
			}
			return input.length - offset;
		}
		
		private byte[] input = null;
		private int offset = 0;
		
		public void nextInput(byte[] input) {
			this.input = input;
			this.offset = 0;
		}
		@Override
		public int read() throws IOException {
			if (input == null || offset >= input.length) {
				return -1;
			}
			return input[offset++];
		}
	}
	
	public static class ProcessOutputStream extends OutputStream {
		private String phrase;
		private String password;
		private ProcessInputStream pis;
		private ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		public ProcessOutputStream(String phrase, String password, ProcessInputStream pis) {
			this.phrase = phrase; 
			this.password = password;
			this.pis = pis;
		}
		@Override
		public void write(int b) throws IOException {
			if (b != 10 && b != 13) {
				bos.write(b);	
			}
			checkLine();
		}
		
		private void checkLine() {
			if (new String(bos.toByteArray()).equals(phrase)) {
				pis.nextInput(password.getBytes());;
			}
 		}
	}
}
