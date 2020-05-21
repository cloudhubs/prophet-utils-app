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
