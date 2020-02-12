package edu.baylor.ecs.cloudhubs.prophetapputils;

import edu.baylor.ecs.cloudhubs.prophetdto.app.ProphetAppData;
import edu.baylor.ecs.cloudhubs.prophetdto.app.ProphetAppMultiRepoRequest;
import edu.baylor.ecs.cloudhubs.prophetutils.ProphetUtilsFacade;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class JParserController {

    @PostMapping("/multirepoapp")
    public ProphetAppData getMultiRepoAppData(@RequestBody ProphetAppMultiRepoRequest request) throws IOException {
        ProphetAppData response = ProphetUtilsFacade.getProphetAppData(request);
        return response;
    }

}
