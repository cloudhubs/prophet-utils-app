package edu.baylor.ecs.cloudhubs.prophetapputils;

import edu.baylor.ecs.cloudhubs.prophetdto.app.ProphetAppData;
import edu.baylor.ecs.cloudhubs.prophetdto.app.ProphetAppMultiRepoRequest;
import edu.baylor.ecs.cloudhubs.prophetdto.app.ProphetAppRequest;
import edu.baylor.ecs.cloudhubs.prophetutils.ProphetUtilsFacade;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.URIish;
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

    @PostMapping("/")
    public ProphetAppData getMultiRepoAppData(@RequestBody ProphetAppMultiRepoRequest request) {
        String dirName = "repos-" + LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
        File dir = new File(dirName);
        boolean result = dir.mkdirs();
        if (!result) {
            return null;
        }
        ProphetAppData data = new ProphetAppData();

        try {
            List<ProphetAppRequest> localRepos = new ArrayList<>();
            for (ProphetAppRequest repo : request.getRepositories()) {
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
                    System.out.println("Exception occurred while cloning repo");
                    e.printStackTrace();
                }
            }
            request.setRepositories(localRepos);
            data = ProphetUtilsFacade.getProphetAppData(request);
        } catch(Exception e) {
            System.out.println("Failed to process project!");
        } finally {
            try {
                FileUtils.deleteDirectory(dir);
            } catch (IOException e) {
                System.out.println("Failed to delete project directory!");
            }
        }

        return data;
    }
}
