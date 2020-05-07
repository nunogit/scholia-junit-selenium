package opendata.scholia.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.text.SimpleDateFormat;

import org.apache.commons.configuration2.Configuration;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;



public class GitWriter {
	
	private static final String  TEMP_GIT_FOLDER = "/tmp/tempRepo";

//	"https://github.com/nunogit/scholia-junit-selenium-log.git"
	
	public static void write(String path, String file, String content) throws IllegalStateException, GitAPIException, URISyntaxException {
		
	    String username = ConfigManager.instance().getConfig().getString("github.username");
	    String password = ConfigManager.instance().getConfig().getString("github.password");
	    String repository = ConfigManager.instance().getConfig().getString("github.repository");
	    
		
		// it would be intersting to explore virtual file systems, to avoid using /tmp/
		// tried google jimfs but it doesn't support the io.File semantics
		
	    //Git git = Git.open(new File("/tmp/teste"));
	    Git git = Git.init().setDirectory(new File(TEMP_GIT_FOLDER) ).call();
	    //Git git = Git.cloneRepository().setURI("https://github.com/nunogit/scholia-junit-selenium-log.git")
	    //		     .setDirectory(new File("/tmp/test"))
	    //		     .call();
	    		
	    // add remote repo:
	    RemoteAddCommand remoteAddCommand = git.remoteAdd();
	    remoteAddCommand.setName("origin");
	    remoteAddCommand.setUri(new URIish(repository));
	    remoteAddCommand.call();
	    
	    PullCommand pullCommand = git.pull();
	    pullCommand.call();
	    
	    System.out.println("path "+TEMP_GIT_FOLDER+path);
	    System.out.println("file "+file);
	    DiskWriter.write(TEMP_GIT_FOLDER+path, file, content, false);
	    
	    git.add().addFilepattern(file).call();
	    git.commit().setMessage( "adding file" ).call();
	    
	    // push to remote:
	    PushCommand pushCommand = git.push();
	    

	    
	    pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password));
	    // you can add more settings here if needed
	    Iterable<PushResult>prList = pushCommand.call();
	    for(PushResult pr: prList){
	    	System.out.println( pr.getMessages() );
	    }
	}
	
    public static void main(String[] args) throws IOException, GitAPIException, URISyntaxException {
		System.out.println("Writing to git...");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
		long timestamp = System.currentTimeMillis();
		String sTimestamp = sdf.format(timestamp);
    	GitWriter gw = new GitWriter();
    	System.out.println(sTimestamp);
    	gw.write("/", "hellor-"+sTimestamp, "hello2");
    }
}
