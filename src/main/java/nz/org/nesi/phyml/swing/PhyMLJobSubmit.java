package nz.org.nesi.phyml.swing;

import grisu.control.ServiceInterface;
import grisu.frontend.control.jobMonitoring.RunningJobManager;
import grisu.frontend.model.job.GrisuJob;
import grisu.model.FileManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import phyml.StandardOutPanel;

public class PhyMLJobSubmit {

	private final ServiceInterface si;
	private final String command;
	private final String file;
	
	public PhyMLJobSubmit(ServiceInterface si, String command, String file) {
		this.si = si;
		this.command = command;
		this.file = file;
	}
	
	public void submit() throws Exception {
		
		GrisuJob job = new GrisuJob(si);
		
		
		job.setApplication("phyml");
		job.setApplicationVersion("20121208");
		job.setSubmissionLocation("pan:pan.nesi.org.nz");
		
//		job.setCommandline("phyml-mpi "+command);
		job.setCommandline("/share/apps/phyml-20120412-patch-20121208/bin/phyml-mpi "+command);
		job.addInputFileUrl(file);
		
		job.setCpus(20);
		job.setForce_mpi(true);
		try {
			job.setWalltime("10m");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		job.setTimestampJobname(FileManager.getFilename(file));
		
		PropertyChangeListener l = new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				
				String propName = evt.getPropertyName();
				if ("submissionLog".equals(propName)) {
					final List<String> log = (List<String>) evt.getNewValue();
					String text = log.get(log.size() - 1);
					StandardOutPanel.setInput(text);
				}
				
			}
		};
		
		job.addPropertyChangeListener(l);
		
		try {
		RunningJobManager.getDefault(si).createJob(job, "/nz/nesi");
		
		job.submitJob();
		
		} finally {
			job.removePropertyChangeListener(l);
		}
		
		
		
		
	}

}
