### Project Overview

This project focuses on using DevOps tools and workflows to develop, deploy, and monitor a simple calculator program. The emphasis is on the DevOps pipeline, covering the complete process from local development to deployment.

### Objectives

1. Set up a local project in an IDE.
2. Use version control for tracking and collaboration.
3. Push the project to a remote Git repository.
4. Create a CI/CD pipeline to automatically build and test the project on code updates.
5. Containerize the project and push it to a remote container registry.
6. Deploy the container using Jenkins.

### Tools

- *Code Editor*: IntelliJ IDEA Ultimate
- *Version Control*: Git and GitHub
- *Build Tool*: Apache Maven
- *CI/CD*: Jenkins
- *Containerization*: Docker
- *Configuration Management*: Ansible
- *Build Automation*: Ngrok, GitHub webhooks

### Workflow

1. *Development*: VS Code.
2. *Version Control*: Manage code with Git, hosted on GitHub.
3. *Build*: Use Maven to compile and package the project.
4. *CI/CD*: Jenkins automates tests and deployments.
5. *Containerization*: Use Docker for consistent deployments.
6. *Configuration Management*: Ansible automates infrastructure setup.
7. *Build Automation*: Ngrok facilitates GitHub webhooks triggering Jenkins builds.

### Installation and Setup

- *Java*: Install via sudo apt install default-jre and sudo apt install default-jre, verify with java --version and javac --version.
- *VS Code*: Download from [VS Code](https://code.visualstudio.com/)site and follow the installation steps.
- *Git*: Install with sudo install git, verify with git --version.
- *Maven*: Install via sudo apt install maven, verify with mvn --version.
- *Jenkins*: Install with brew install jenkins, start Jenkins with brew services start jenkins.
- *Docker*: Download and install Docker Desktop, verify with docker --version.
- *Ansible*: Install via brew install ansible, verify with ansible --version.
- *Ngrok*: Download binaries from the official site, set up with ngrok config add-authtoken <your_authtoken>. 

This setup allows seamless integration and deployment using DevOps tools.

### Project Implementation

To begin our journey, we'll create a new project in VS Code using Ctrl + Shift + P and type Java: Create new project. In that choose maven as a build tool and choose maven-archetype-quickstart and select a version. In the next dialogue box, enter the name of your project, like com.calculator, as shown in "Insert 1.jpg here". Enter the artifact Id and select your destination folder where you want to start your project. You'll now have a calculator directory with some directory structure.

#### Git Workflow

Commands used to initialise a new Git repository, add files to the staging area, commit changes, set up a remote repository on GitHub, and push the changes to the remote repository. git init git add . git commit -m “” git status git remote add origin git push -u origin main Enter username and PAT (Personal Access Token).

#### Project Structure

Insert 2.jpg here

Make a table of 

	--
	Directory/File | Usage | 
	calculator_app | main directory |
	 calculator_app/Deployment | files for ansible |
	 calculator_app/src | contains java code for calculator app and testing java file | 
	 calculator_app/src/main/java/com/calculator | has java code for calculator app |
	 calculator_app/src/resources | Has configuration files for log4j2.xml dependency |
	 calculator_app/src/test/java | Has testing java file |
	 calculator_app/.gitignore | Files/Directories to be ignored by git |
	 calculator_app/calculator_devops.log | Log file for the project |
	 calculator_app/Dockerfile | Config file for Docker |
	 calculator_app/pom.xml | Config file for maven |
	 calculator_app/target | Stores built JAR and class files |

#### Steps to Build and Run the project

##### pom.xml

Insert 3.jpg

-- Assuming the java code has some starter code, to build the app, do `mvn clean`. This cleans all the built files and JARs. Do `mvn compile`, this compiles the project and associated test cases. This phase ensures the code is error free and ready for the subsequent steps.

-- `mvn install`: this generates the JAR file. This final step packages the project, creating the desired output artifact once the compilation is successful.

-- Alternatively you can do `mvn clean install`, as a single step that does all of the above simultaneously.

##### Running build JAR file

-- Run the JAR using `java -jar <filename>.jar`. Our pom.xml generates two JARs, one without dependencies and the other with dependiencies.

Within the Maven configuration file (pom.xml), the <mainClass> tag specifies
the path to the main Java file following the package structure. Additionally, the
<descriptorRef> tag is employed to modify the default output JAR file name.
To include project dependencies, the <dependencies> tag is utilised, enabling
the addition of external libraries. In our scenario, we've incorporated
dependencies such as log4j, utilised for logging functionality, and JUnit,
employed for testing purposes.

##### Implement Logging and write Tests

The specified logging format includes the following components:
"%d{yyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n
%d{yyy-MM-dd HH:mm:ss.SSS}: Represents the timestamp with millisecond
precision.
%t: Indicates the running thread's name.
9
Mini Project - Simple Calculator - Report IMT2020035
%-5level: Denotes the log level with left alignment and a maximum width of 5
characters.
%logger{36}: Refers to the logger's name within a maximum of 36 characters.
%msg: Represents the user-written message contained in the source code.
The necessary imports for Log4j

`import org.apache.logging.log4j.LogManager;`
`import org.apache.logging.log4j.Logger;`

Creating an instance of the Logger in Log4j involves using a statement to
initialise a Logger object within the code.

Do `mvn clean install`, this will run tests and build the JAR. It should look something like this: Insert 5.jpg here


##### Jenkins

Go to https://localhost:8080 and install the necessary plugins.
We will establish a Jenkins pipeline comprising six distinct stages, as outlined
below:

Insert 6.jpg here

``` Jenkins
pipeline{
    environment{
    docker_image = ""
    }
    agent any
    tools {
        maven 'Maven 3.9.6'
    }
    stages{
        stage('Stage 1: Git Clone'){
            steps{
            git branch: 'master',
            
            url:'https://github.com/Krutik-Patel/Calculator-CI-CD.git'
            }
        }
        stage('Stage 2: Maven Build') {
            steps {
                sh 'mvn clean install'
            }
        }
        stage('Stage 3: Build Docker Image'){
            steps{
                script{
                    docker_image = docker.build "krutikpatel/calculator:latest"
                }
            }
        }
        stage('Stage 4: Push docker image to hub') {
            steps{
                script{
                    docker.withRegistry('', 'DockerHubCred'){
                        docker_image.push()
                    }
                }
            }
        }
        stage('Stage 5: Clean docker images'){
            steps{
                script{
                    sh 'docker container prune -f'
                    sh 'docker image prune -f'
                }
            }
        }
        stage('Step 6: Ansible Deployment'){
            steps{
                ansiblePlaybook becomeUser: null,
                colorized: true,
                credentialsId: 'localhost',
                disableHostKeyChecking: true,
                installation: 'Ansible',
                inventory: 'Deployment/inventory',
                playbook: 'Deployment/deploy.yaml',
                sudoUser: null
            }
        }
    }
}
```

###### Overview
Stage 1: Git Clone

Purpose: This stage clones the specified Git repository from the master branch.
Steps:
Uses the git step to clone the repository from the URL https://github.com/Krutik-Patel/Calculator-CI-CD.git.
Stage 2: Maven Build

Purpose: This stage builds the project using Maven.
Steps:
Executes the Maven command mvn clean install to clean and build the project.
Stage 3: Build Docker Image

Purpose: This stage builds a Docker image for the application.
Steps:
Uses the docker.build method to build a Docker image with the tag krutikpatel/calculator:latest.
Stage 4: Push Docker Image to Hub

Purpose: This stage pushes the built Docker image to Docker Hub.
Steps:
Uses docker.withRegistry to authenticate with Docker Hub using the credentials DockerHubCred.
Pushes the Docker image to the repository.
Stage 5: Clean Docker Images

Purpose: This stage cleans up unused Docker containers and images to free up space.
Steps:
Executes docker container prune -f to remove all stopped containers.
Executes docker image prune -f to remove all unused images.
Stage 6: Ansible Deployment

Purpose: This stage deploys the application using Ansible.
Steps:
Uses the ansiblePlaybook step to run the Ansible playbook Deployment/deploy.yaml with the specified inventory file Deployment/inventory.
Various options are set, such as colorized output and disableHostKeyChecking.

##### Docker

Now, we will create a docker container for our project. For this, we need a
Dockerfile.
The purpose of the above code is to generate a Docker container with
OpenJDK version 11 as the base image which acts like a JVM. The final
container can be run independently without any dependencies.
In the pipeline script, docker_image = docker.build “krutikpatel/calculator:latest”
builds the docker image.

``` Docker
FROM openjdk:17
COPY ./target/calculator_app-1.0-SNAPSHOT-jar-with-dependencies.jar ./
WORKDIR ./
CMD ["java","-jar","calculator_app-1.0-SNAPSHOT-jar-with-dependencies.jar"]
```

Insert 7.jpg here

```
sudo docker images
```

The provided command allows verification of the successful creation of a
Docker image.



##### Pushing to DockerHub

Stage 4 focuses on pushing the image to docker hub. To achieve this, set up a credential in
Jenkins, containing the Jenkins username and password for authentication.

Insert image 8.jpg

##### Freeing up dangling pointers:

To free up space and we’ll delete the unused docker containers and
images. 
``` bash
docker container prune -f
docker image prune -f
```

##### Pulling Image from Dockerhub and create container using Ansible

We are going to pull the images from the DockerHub and create
containers using Ansible. We will create a Deployment folder and create two
files named inventory and deploy.yml.
Inventory file
```ini
localhost ansible_connection=local ansible_user=krutik
```


Deploy.yml file
```yaml
---
- name: Pull Docker Image of Calculator
  hosts: localhost
  vars:
    ansible_python_interpreter: /usr/bin/python3
  tasks:
    - name: Pull image
      docker_image:
        name: krutikpatel/calculator:latest
        source: pull
    - name: Start docker service
      service:
        name: docker
        state: started
    - name: Running container
      shell: docker run -it -d --name Calculator krutikpatel/calculator
```


Run the following command to view the list of images;
`docker images`

Now, run the following command to create a container from a image;

`docker run -it -d --name Calculator krutikpatel/calculator:latest`
To view the containers run the following command;
`docker ps`
Now, to execute the container;
`docker start -a -i Calculator`

Insert image 9.jpg and 10.jpg


##### Automating Pipeline Execution with Git SCM Polling and Ngrok

Insert image 11.jpg, 12.jpg, 


Open a terminal window and enter the command `ngrok http 8080`. This
command will establish an HTTP tunnel using Ngrok, exposing the local server
running on port 8080 to the internet.
Copy the forwarding URL provided by Ngrok. Subsequently, create a GitHub
webhook and utilise this URL as the payload URL for the webhook
configuration. GitHub initiates a test connection, and upon successful
configuration, a '200 OK' message confirms the proper setup.
Now, we'll update the Jenkins URL to the forwarding URL obtained and
configure a build trigger for Git SCM polling. This setup ensures that our
pipeline automatically initiates the build process whenever Jenkins detects a
new push made to the associated GitHub repository. You can check by pushing some code in the github repo.