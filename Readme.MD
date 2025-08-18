## To upload index to elasticsearch
#### Keep elastic search running on localhost port 9200 before running the app.
#### Create the airports index manually with the json code in `airports_index.html` file in resources/static file. Also available at `http://localhost:8080/airports_index.html`.
#### Upload the csv file with airport details with field name `file` in postman form data and hit the `index/bulk-upload` endpoint with "POST" request.

## Build docker image of project

#### Run this in the root directory `docker build -t rituraj49/lucene-demo:latest .`. It will build the image using the Dockerfile
#### Push the image to docker hub. `docker push rituraj49/lucene-demo`
#### To run the docker project with keycloak and postgres run `docker compose up`