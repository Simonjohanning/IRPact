# IRPact
IRPact is a prototype for a framework for agent-based modeling in innovation diffusion research that is part of the IRPsim project at Leipzig University, itself being an informal part of the [Smart Utilities and Sustainable Infrastructure Change (SUSIC)](https://www.wifa.uni-leipzig.de/iirm/energiemanagement/forschung/projekte/susic.html) project as part of [Leipzig Smart Infrastructure Hub](https://smartinfrastructurehub.com/).

It is based on [a thesis](https://doi.org/10.13140/RG.2.2.27912.78082), [a review paper](https://doi.org/10.13140/RG.2.2.29711.94887) and [a vision paper](https://doi.org/10.13140/RG.2.2.23001.06248), has been principally developed between 2016 and 2018, and is to be reengineered within the scope of the SUSIC project between 2020 and 2021.

## Motivation
The adoption of eco-innovation is seen as a promising approach in supporting to reach emission goals. 
Yet, even promising products fail to diffuse in consumer households.
Agent-based innovation diffusion models aim to inform about diffusion dynamics and help entities to evaluate strategies for the role out of products.
While numerous models exist, they are often developed without regarding existing approaches and few common structures and code bases exist.
IRPact addresses this by developing a more generic framework rather than a model on the diffusion of eco-innovations.    

## Purpose
IRPact aims to provide a rather generic framework for innovation diffusion of eco-innovation. 
It is based on a synthesis of existing literature and aims to provide a common code-base for a wide range of models.
It is designed to be flexible and modular, and allow for easy extension to cater more specific needs.

Models can be specified through a number of configuration files, as detailed in the [configuration guide](./documentation/configuration_guide.md).

## How to get Started?
IRPact has been implemented in Java 8 (with the intention to upgrade to a more current release throughout the SUSIC project) and is built with Apache Maven.
Its prerequisites are a functioning JAVA environment with [maven](https://maven.apache.org/install.html).
IRPact is invoked through a .jar file, which is itself built in the packaging step of the maven build process.
This process will also take care of all dependencies to make sure all tools are setup according to the [pom file](./pom.xml).

### Building the project
As IRPact is run through invoking the respective .jar-file, the .jar has to be created first. 
As said, this is generated through the maven package, which is invoked through the following command in the command line:
```cmd
mvn package
```    
This step creates a IRPact-X.x.jar file in the ./target folder of the project (unless specified otherwise with the respective maven flag),
with X standing for the major version and x standing for the minor version as specified through the pom file.

### Running the project
After the project has been build (i.e. the IRPact-X.x.jar file exists in the ./target folder), the program can be invoked by:
```cmd
java -jar target/IRPact-X.x.jar 
```
For a specific model, a configuration has to be provided (as detailed in the [configuration guide](./documentation/configuration_guide.md)).
The configuration to be used is set as a command line argument. 
If you want to use the configuration *_myConfiguration_*, for example, you would invoke the program as follows:
```cmd
java -jar target/IRPact-X.x.jar myConfiguration 
```
The output is written in the command line and in the respective output channels configured in the [output scheme](./src/main/java/IRPact_modellierung/io/output/OutputScheme.java),
e.g. in the log files found in the [log folder](./logs/).

### Configuring your model
Specific models are configured through a bunch of .json files in the configuration folder of your model. 
The configuration must be placed in the ./target/configuration/*_yourModel_* folder with the respective configuration files for each model element of IRPact.
What configuration files are needed and how these are structured is detailed in the [configuration guide](./documentation/configuration_guide.md)).

## How to get Involved?  
We are always happy about people interested in joining us and supporting our research and development. 
If you would like to get involved, feel free to send us a message (at johanning[at]wifa.uni-leipzig.de).
Some ways you could get involved are through
* Contributing to the code-base
* Test the framework and help to improve it through bug reports
* Use IRPact as a framework for your own model (we gladly help support you adapting it according to your needs)
* Proposals for joint research endeavors
* Anything else you can think of

Just get in touch with us and lets discuss how we can collaborate. 

## License
IRPact is licensed under the GNU GENERAL PUBLIC LICENSE, Version 3, as specified in the [license file](./LICENSE).
This is in line with our passion for copyleft, i.e. conviction that users should have:
* the freedom to use the software for any purpose,
* the freedom to share the software with friends and neighbors,
* the freedom to change the software to suit the users needs, and
* the freedom to share the changes made