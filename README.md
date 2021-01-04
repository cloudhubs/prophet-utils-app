# Prophet
This is one component of the Prophet system.

- [React frontend: prophet-web](https://github.com/cloudhubs/prophet-web)
- REST API: prophet-utils-app (this project)
- [Backend functionality: prophet-utils](https://github.com/cloudhubs/prophet-utils)
- [Backend classes and DTOs: prophet-dto](https://github.com/cloudhubs/prophet-dto)
- [REST API Discovery: rad-source](https://github.com/cloudhubs/rad-source)
- [Bounded context creation: bounded-context](https://github.com/cloudhubs/bounded-context)

# Prophet Utils App
Request format: 

    {
    	"repositories": [
    		{
    			"path": "organizationName/repoName", // https://github.com/ will be prefixed onto this
    			"isMonolith": false
    		}
    	],
    	"systemName" : "tms"
    }
Response format:

    {
	    "global": {
	        "projectName": "name",
	        "communication": "graph TD\n ...",
	        "contextMap": "classDiagram\n\t ..."
	    },
	    "ms": [
	        {
	            "name": "ms-name",
	            "boundedContext": "classDiagram\n\t ..."
	        },
	        ...
	    ]
	}

Build prophet-dto and prophet-utils with `mvn clean install package -DskipTests`, then run this project with `mvn clean spring-boot:run -DskipTests`

## Build Docker Image

```
$ mvn clean install -DskipTests
$ docker build -t cloudhubs2/prophet-app-utils .
$ docker push cloudhubs2/prophet-app-utils
$ docker run -d -p 8081:8081 cloudhubs2/prophet-app-utils
```
