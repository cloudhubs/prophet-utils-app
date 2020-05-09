package edu.baylor.ecs.cloudhubs.prophetapputils;

import edu.baylor.ecs.cloudhubs.prophetdto.app.ProphetAppData;
import edu.baylor.ecs.cloudhubs.prophetdto.app.ProphetError;
import edu.baylor.ecs.cloudhubs.prophetdto.app.utilsapp.GitReq;
import edu.baylor.ecs.cloudhubs.prophetdto.app.utilsapp.RepoReq;
import edu.baylor.ecs.cloudhubs.prophetutils.ProphetUtilsFacade;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.URIish;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ProphetUtilsController {

    /**
     * Append to start of passed path to retrieve path.
     * Expand later to include other code sources
     */
    private final String repoPrefix = "https://github.com/";

    Logger logger = LoggerFactory.getLogger(ProphetUtilsController.class);

    @PostMapping("/")
    public ResponseEntity getMultiRepoAppData(@RequestBody GitReq request) {
        String dirName = "repos-" + LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
        File dir = new File(dirName);
        boolean result = dir.mkdirs();
        if (!result) {
            return null;
        }
        ProphetAppData data = new ProphetAppData();

        try {
            List<RepoReq> localRepos = new ArrayList<>();
            for (RepoReq repo : request.getRepositories()) {
                String repoUrl = repoPrefix + repo.getPath();
                String repoName = new URIish(repoUrl).getHumanishName();
                String repoDirName = dirName + "/" + repoName;
                File repoDir = new File(repoDirName);
                result = repoDir.mkdir();
                if (!result) {
                    return null;
                }

                try {
                    System.out.println("Cloning " + repoUrl + " into " + repoDirName);
                    Git.cloneRepository()
                            .setURI(repoUrl)
                            .setDirectory(Paths.get(repoDirName).toFile())
                            .call();
                    System.out.println("Completed Cloning");
                    repo.setPath(repoDir.getCanonicalPath());
                    localRepos.add(repo);
                } catch (GitAPIException e) {
                    logger.error("Failed to clone repository. " + e.toString());
                    e.printStackTrace();
                    ProphetError error = new ProphetError();
                    error.setError(true);
                    error.setMessage("Failed to clone repository. " + e.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
                }
            }
            request.setRepositories(localRepos);
            data = ProphetUtilsFacade.getProphetAppData(request);
            logger.info("Finished processing project");
        } catch(Exception e) {
            logger.error("Failed to process project!\n" + e.getMessage());
            e.printStackTrace();
            ProphetError error = new ProphetError();
            error.setError(true);
            error.setMessage("Failed to process project. " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } finally {
            try {
                FileUtils.deleteDirectory(dir);
            } catch (IOException e) {
                logger.error("Failed to delete project directory.");
                e.printStackTrace();
            }
        }

        return ResponseEntity.ok(data);
    }
}
