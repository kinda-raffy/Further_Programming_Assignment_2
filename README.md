# LMVM Application

## Prerequisites

- Java Development Kit (JDK) 21
- Maven (included via wrapper)

## Getting Started

### Running the Application

1. Clone the repository into your local machine and navigate to the project directory.

```bash
gh repo clone kinda-raffy/Further_Programming_Assignment_2 && cd Further_Programming_Assignment_2
```

2. Ensure you are using JDK 21. This project supports `sdkman` and is configured to use `java=21.0.6-tem`.

```bash
# Install sdkman
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
# Set JDK for this shell
sdk env
```

3. Run the application using the Maven wrapper.

```bash
# Unix
./mvnw clean javafx:run
# Windows
mvnw.cmd clean javafx:run
```
