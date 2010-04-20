package at.tuwien.hoppla.migration.gui;

import java.util.ArrayList;

import javax.swing.JFrame;

import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.tuwien.hoppla.migration.gui.internal.MigrationFrame;
import at.tuwien.hoppla.migration.installation.InstallationService;

public class GuiService {

	private Logger log = LoggerFactory.getLogger(GuiService.class);
	MigrationFrame frame = null;
	
	protected void  activate(ComponentContext ctx) {
		
		log.debug("GuiService activated");
		frame = new MigrationFrame(ctx.getBundleContext());
		frame.start();
		
	}
	
	protected void deactivate(ComponentContext ctx) {
		frame.stop();
		frame = null;
		log.debug("GuiService deactivated");
	}
	
}
