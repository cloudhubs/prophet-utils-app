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
