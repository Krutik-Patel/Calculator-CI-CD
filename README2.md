## **Project Overview**

This project demonstrates the integration of DevOps tools and workflows in developing, deploying, and monitoring a simple calculator application. The primary focus is on establishing a complete DevOps pipeline, showcasing every phase from local development to deployment in a production-like environment.

---

## **Objectives**

1. Set up a local development environment within an IDE.
2. Implement version control for effective tracking and collaboration.
3. Push the project to a remote Git repository.
4. Design and implement a CI/CD pipeline for automatic builds and tests on code updates.
5. Containerize the application and push it to a remote container registry.
6. Deploy the container using Jenkins for a streamlined and automated process.

---

## **Tools & Technologies**

- **Code Editor**: [IntelliJ IDEA Ultimate](https://www.jetbrains.com/idea/)
- **Version Control**: Git and GitHub
- **Build Tool**: Apache Maven
- **CI/CD**: Jenkins
- **Containerization**: Docker
- **Configuration Management**: Ansible
- **Build Automation**: Ngrok, GitHub Webhooks

---

## **Workflow**

1. **Development**: Using VS Code for coding and project management.
2. **Version Control**: Code is managed with Git, hosted on GitHub.
3. **Build**: Maven is used to compile and package the project.
4. **CI/CD**: Jenkins automates the process of testing and deployment.
5. **Containerization**: Docker is employed to ensure consistent deployments across environments.
6. **Configuration Management**: Ansible automates infrastructure setup and deployment.
7. **Build Automation**: Ngrok facilitates GitHub webhooks for triggering Jenkins builds.

---

## **Installation and Setup**

1. **Java**: Install with:
   ```bash
   sudo apt install default-jre
   sudo apt install default-jdk
   ```
   Verify with `java --version` and `javac --version`.

2. **VS Code**: Download from [VS Code](https://code.visualstudio.com/).

3. **Git**: Install using:
   ```bash
   sudo apt install git
   ```
   Verify with `git --version`.

4. **Maven**: Install using:
   ```bash
   sudo apt install maven
   ```
   Verify with `mvn --version`.

5. **Jenkins**: Install with:
   ```bash
   brew install jenkins
   ```
   Start Jenkins with `brew services start jenkins`.

6. **Docker**: Install Docker Desktop from [here](https://www.docker.com/products/docker-desktop), verify with `docker --version`.

7. **Ansible**: Install via:
   ```bash
   brew install ansible
   ```
   Verify with `ansible --version`.

8. **Ngrok**: Download from the official [Ngrok site](https://ngrok.com/), and set it up with:
   ```bash
   ngrok config add-authtoken <your_authtoken>
   ```

---

## **Project Implementation**

To start, create a new project in VS Code:

1. Open the command palette (`Ctrl + Shift + P`).
2. Select `Java: Create new project`.
3. Choose **Maven** as the build tool and select `maven-archetype-quickstart`.
4. Enter a project name, e.g., `com.calculator`, and choose your project folder.

*Example Image: "Insert 1.jpg here"*

### **Git Workflow**

Initialize a Git repository and push the project to GitHub using the following commands:

```bash
git init
git add .
git commit -m "Initial commit"
git remote add origin <repository-url>
git push -u origin main
```

---

## **Project Structure**

| Directory/File | Usage |
|----------------|-------|
| `calculator_app` | Main directory |
| `calculator_app/Deployment` | Files for Ansible deployment |
| `calculator_app/src` | Java code for the calculator app and tests |
| `calculator_app/src/main/java/com/calculator` | Calculator app's main code |
| `calculator_app/src/resources` | Configuration files (e.g., `log4j2.xml`) |
| `calculator_app/src/test/java` | Test files |
| `calculator_app/.gitignore` | Files/Directories to ignore for Git |
| `calculator_app/calculator_devops.log` | Log file |
| `calculator_app/Dockerfile` | Docker configuration |
| `calculator_app/pom.xml` | Maven configuration |
| `calculator_app/target` | Stores built JAR and class files |

*Example Image: "Insert 2.jpg here"*

---

## **Building and Running the Project**

### **pom.xml Configuration**

*Insert 3.jpg here*

Run the following commands to clean, compile, and install the project:

```bash
mvn clean     # Cleans built files
mvn compile   # Compiles the project
mvn install   # Creates the JAR file
```

Alternatively, run:

```bash
mvn clean install
```

This will clean, compile, and install in one step.

### **Running the JAR File**

Use the following command to run the JAR:

```bash
java -jar <filename>.jar
```

---

## **Logging and Testing**

For logging, use Log4j with the following import statements:

```java
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
```

Run the following command to execute tests and build the JAR:

```bash
mvn clean install
```

*Insert 5.jpg here*

---

## **Jenkins Pipeline**

To set up Jenkins, go to `http://localhost:8080` and install the necessary plugins.

*Insert 6.jpg here*

Here's an example Jenkins pipeline script:

```groovy
pipeline {
    environment {
        docker_image = ""
    }
    agent any
    tools {
        maven 'Maven 3.9.6'
    }
    stages {
        stage('Git Clone') {
            steps {
                git branch: 'main', url: 'https://github.com/Krutik-Patel/Calculator-CI-CD.git'
            }
        }
        stage('Maven Build') {
            steps {
                sh 'mvn clean install'
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    docker_image = docker.build "krutikpatel/calculator:latest"
                }
            }
        }
        stage('Push Docker Image to Hub') {
            steps {
                script {
                    docker.withRegistry('', 'DockerHubCred') {
                        docker_image.push()
                    }
                }
            }
        }
        stage('Clean Docker Images') {
            steps {
                script {
                    sh 'docker container prune -f'
                    sh 'docker image prune -f'
                }
            }
        }
        stage('Ansible Deployment') {
            steps {
                ansiblePlaybook inventory: 'Deployment/inventory', playbook: 'Deployment/deploy.yml'
            }
        }
    }
}
```

---

## **Docker Integration**

To create a Docker image for your project, use the following `Dockerfile`:

```Dockerfile
FROM openjdk:17
COPY ./target/calculator_app-1.0-SNAPSHOT-jar-with-dependencies.jar ./
WORKDIR ./
CMD ["java", "-jar", "calculator_app-1.0-SNAPSHOT-jar-with-dependencies.jar"]
```

*Insert 7.jpg here*

Verify the Docker image using:

```bash
sudo docker images
```

---

## **Pushing to DockerHub**

To push the Docker image to DockerHub, set up Jenkins credentials and push:

*Insert image 8.jpg*

---

## **Freeing Up Resources**

Clean up unused Docker containers and images with:

```bash
docker container prune -f
docker image prune -f
```

---

## **Pulling and Running Docker Containers with Ansible**

Create two files, `inventory` and `deploy.yml`, in the `Deployment` folder:

- **Inventory file**:

```ini
localhost ansible_connection=local ansible_user=krutik
```

- **Deploy.yml**:

```yaml
---
- name: Pull Docker Image of Calculator
  hosts: localhost
  tasks:
    - name: Pull Docker image
      docker_image:
        name: krutikpatel/calculator:latest
        source: pull
    - name: Start Docker service
      service:
        name: docker
        state: started
    - name: Run container
      shell: docker run -it -d --name Calculator krutikpatel/calculator
```

View Docker images with:

```bash
docker images
```

---

## **Automating Pipeline Execution with Ngrok**

*Insert images 11.jpg, 12.jpg*

Use Ngrok to expose Jenkins locally:

```bash
ngrok http 8080
```

This URL can be used to set up a GitHub webhook that automatically triggers builds on new commits.

---

This revision improves readability, organization, and adds clarity to your technical steps. You can now insert your images at the designated points for a more visually cohesive document.