# Automatic and Interactive Association Map Creation from Documents using Association Rule Mining

This repository contains the source code and resources for the paper:

> **E. Mitkousis and Y. Tzitzikas**, "Interactive Association Map Creation from Documents using Association Rule Mining", *Proceedings of the 29th International Conference on Theory and Practice of Digital Libraries (TPDL 2025)*, Tampere, Finland, September 2025.


## Abstract

One method to aid the understanding of a document corpus is to construct automatically a knowledge map for that corpus by analyzing the contents of the documents. Several methods have been proposed in the literature for this task. In this paper, we investigate a method for automatic knowledge map construction that is based on **Association Rule Mining (ARM)**. ARM was originally proposed for databases and structured data as a method for data mining, e.g., for market basket analysis. In this work, we explore its application to unstructured text documents.

---

## User Manual

This section provides a step-by-step guide on how to install, configure, and use the software.

### 1. **Prerequisites**
- **Java Development Kit (JDK)**: Ensure you have JDK 17 or later installed on your system.
- **Maven**: The project uses Maven for dependency management and building. Make sure Maven is installed.
- **Text Documents**: Prepare the corpus of documents you want to analyze. Supported formats: `.txt` or any plain text format.

### 2. **Installation**
1. Clone the repository:
   ```bash
   git clone https://github.com/EfthimisM/AssociationMaps
   ```
2. Navigate to the project directory:
   ```bash
   cd AssociationMaps
   ```
3. Build the project using Maven:
   ```bash
   mvn clean install
   ```

### 3. **Running the Program**
After building the project, you can run the program using the following command:
   ```bash
   java -jar target/WebApp1-1.0-SNAPSHOT.jar
   ```
   or 
   ```bash
   mvn spring-boot:run
   ```
#### 4. Starting the Program
- The program runs on `localhost:8080`.
- After starting the program, open any web browser and navigate to:
  http://localhost:8080/visualize

---

#### 5. Input Parameters
On the visualization page, you will need to provide the following inputs:

1. **Folder Path**
- Enter the full path to a folder on your computer that contains a collection of `.txt` files.
- Example: `C:\Documents\TextFiles`

2. **Support Threshold**
- Set the minimum support threshold for the rules displayed in the knowledge map.
- Support indicates how frequently a rule appears in the dataset.
- Example: `0.1` (10%) or and absolute value like `10` (it's found in 10 documents)

3. **Confidence Threshold**
- Set the minimum confidence threshold for the rules displayed in the knowledge map.
- Confidence indicates the reliability of a rule.
- Example: `0.7` (70%)

4. **Phrase Length**
- Specify the number of words each term in the map should contain.
- Example: `2` (for bigrams like "machine learning")

---

#### 6. Interactive Knowledge Map
Once the program processes your input, an interactive knowledge map will be displayed. You can interact with the map as follows:

1. **Explore Terms (Nodes)**
- Click on any term (node) to see its level in the hierarchy and related terms.

2. **Inspect Rules (Edges)**
- Click on any rule (edge) to view its confidence and support scores.

3. **Update Input and Re-run**
- You can modify the input parameters (folder path, support, confidence, or phrase length) and re-run the program to generate a new knowledge map.

---

#### 7. Tips for Best Results
- Ensure the folder contains only `.txt` files with clean, readable text.
- Start with lower support and confidence thresholds to see more rules, then adjust as needed.
- Experiment with different phrase lengths to explore different levels of granularity in the terms.
