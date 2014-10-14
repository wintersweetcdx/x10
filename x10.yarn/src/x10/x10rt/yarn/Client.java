/*
 *  This file is part of the X10 project (http://x10-lang.org).
 *
 *  This file is licensed to You under the Eclipse Public License (EPL);
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *      http://www.opensource.org/licenses/eclipse-1.0.php
 *
 *  (C) Copyright IBM Corporation 2014.
 */

package x10.x10rt.yarn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.SimpleLog;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.yarn.api.ApplicationConstants;
import org.apache.hadoop.yarn.api.ApplicationConstants.Environment;
import org.apache.hadoop.yarn.api.protocolrecords.GetNewApplicationResponse;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.ApplicationSubmissionContext;
import org.apache.hadoop.yarn.api.records.ContainerLaunchContext;
import org.apache.hadoop.yarn.api.records.FinalApplicationStatus;
import org.apache.hadoop.yarn.api.records.LocalResource;
import org.apache.hadoop.yarn.api.records.LocalResourceType;
import org.apache.hadoop.yarn.api.records.LocalResourceVisibility;
import org.apache.hadoop.yarn.api.records.NodeReport;
import org.apache.hadoop.yarn.api.records.NodeState;
import org.apache.hadoop.yarn.api.records.QueueACL;
import org.apache.hadoop.yarn.api.records.QueueInfo;
import org.apache.hadoop.yarn.api.records.QueueUserACLInfo;
import org.apache.hadoop.yarn.api.records.Resource;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;
import org.apache.hadoop.yarn.api.records.YarnClusterMetrics;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.client.api.YarnClientApplication;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.ApplicationNotFoundException;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.util.ConverterUtils;

public class Client {

	private static final Log LOG = new SimpleLog(Client.class.getName());
	
	private Configuration conf;
	private YarnClient yarnClient;
	
	// Application Master configuration details
	private final String appName;
	private final String amQueue = "default";
	private final int amMemory = 500;
	private final int amVCores = 1;
	private final String appMasterMainClass;
	private final String[] args;

	public Client(String[] args) {
		this.conf = new YarnConfiguration();
		//this.conf.set(YarnConfiguration.RM_HOSTNAME, args[0]);
		//this.conf.setInt(YarnConfiguration.RESOURCEMANAGER_CONNECT_MAX_WAIT_MS, 1000);
		//this.conf.setInt(YarnConfiguration.RESOURCEMANAGER_CONNECT_RETRY_INTERVAL_MS, 1000);
		//this.conf.set(CommonConfigurationKeysPublic.FS_DEFAULT_NAME_KEY, "hdfs://"+args[0]);
		this.appMasterMainClass = ApplicationMaster.class.getName();
		this.yarnClient = YarnClient.createYarnClient();
		this.yarnClient.init(this.conf);
		this.args = args;
		int lastslash = args[0].lastIndexOf('/');
		if (lastslash == -1)
			this.appName = args[0];
		else
			this.appName = args[0].substring(lastslash+1);
	}
	
	public static void main(String[] args) {
		try {
			LOG.info("Initializing Client");
			Client client = new Client(args);
			client.run();
		}
		catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}
	
	public void run() throws IOException, YarnException {
		yarnClient.start();
		YarnClusterMetrics clusterMetrics = yarnClient.getYarnClusterMetrics();
		
		// print out cluster information
		LOG.info("Got Cluster metric info from ASM, numNodeManagers=" + clusterMetrics.getNumNodeManagers());
		List<NodeReport> clusterNodeReports = yarnClient.getNodeReports(NodeState.RUNNING);
		LOG.info("Got Cluster node info from ASM");
		for (NodeReport node : clusterNodeReports) {
			LOG.info("Got node report from ASM for"
					+ ", nodeId=" + node.getNodeId()
					+ ", nodeAddress" + node.getHttpAddress()
					+ ", nodeRackName" + node.getRackName()
					+ ", nodeNumContainers" + node.getNumContainers());
		}
		QueueInfo queueInfo = yarnClient.getQueueInfo(this.amQueue);
		LOG.info("Queue info"
				+ ", queueName=" + queueInfo.getQueueName()
				+ ", queueCurrentCapacity=" + queueInfo.getCurrentCapacity()
				+ ", queueMaxCapacity=" + queueInfo.getMaximumCapacity()
				+ ", queueApplicationCount=" + queueInfo.getApplications().size()
				+ ", queueChildQueueCount=" + queueInfo.getChildQueues().size());	
		List<QueueUserACLInfo> listAclInfo = yarnClient.getQueueAclsInfo();
		for (QueueUserACLInfo aclInfo : listAclInfo) {
			for (QueueACL userAcl : aclInfo.getUserAcls()) {
				LOG.info("User ACL Info for Queue"
						+ ", queueName=" + aclInfo.getQueueName()	
						+ ", userAcl=" + userAcl.name());
			}
		}
		
		

		// Get a new application id
		YarnClientApplication app = yarnClient.createApplication();
		GetNewApplicationResponse appResponse = app.getNewApplicationResponse();

		int maxMem = appResponse.getMaximumResourceCapability().getMemory();
		LOG.info("Max mem capabililty of resources in this cluster " + maxMem);
		
		int maxVCores = appResponse.getMaximumResourceCapability().getVirtualCores();
		LOG.info("Max virtual cores capabililty of resources in this cluster " + maxVCores);
		
		// set the application name
		ApplicationSubmissionContext appContext = app.getApplicationSubmissionContext();
		final ApplicationId appId = appContext.getApplicationId();
		appContext.setKeepContainersAcrossApplicationAttempts(false);
		appContext.setApplicationName(appName);
		
		Map<String, LocalResource> localResources = new HashMap<String, LocalResource>();
		LOG.info("Copy App Master jar from local filesystem and add to local environment");
		// Copy the application master jar to the filesystem
		// Create a local resource to point to the destination jar path
		FileSystem fs = FileSystem.get(conf);
		StringBuilder x10jars = new StringBuilder();
		
		boolean isNative = Boolean.getBoolean(ApplicationMaster.X10_YARN_NATIVE);
		String[] jarfiles = System.getProperty("java.class.path").split(":");
		// upload jar files
		String hazelcastjar = System.getenv("HAZELCAST_JAR");
		for (String jar: jarfiles) {
			if (jar.endsWith(".jar")) {
				String nopath = jar.substring(jar.lastIndexOf('/')+1);
				LOG.info("Uploading "+nopath+" to "+fs.getUri());
				x10jars.append(addToLocalResources(fs, jar, nopath, appId.toString(), localResources, null));
				if (isNative) {
					// add the user's program.
					LOG.info("Uploading "+appName+" to "+fs.getUri());
					x10jars.append(':');
					x10jars.append(addToLocalResources(fs, args[0], appName, appId.toString(), localResources, null));
					break; // no other jar files are needed beyond the one holding ApplicationMaster, which is the first one
				} else if (jar.endsWith(hazelcastjar)) // last Managed X10 jar file.  The rest are hadoop jars 
					break;
				else
					x10jars.append(':');
			}
		}
		
		LOG.info("Set the environment for the application master");
		Map<String, String> env = new HashMap<String, String>();
		
		String places = System.getenv(ApplicationMaster.X10_NPLACES);
		env.put(ApplicationMaster.X10_NPLACES, (places==null)?"1":places);
		String cores = System.getenv(ApplicationMaster.X10_NTHREADS);
		env.put(ApplicationMaster.X10_NTHREADS, (cores==null)?"0":cores);
		env.put(ApplicationMaster.X10_HDFS_JARS, x10jars.toString());
		env.put(ApplicationMaster.X10_MAX_MEMORY, Long.toString(Runtime.getRuntime().maxMemory()/1024/1024));

		// At some point we should not be required to add
		// the hadoop specific classpaths to the env.
		// It should be provided out of the box.
		// For now setting all required classpaths including
		// the classpath to "." for the application jar
		StringBuilder classPathEnv = new StringBuilder(Environment.CLASSPATH.$$()).append(ApplicationConstants.CLASS_PATH_SEPARATOR).append("./*");
		for (String c : conf.getStrings(YarnConfiguration.YARN_APPLICATION_CLASSPATH,
				YarnConfiguration.DEFAULT_YARN_CROSS_PLATFORM_APPLICATION_CLASSPATH)) {
			classPathEnv.append(ApplicationConstants.CLASS_PATH_SEPARATOR);
			classPathEnv.append(c.trim());
		}
		env.put("CLASSPATH", classPathEnv.toString());
		
		LOG.info("Completed setting up the ApplicationManager environment " + env.toString());

		// Set the necessary command to execute the application master
		Vector<CharSequence> vargs = new Vector<CharSequence>(this.args.length + 30);
		// Set java executable command
		LOG.info("Setting up app master command");
		vargs.add(Environment.JAVA_HOME.$$() + "/bin/java");
		// Set Xmx based on am memory size
		vargs.add("-Xmx" + amMemory + "m");
		// propigate the native flag
		if (isNative) vargs.add("-DX10_YARN_NATIVE=true");
		// Set class name
		vargs.add(appMasterMainClass);
		// add remaining command line arguments
		for (int i=0; i<args.length; i++)
			vargs.add(args[i]);
		
		vargs.add("1>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/AppMaster.stdout");
		vargs.add("2>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/AppMaster.stderr");
		// Get final commmand
		StringBuilder command = new StringBuilder();
		for (CharSequence str : vargs) {
			command.append(str).append(" ");
		}
		LOG.info("Completed setting up app master command " + command.toString());	
		List<String> commands = new ArrayList<String>();
		commands.add(command.toString());
		

		// Set up the container launch context for the application master
		ContainerLaunchContext amContainer = ContainerLaunchContext.newInstance(
				localResources, env, commands, null, null, null);
		// Set up resource type requirements
		// For now, both memory and vcores are supported, so we set memory and
		// vcores requirements
		Resource capability = Resource.newInstance(amMemory, amVCores);
		appContext.setResource(capability);
		
		appContext.setAMContainerSpec(amContainer);
		
		// kill the application if the user hits ctrl-c
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
	    {
	        @Override
	        public void run()
	        {
	            System.out.println();
	            System.out.println("Exiting...");
	            forceKillApplication(appId);
	        }
	    }));
		
		LOG.info("Submitting application to ASM");
		yarnClient.submitApplication(appContext);

		// Monitor the application
		monitorApplication(appId);
		
		// delete jar files uploaded earlier
		cleanupLocalResources(fs, appId.toString());
	}
	

	/**
	 * Monitor the submitted application for completion.
	 * Kill application if time expires.
	 * @param appId Application Id of application to be monitored
	 * @return true if application completed successfully
	 * @throws YarnException
	 * @throws IOException
	 */
	private boolean monitorApplication(ApplicationId appId) throws YarnException, IOException {
		YarnApplicationState previousState = null;
		while (true) {
			// Check app status every 1 second.
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				LOG.debug("Thread sleep in monitoring loop interrupted");
			}
			// Get application report for the appId we are interested in
			ApplicationReport report = yarnClient.getApplicationReport(appId);
			YarnApplicationState state = report.getYarnApplicationState();
			if (!state.equals(previousState)) {
				previousState = state;
				LOG.info("Got application report from ASM for"
						+ ", appId=" + appId.getId()
						+ ", clientToAMToken=" + report.getClientToAMToken()
						+ ", appDiagnostics=" + report.getDiagnostics()
						+ ", appMasterHost=" + report.getHost()
						+ ", appQueue=" + report.getQueue()
						+ ", appMasterRpcPort=" + report.getRpcPort()
						+ ", appStartTime=" + report.getStartTime()
						+ ", yarnAppState=" + report.getYarnApplicationState().toString()
						+ ", distributedFinalState=" + report.getFinalApplicationStatus().toString()
						+ ", appTrackingUrl=" + report.getTrackingUrl()
						+ ", appUser=" + report.getUser());
			}
			FinalApplicationStatus dsStatus = report.getFinalApplicationStatus();
			if (YarnApplicationState.FINISHED == state) {
				if (FinalApplicationStatus.SUCCEEDED == dsStatus) {
					LOG.info("Application has completed successfully. Breaking monitoring loop");
					return true;
				}
				else {
					LOG.info("Application finished unsuccessfully."
							+ " YarnState=" + state.toString() + ", DSFinalStatus=" + dsStatus.toString()
							+ ". Breaking monitoring loop");
					return false;
				}	
			}
			else if (YarnApplicationState.KILLED == state	
					|| YarnApplicationState.FAILED == state) {
				LOG.info("Application did not finish."
						+ " YarnState=" + state.toString() + ", DSFinalStatus=" + dsStatus.toString()
						+ ". Breaking monitoring loop");
				return false;
			}
		}
	}
	

	private void forceKillApplication(ApplicationId  appId) {
		try {
			yarnClient.killApplication(appId);
		} catch (ApplicationNotFoundException e){
			// ignore
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String addToLocalResources(FileSystem fs, String fileSrcPath, String fileDstPath, String appId, 
			Map<String, LocalResource> localResources, String resources) throws IOException {
		String suffix = appName + "/" + appId + "/" + fileDstPath;
		Path dst = new Path(fs.getHomeDirectory(), suffix);
		if (fileSrcPath == null) {
			FSDataOutputStream ostream = null;
			try {
				ostream = FileSystem.create(fs, dst, new FsPermission((short) 0710));
				ostream.writeUTF(resources);
			} finally {
				IOUtils.closeQuietly(ostream);
			}
		} else {
			fs.copyFromLocalFile(new Path(fileSrcPath), dst);
		}
		FileStatus scFileStatus = fs.getFileStatus(dst);
		LocalResource scRsrc = LocalResource.newInstance(
						ConverterUtils.getYarnUrlFromURI(dst.toUri()),
						LocalResourceType.FILE, LocalResourceVisibility.APPLICATION,
						scFileStatus.getLen(), scFileStatus.getModificationTime());
		localResources.put(fileDstPath, scRsrc);
		return ("/user/"+System.getProperty("user.name")+'/'+suffix);
	}
	
	private void cleanupLocalResources(FileSystem fs, String appId) throws IOException {
		String suffix = appName + "/" + appId + "/";
		Path dst = new Path(fs.getHomeDirectory(), suffix);
		fs.delete(dst, true);
	}
}