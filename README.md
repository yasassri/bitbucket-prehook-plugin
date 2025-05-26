# Bitbucket Prehook Plugin

A Bitbucket Server (Data Center) pre-receive hook plugin for repository management and enforcement of custom rules before accepting pushes and tags.

## Features

- Enforces tag naming conventions (see TagValidationHook)
- Prevents unwanted tags from being pushed to repositories
- Easy integration with Bitbucket Server/Data Center
- Built with Java and Closure Templates

## Getting Started

### Prerequisites

- Java 8 or higher
- Bitbucket Server/Data Center 5.x or later
- Maven

### Building the Plugin

Clone this repository and build using Maven:

```bash
git clone https://github.com/yasassri/bitbucket-prehook-plugin.git
cd bitbucket-prehook-plugin
mvn package
```

The plugin JAR will be located in the `target/` directory.

### Installing the Plugin

1. Go to Bitbucket Administration > Manage Apps.
2. Upload the generated JAR file.
3. Enable the **Bitbucket Prehook Plugin**.

### Usage

This plugin automatically enforces tag naming and other validation rules as defined in `TagValidationHook.java`. You can customize the logic in `src/main/java/org/ycr/bitbucket/impl/TagValidationHook.java` to suit your organization’s needs.

### Example: Tag Validation

By default, this plugin checks all tags pushed to the repository and rejects those that don’t match your configured patterns.

## Project Structure

```
src/
  main/
    java/
      org/
        ycr/
          bitbucket/
            impl/
              TagValidationHook.java   # Main logic for tag validation
  resources/
    # Closure Templates and other resources
```

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Author

- [yasassri](https://github.com/yasassri)

---

Let me know if you want to include more details or usage examples!
