# IVH5 Java concurrency code #

This repository contains example Java concurrency files.  

## Usage ##
1. To get the code, open a command prompt and type

`git clone https://rschellius@bitbucket.org/rschellius/java-concurrency.git`

2. To build the code, go to http://www.gradle.org/ and install gradle .
3. From a command prompt in the examples root directory, run `gradle build`.

Optionally:

1. Run `gradle eclipse` to create Eclipse project files.
2. Import the projects into Eclipse as existing projects.

To run an example from the command line, navigate to an example directory and type

	java -cp build\classes\main classname

For example, from the SleepMessages directory:

	java -cp build\classes\main SleepMessages
	
## Gradle commands ##

| Command | Actie                    |
| -------------------------- | ---------------------------------- |
| `gradle build`      | Compile and test code.   |
| `gradle test`   | Run the testcases.    |
| `gradle compileJava` | Compile the code. |
| `gradle clean` | Remove all generated classfiles. |
| `gradle eclipse` | Generate Eclipse project files. Then import a project from Eclipse as an exitsting project. |
| `gradle cleanEclipse` | Remove Eclipse project files. |