# This example is taken from below video
# https://www.youtube.com/watch?v=3n_pqQcih5c

# To create an image from this docker file use below command
# -t is a tag name(image name), '.' is a DockerFile path.
# docker build -t samplespringbootapplication .
# use "docker images" to see created image
# to run a container
# docker run -it --rm -p 8085:8080 --name=samplespringbootapplication1 samplespringbootapplication
# -rm means remove a container on exit
# -p 8085:8080 means publish container's 8080 port for host machine's 8085 port.
# --name is a name of container
# if you use 'docker ps -a', you will see PORTs=0.0.0.0:8085->8080/tcp
# Then try http://0.0.0.0:8085/hello/home or http://localhost:8085/hello/home or http://127.0.0.1:8085/hello/home

FROM java:8
# The EXPOSE instruction does not actually publish the port. It functions as a type of documentation between the person who builds the image and the person who runs the container, about which ports are intended to be published.
# EXPOSE 8080 is used to make two containers communicate with each other. you can read self created docker document to see how two applicaitons in two different containers can communicate with each other.
# application is running on 8080, you expose it to host machine's 8085 port
# normally, you should use -p parameter to publish host's port. You can read about EXPOSE vs -p in self created docker document.
EXPOSE 8085:8080 # 8085 has no effect here. it is just for the documentation to tell a person who runs a container that use 8085 with -p
# copy jar file of your application into docker image
ADD /target/SampleSpringBoot.jar SampleSpringBoot.jar
# when a container is run, it will run this command
ENTRYPOINT ["java","-jar","SampleSpringBoot.jar"]
