import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

import at.tuwien.hoppla.migration.installation.services.internal.DebianPackageInstallation;


public class TestDebianPackageInstaller {

	@Test
	public void testIsInstalled() {
		DebianPackageInstallation i = new DebianPackageInstallation();
		Map<String, String> map = new HashMap<String, String>();
		map.put("packageName", "calibre");
		assertTrue(i.isInstalled(map));
	}
	
	@Test
	public void testNotInstalled() {
		DebianPackageInstallation i = new DebianPackageInstallation();
		Map<String, String> map = new HashMap<String, String>();
		map.put("packageName", "asdfsadfaasdf");
		assertFalse(i.isInstalled(map));
	}
	
	@Test 
	public void testInstall() throws Exception {
		DebianPackageInstallation i = new DebianPackageInstallation();
		Map<String, String> map = new HashMap<String, String>();
		map.put("packageName", "calibre");
		i.install(map);
	}

	@Test
	@Ignore
	public void testRemove() throws Exception {
		DebianPackageInstallation i = new DebianPackageInstallation();
		Map<String, String> map = new HashMap<String, String>();
		map.put("packageName", "calibre");
		i.uninstall(map);
	}
	
	
	
}
