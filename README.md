# :mega: ChatUnitest Maven Plugin

![logo](docs/img/logo.png)


[English](./README.md) | [中文](./Readme_zh.md)

[![Maven Central](https://img.shields.io/maven-central/v/io.github.ZJU-ACES-ISE/chatunitest-maven-plugin?color=hex&style=plastic)](https://maven-badges.herokuapp.com/maven-central/io.github.ZJU-ACES-ISE/chatunitest-maven-plugin)

## Updates:
💥 Add docker image to generate tests in isolated sandbox environment.

💥 Added multithreading feature for faster test generation.

💥 Plugin now exports runtime and error logs.

💥 Custom prompt support added.

💥 Algorithm optimized to minimize token usage.

💥 Expanded configuration options. Refer to **Steps to Run** for details.

💥 Integrate multiple related tasks.

## Background
Many people have tried using ChatGPT to assist with various programming tasks and have achieved good results. However, there are some issues with directly using ChatGPT: First, the generated code often does not execute properly, leading to the adage **“five minutes of coding, two hours of debugging.”** Second, it is inconvenient to integrate with existing projects, as it requires manual interaction with ChatGPT and switching between different pages. To address these issues, we have proposed a **“Generate-Validate-Fix”** framework and implemented a prototype system. Additionally, to facilitate usage, we have developed several plugins that can be easily integrated into existing development workflows. We have completed the development of a Maven plugin, which has been published to the Maven Central Repository, and we welcome you to try it out and provide feedback. We have also launched the Chatunitest plugin in the IntelliJ IDEA Plugin Marketplace. You can search for and install ChatUniTest in the marketplace, or visit the plugin page [Chatunitest: IntelliJ IDEA Plugin](https://plugins.jetbrains.com/plugin/22522-chatunitest) for more information about our plugin. In this latest branch, we have integrated multiple related works that we have reproduced, allowing users to choose and use them as needed.


## Steps to run (Docker)

See [chenyi26/chatunitest](https://hub.docker.com/repository/docker/chenyi26/chatunitest/general).

## Steps to run

**Overall, you need to introduce two dependencies (core and starter) and a plugin in the project under test**

**And pay attention to whether the introduced version is correct**

### 1. Configuration of `pom.xml` File

To configure the `chatunitest-maven-plugin` in the `pom.xml` file of the project where you want to generate unit tests, add the following plugin configuration and adjust the parameters according to your requirements:

```xml
<plugin>
    <groupId>io.github.ZJU-ACES-ISE</groupId>
    <artifactId>chatunitest-maven-plugin</artifactId>
    <version>1.5.1</version>
    <configuration>
        <!-- Required: You must specify your OpenAI API keys. -->
        <apiKeys></apiKeys>
        <model>gpt-4o-mini</model>
        <proxy>${proxy}</proxy>
    </configuration>
</plugin>
```

In general, you only need to provide the API key. **If you encounter an APIConnectionError, you can add your proxy IP and port number in the `proxy` parameter.** You can find the proxy IP and port for Windows by navigating to Settings -> Network & Internet -> Proxy:

**Here is a detailed explanation of each configuration option:**

- `apiKeys`: (**Required**) Your OpenAI API keys, e.g., `Key1, Key2, ...`
- `model`: (**Optional**) OpenAI model, default value: `gpt-3.5-turbo`
- `url`: (**Optional**) API for calling the model, default value: `https://api.openai.com/v1/chat/completions`
- `testNumber`: (**Optional**) The number of tests generated for each method, default value: `5`
- `maxRounds`: (**Optional**) Maximum rounds for the fixing process, default value: `5`
- `minErrorTokens`: (**Optional**) Minimum token count for error messages during the fixing process, default value: `500`
- `temperature`: (**Optional**) OpenAI API parameter, default value: `0.5`
- `topP`: (**Optional**) OpenAI API parameter, default value: `1`
- `frequencyPenalty`: (**Optional**) OpenAI API parameter, default value: `0`
- `presencePenalty`: (**Optional**) OpenAI API parameter, default value: `0`
- `proxy`: (**Optional**) If needed, enter your hostname and port number, e.g., `127.0.0.1:7078`
- `selectClass`: (**Optional**) The class to be tested; if there are classes with the same name in the project, specify the full class name.
- `selectMethod`: (**Optional**) The method to be tested
- `tmpOutput`: (**Optional**) Output path for parsing project information, default value: `/tmp/chatunitest-info`
- `testOutput`: (**Optional**) Output path for tests generated by `chatunitest`, default value: `{basedir}/chatunitest`
- `project`: (**Optional**) Target project path, default value: `{basedir}`
- `thread`: (**Optional**) Enable or disable multithreading, default value: `true`
- `maxThread`: (**Optional**) Maximum number of threads, default value: `CPU core count * 5`
- `stopWhenSuccess`: (**Optional**) Whether to stop after generating a successful test, default value: `true`
- `noExecution`: (**Optional**) Whether to skip the step of executing test verification, default value: `false`

All these parameters can also be specified using the `-D` option in the command line.
- `merge`: (**Optional**) Merge all tests corresponding to each class into a test suite, default value: `true`.
- `promptPath`: (**Optional**) Path for custom prompts. Reference the default prompt directory: `src/main/resources/prompt`.
- `obfuscate`: (**Optional**) Enable obfuscation to protect privacy code. Default value: `false`.
- `obfuscateGroupIds`: (**Optional**) Group IDs that need to be obfuscated. Default value includes only the current project's group ID. All these parameters can also be specified using the `-D` option in the command line.
- `phaseType`: (**Optional**) Choose the reproduction scheme. If not selected, the default `chatunitest` process will be executed. All these parameters can also be specified using the `-D` option in the command line.
    - CoverUP
    - HITS
    - TELPA
    - SYMPROMPT
    - CHATTESTER
    - TESTSPARK
    - TESTPILOT
    - MUTAP

If you are using a local large model (e.g., code-llama), simply change the model name and request URL as follows:

```xml
<plugin>
    <groupId>io.github.ZJU-ACES-ISE</groupId>
    <artifactId>chatunitest-maven-plugin</artifactId>
    <version>1.5.1</version>
    <configuration>
        <!-- Required: Use any string to replace your API keys -->
        <apiKeys>xxx</apiKeys>
        <model>code-llama</model>
        <url>http://0.0.0.0:8000/v1/chat/completions</url>
    </configuration>
</plugin>
```

![img.png](src/main/resources/img/win_proxy.png)

### 1. Add the Following Dependency to the `pom.xml` File

Similarly, add the following dependency to the `pom.xml` file of the project where you want to generate unit tests:

```xml
<dependency>
    <groupId>io.github.ZJU-ACES-ISE</groupId>
    <artifactId>chatunitest-starter</artifactId>
    <version>1.4.0</version>
    <type>pom</type>
</dependency>
```

### 2. Running the Plugin

**First, you need to install the project and download the required dependencies, which can be done by running the `mvn install` command.**

**You can run the plugin using the following commands:**

**To generate unit tests for a specific method:**

```shell
mvn chatunitest:method -DselectMethod=className#methodName
```

**To generate unit tests for a specific class:**

```shell
mvn chatunitest:class -DselectClass=className
```

When executing the `mvn chatunitest:method` or `mvn chatunitest:class` commands, you must specify `selectMethod` and `selectClass`, which can be achieved using the `-D` option.

**Example:**

```java
public class Example {
    public void method1(Type1 p1, ...) {...}
    public void method2() {...}
    ...
}
```

To test the `Example` class and all its methods:

```shell
mvn chatunitest:class -DselectClass=Example
```

To test the `method1` in the `Example` class (currently, ChatUnitest will generate tests for all methods named `method1` in the class):

```shell
mvn chatunitest:method -DselectMethod=Example#method1
```

**To generate unit tests for the entire project:**

:warning: :warning: :warning: For large projects, this may consume a significant number of tokens, resulting in considerable costs.

```shell
mvn chatunitest:project
```

**To generate unit tests using a specific phase type:**

```shell
mvn chatunitest:method -DselectMethod=className#methodName -DselectMethod=className#methodName -DphaseType=COVERUP
```

**To clean up the generated test code:**

```shell
mvn chatunitest:clean
```

Running this command will delete all generated test code and restore your test directory.

**To manually run the generated tests:**

```shell
mvn chatunitest:copy
```

Running this command will copy all generated test code to your test folder while backing up your test directory.

If the `merge` configuration is enabled, you can run the test suite for each class:

```shell
mvn chatunitest:restore
```

Running this command will restore your test directory.

## Custom Content
### Using FTL Templates

#### 1. Configure Mapping Relationships
Define the mapping relationships in the `config.properties` file.

#### 2. Define the PromptFile Enum Class
In the `PromptFile` enum class, define the enum constants along with their corresponding template file names.

#### 3. Reference Templates
Reference the `PromptFile` templates in the `getInitPromptFile` and `getRepairPromptFile` methods of the `PromptGenerator` class.

#### 4. Generate Prompts
Subsequently, call the `generateMessages` method of the `PromptGenerator` to retrieve the prompts. For specific implementation details, refer to the HITS implementation.

### Expand FTL template
`PromptInfo is a data entity class that can be extended as needed` The 'dataModel' in PromptTemplate stores variable data for use by FTL templates. If there is a custom new FTL template, please check if there are any new variables introduced and update the 'dataModel' in a timely manner.

### Modifying the Granularity of Generated Unit Tests
You can create a subclass of `MethodRunner`, as seen in `HITSRunner`, and add a new implementation in the `selectRunner` method.

### Custom Unit Test Generation Scheme
If you wish to define your own unit test generation scheme, here is an example:

- First, you need to define a subclass of `PhaseImpl` to implement the core generation scheme. We typically place it in the `solution` folder of the `phase`.

- Next, add a new implementation in the `createPhase` method within the `PhaseImpl` class. If there are new templates, refer to the section on using FTL templates; if new data variables are introduced, see the section on modifying FTL templates.

- If you need to modify the granularity of the generated unit tests (for example, HITS generates unit tests based on method slicing), please refer to the section on modifying the granularity of generated unit tests.

## Supported Environments

The ChatUnitest Maven Plugin can run on multiple operating systems and various Java Development Kits and Maven versions. Here are the tested and supported environments:

- Environment 1: Windows 11 / Oracle JDK 8 / Maven 3.9
- Environment 2: Windows 10 / Oracle JDK 8 / Maven 3.6
- Environment 3: Ubuntu 22.04 / OpenJDK 8 / Maven 3.6
- Environment 4: Darwin Kernel 22.1.0 / Oracle JDK 8 / Maven 3.8

Please note that these environments are examples that have been tested and can run successfully; you may also try running the plugin in other similar environments. If you encounter issues in other environments, please refer to the documentation or contact the developers.

## :construction: TODO

- Add code obfuscation to avoid sending raw code to ChatGPT
- Add cost estimation and quotas
- Optimize the structure of generated test cases

## MISC

Our work has been submitted to arXiv, and you can find the link here: [ChatUniTest](https://arxiv.org/abs/2305.04764).

```
@misc{xie2023chatunitest,
      title={ChatUniTest: a ChatGPT-based automated unit test generation tool}, 
      author={Zhuokui Xie and Yinghao Chen and Chen Zhi and Shuiguang Deng and Jianwei Yin},
      year={2023},
      eprint={2305.04764},
      archivePrefix={arXiv},
      primaryClass={cs.SE}
}
```

## :email: Contact Us

If you have any questions or would like to learn more about our experimental results, please feel free to contact us via email at the following addresses:

1. Corresponding author: `zjuzhichen AT zju.edu.cn`
2. Author: `yh_ch AT zju.edu.cn`, `xiezhuokui AT zju.edu.cn`
