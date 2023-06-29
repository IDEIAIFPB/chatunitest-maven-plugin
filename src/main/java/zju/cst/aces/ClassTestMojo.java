package zju.cst.aces;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import zju.cst.aces.parser.ProjectParser;
import zju.cst.aces.runner.ClassRunner;
import zju.cst.aces.utils.Config;
import zju.cst.aces.utils.TestCompiler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author chenyi
 * A demo of ChatUniTest maven plugin
 */

@Mojo(name = "class")
public class ClassTestMojo
        extends ProjectTestMojo {
    @Parameter(name = "selectClass", required = true)
    public String selectClass;

    /**
     * Generate tests for all classes in the project
     * @throws MojoExecutionException
     */
    public void execute() throws MojoExecutionException {
        init();
        String className = selectClass;
        tmpOutput = Paths.get(tmpOutput, Paths.get(project).getFileName().toString()).toString();
        String parseOutput = tmpOutput + File.separator + "class-info";
        parseOutput = parseOutput.replace("/", File.separator);
        Path projectPath = Paths.get(project).resolve("src" + File.separator + "main" + File.separator + "java");

        String jarDeps = tmpOutput + File.separator + "jar-deps";
        ProjectParser parser = new ProjectParser(projectPath.toString(), parseOutput, jarDeps);
        if (! (new File(parseOutput).exists())) {
            getLog().info("\n==========================\n[ChatTester] Parsing class info ...");
            parser.parse();
            getLog().info("\n==========================\n[ChatTester] Parse finished");
        }

        getLog().info("\n==========================\n[ChatTester] Generating tests for class " + className + " ...");
        TestCompiler.backupTestFolder();
        Config.setApiKeys(apiKeys);
        try {
            new ClassRunner(className, parseOutput, testOutput).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TestCompiler.restoreTestFolder();

        getLog().info("\n==========================\n[ChatTester] Generation finished");
    }
}
