IMAGE_TAG=ebi-person-service:latest
jar:
	mvn clean install

app-run:
	mvn spring-boot:run

docker-build:
	docker build -f ./Dockerfile -t ${IMAGE_TAG} .

docker-run:
	docker run --rm -p8080:8080 -it ${IMAGE_TAG}

docker-compose-run:
	docker-compose up --build