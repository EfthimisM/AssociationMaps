# Automatic and Interactive Knowledge Map Creation from Documents using Association Rule Mining

## Abstract

One method to aid the understanding of a document corpus is to construct automatically a knowledge map for that corpus by analyzing the contents of the documents. Several methods have been proposed in the literature for this task. In this paper, we investigate a method for automatic knowledge map construction that is based on **Association Rule Mining (ARM)**. ARM was originally proposed for databases and structured data as a method for data mining, e.g., for market basket analysis. In this work, we explore its application to unstructured text documents.

---

## User Manual

This section provides a step-by-step guide on how to install, configure, and use the software.

### 1. **Prerequisites**
- **Java Development Kit (JDK)**: Ensure you have JDK 8 or later installed on your system.
- **Maven**: The project uses Maven for dependency management and building. Make sure Maven is installed.
- **Text Documents**: Prepare the corpus of documents you want to analyze. Supported formats: `.txt` or any plain text format.

### 2. **Installation**
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/your-repo-name.git
   ```
2. Navigate to the project directory:
   ```bash
   cd your-repo-name
   ```
3. Build the project using Maven:
   ```bash
   mvn clean install
   ```

### 3. **Running the Program**
After building the project, you can run the program using the following command:
   ```bash
   java -jar target/your-jar-file.jar --input /path/to/documents --output /path/to/output