package at.tuwien.hoppla.migration.gui.internal;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.tuwien.hoppla.migration.installation.InstallationService;

public class ServiceDataModel extends AbstractTableModel {

	private static Logger log = LoggerFactory.getLogger(ServiceDataModel.class);
	
	private BundleContext ctx;
	private ServiceReference[] refs;
	
	public ServiceDataModel(BundleContext ctx) {
		this.ctx = ctx;
		updateNames();
		log.error("ServiceDatamodel initialized");
	}
	
	
	public void updateNames() {
		log.debug("Update names");
		try {
			refs = ctx.getAllServiceReferences(InstallationService.class.getName(), null);
			for(ServiceReference ref:refs) {
				String[] keys = ref.getPropertyKeys();
				for (String key:keys) { log.debug("Key: " + key);}
			}
					
		} catch (InvalidSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public int getRowCount() {
		updateNames();
		return refs.length;
	}
	

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ServiceReference ref = refs[rowIndex];
		if (ref != null) {
			switch (columnIndex) {
				case 0: return ref.getProperty("component.id");
				case 1: return ref.getProperty("component.name");
				case 2: return ref.getProperty("service.description");
				case 3: return ref.getProperty("service.vendor");
				case 4: return ref.getProperty("hoppla.identifier");
				default: return "";
			}
		}
		return "";
	}

}
