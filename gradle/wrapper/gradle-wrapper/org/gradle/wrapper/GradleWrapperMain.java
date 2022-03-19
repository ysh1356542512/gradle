// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GradleWrapperMain.java

package org.gradle.wrapper;

import java.io.File;
import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Properties;
import org.gradle.cli.*;

// Referenced classes of package org.gradle.wrapper:
//            Download, Install, PathAssembler, BootstrapMainStarter, 
//            Logger, WrapperExecutor, SystemPropertiesHandler, GradleUserHomeLookup

public class GradleWrapperMain
{

    public GradleWrapperMain()
    {
    }

    public static void main(String args[])
        throws Exception
    {
        File wrapperJar = wrapperJar();
        File propertiesFile = wrapperProperties(wrapperJar);
        File rootDir = rootDir(wrapperJar);
        CommandLineParser parser = new CommandLineParser();
        parser.allowUnknownOptions();
        parser.option(new String[] {
            "g", "gradle-user-home"
        }).hasArgument();
        parser.option(new String[] {
            "q", "quiet"
        });
        SystemPropertiesCommandLineConverter converter = new SystemPropertiesCommandLineConverter();
        converter.configure(parser);
        ParsedCommandLine options = parser.parse(args);
        Properties systemProperties = System.getProperties();
        systemProperties.putAll(converter.convert(options, new HashMap()));
        File gradleUserHome = gradleUserHome(options);
        addSystemProperties(systemProperties, gradleUserHome, rootDir);
        Logger logger = logger(options);
        WrapperExecutor wrapperExecutor = WrapperExecutor.forWrapperPropertiesFile(propertiesFile);
        wrapperExecutor.execute(args, new Install(logger, new Download(logger, "gradlew", "0"), new PathAssembler(gradleUserHome, rootDir)), new BootstrapMainStarter());
    }

    private static void addSystemProperties(Properties systemProperties, File gradleUserHome, File rootDir)
    {
        systemProperties.putAll(SystemPropertiesHandler.getSystemProperties(new File(rootDir, "gradle.properties")));
        systemProperties.putAll(SystemPropertiesHandler.getSystemProperties(new File(gradleUserHome, "gradle.properties")));
    }

    private static File rootDir(File wrapperJar)
    {
        return wrapperJar.getParentFile().getParentFile().getParentFile();
    }

    private static File wrapperProperties(File wrapperJar)
    {
        return new File(wrapperJar.getParent(), wrapperJar.getName().replaceFirst("\\.jar$", ".properties"));
    }

    private static File wrapperJar()
    {
        URI location;
        try
        {
            location = org/gradle/wrapper/GradleWrapperMain.getProtectionDomain().getCodeSource().getLocation().toURI();
        }
        catch(URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
        if(!location.getScheme().equals("file"))
            throw new RuntimeException(String.format("Cannot determine classpath for wrapper Jar from codebase '%s'.", new Object[] {
                location
            }));
        try
        {
            return Paths.get(location).toFile();
        }
        catch(NoClassDefFoundError e)
        {
            return new File(location.getPath());
        }
    }

    private static File gradleUserHome(ParsedCommandLine options)
    {
        if(options.hasOption("g"))
            return new File(options.option("g").getValue());
        else
            return GradleUserHomeLookup.gradleUserHome();
    }

    private static Logger logger(ParsedCommandLine options)
    {
        return new Logger(options.hasOption("q"));
    }

    public static final String GRADLE_USER_HOME_OPTION = "g";
    public static final String GRADLE_USER_HOME_DETAILED_OPTION = "gradle-user-home";
    public static final String GRADLE_QUIET_OPTION = "q";
    public static final String GRADLE_QUIET_DETAILED_OPTION = "quiet";
}
