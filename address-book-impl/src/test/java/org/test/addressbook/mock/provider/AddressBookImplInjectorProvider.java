package org.test.addressbook.mock.provider;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import java.io.File;
import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.appops.cache.slim.CacheSlimModule;
import org.appops.configuration.ModuleConfig;
import org.appops.configuration.guice.ConfigServiceModule;
import org.appops.configuration.loader.ConfigurationLoader;
import org.appops.configuration.slimimpl.SlimImplStructure;
import org.appops.configuration.store.ConfigurationStore;
import org.appops.core.ClassPathAnalyser;
import org.appops.core.ServiceException;
import org.appops.core.deployment.DeploymentMode;
import org.appops.core.deployment.ServiceConfiguration;
import org.appops.core.deployment.WebConfig;
import org.appops.job.SchedulerSlimModule;
import org.appops.logging.guice.DefaultLoggerModule;
import org.appops.marshaller.DescriptorType;
import org.appops.marshaller.Marshaller;
import org.appops.marshaller.guice.MarshallerModule;
import org.appops.service.ServiceInitializer;
import org.appops.service.deployment.ServiceJettyLauncher;
import org.appops.service.entrypoint.ServiceArgs;
import org.appops.service.exception.AppEntryPointException;
import org.appops.service.exception.DeploymentException;
import org.appops.service.injection.ServiceBaseModule;
import org.appops.service.injection.ServiceStoreSlimModule;
import org.appops.web.jetty.JettyWebServiceModule;
import org.test.addressbook.injection.AddressBookModule;

public class AddressBookImplInjectorProvider {

  private static Injector injector;
  private String configDirPath;

  public AddressBookImplInjectorProvider() throws ServiceException {
    init();
  }

  public AddressBookImplInjectorProvider(String envConfigPath) throws ServiceException {
    setConfigDirPath(envConfigPath);
    init();
  }

  public void init() throws ServiceException {
    String args[] = {};
    ServiceArgs appArgs = new ServiceArgs(args);
    startApp(appArgs);
  }

  protected static void startApp(ServiceArgs serviceArgs) {
    try {
      List<Module> modules = new ArrayList<>();
      modules.add(new ConfigServiceModule());
      modules.add(new MarshallerModule());
      modules.add(new DefaultLoggerModule());

      modules.add(new CacheSlimModule());
      modules.add(new ServiceStoreSlimModule());
      modules.add(new SchedulerSlimModule());
      modules.add(new ServiceBaseModule());
      modules.add(new JettyWebServiceModule());

      modules.add(new AddressBookModule());

      injector = Guice.createInjector(modules);

      String configString =
          FileUtils.readFileToString(serviceArgs.getServiceConfig(), StandardCharsets.UTF_8);
      Marshaller marshaller = injector.getInstance(Marshaller.class);
      DescriptorType descriptorType =
          DescriptorType.fromExtension(serviceArgs.getServiceConfig().getName());
      ServiceConfiguration serviceConfiguration =
          marshaller.unmarshall(configString, ServiceConfiguration.class, descriptorType);
      WebConfig webConfig = new WebConfig();
      webConfig.setIp("http://localhost");
      webConfig.setPort(8091);
      webConfig.setContextPath("");
      serviceConfiguration.setWebConfig(webConfig);
      String depConfigLight =
          marshaller.marshall(serviceConfiguration.lightweightCopy(), descriptorType);
      ConfigurationStore configurationStore = injector.getInstance(ConfigurationStore.class);
      configurationStore.addConfiguration(ServiceConfiguration.class.getCanonicalName(),
          depConfigLight, descriptorType);
      String profileName = serviceArgs.getSelectedProfileName();
      String profileRoot = serviceArgs.getProfileRoot();
      File folder = new File(profileRoot + profileName);
      File[] listOfFiles = folder.listFiles();
      HashMap<String, ServiceConfiguration> serviceConfigMap = new HashMap<>();
      String serviceDeploymentMode = serviceArgs.getDeploymentMode();
      setDeployementMode(serviceDeploymentMode, serviceConfiguration);
      for (File file : listOfFiles) {
        if (file.isDirectory()) {
          String slimImplServiceName = file.getName();
          boolean isCoreService = false;
          if (serviceConfiguration.getServiceName().equals(slimImplServiceName)) {
            isCoreService = true;
          }
          File[] files = file.listFiles();
          for (File slimImplconfigfile : files) {
            String configFileName = slimImplconfigfile.getName();
            if (isCoreService) {
              populateServiceConfiguration(marshaller, slimImplconfigfile, serviceConfiguration);
            } else if (serviceDeploymentMode.equalsIgnoreCase(DeploymentMode.CLUBBED.toString())
                && isImplYml(configFileName)) {
              populateClubbedConfiguration(marshaller, slimImplconfigfile, slimImplServiceName,
                  serviceConfigMap, serviceConfiguration.getMode());
            } else if (serviceDeploymentMode.equalsIgnoreCase(DeploymentMode.STANDALONE.toString())
                && !isImplYml(configFileName)) {
              populateStandaloneConfiguration(marshaller, slimImplconfigfile, slimImplServiceName,
                  serviceConfigMap, serviceConfiguration.getMode());
            }
          }
        }
      }

      initializeServices(serviceConfigMap, injector, serviceConfiguration, profileName,
          profileRoot);
      ServiceJettyLauncher appLauncher = injector.getInstance(ServiceJettyLauncher.class);
      System.setProperty("currentProfile", profileName);
      System.setProperty("baseUrl", serviceConfiguration.serviceUrl());
      appLauncher.launch(serviceConfiguration);

    } catch (Exception e) {
      throw new AppEntryPointException(e);
    }
  }


  private static boolean isImplYml(String configFileName) {
    if (configFileName.endsWith("impl.yml")) {
      return true;
    }
    return false;
  }


  private static void populateServiceConfiguration(Marshaller marshaller, File slimImplconfigfile,
      ServiceConfiguration serviceConfiguration) {
    try {
      String serviceconfigString =
          FileUtils.readFileToString(slimImplconfigfile, StandardCharsets.UTF_8);
      DescriptorType configDescriptorType =
          DescriptorType.fromExtension(slimImplconfigfile.getName());
      SlimImplStructure slimImplConfig =
          marshaller.unmarshall(serviceconfigString, SlimImplStructure.class, configDescriptorType);

      if (slimImplconfigfile.getName().endsWith("slim.yml")) {
        serviceConfiguration.getModules().getSlimModules().addAll(slimImplConfig.getModules());
      } else if (slimImplconfigfile.getName().endsWith("impl.yml")) {
        serviceConfiguration.getModules().getImplModules().addAll(slimImplConfig.getModules());
      }
    } catch (Exception e) {

    }

  }


  private static ServiceConfiguration populateServiceConfiguration(SlimImplStructure slimImplConfig,
      String slimImplServiceName, DeploymentMode deploymentMode) throws Exception {

    try {
      if (slimImplConfig != null) {
        ServiceConfiguration serviceConfiguration = new ServiceConfiguration();
        serviceConfiguration.setAnnotationClass(Class.forName(slimImplConfig.getAnnotationClass()));
        serviceConfiguration.setServiceConfig(slimImplConfig.getConfig());
        serviceConfiguration.setServiceName(slimImplServiceName);
        if (deploymentMode.equals(DeploymentMode.STANDALONE)) {
          ModuleConfig slimModduleConfig = new ModuleConfig();
          slimModduleConfig.setSlimModules(slimImplConfig.getModules());
          serviceConfiguration.setModules(slimModduleConfig);
        } else if (deploymentMode.equals(DeploymentMode.CLUBBED)) {
          ModuleConfig implModuleConfig = new ModuleConfig();
          implModuleConfig.setImplModules(slimImplConfig.getModules());
          serviceConfiguration.setModules(implModuleConfig);
        } else {
          throw new DeploymentException("Deployment mode not matched " + deploymentMode.name());
        }
        return serviceConfiguration;
      }
    } catch (Exception e) {
      throw e;
    }
    return null;
  }

  private static void populateStandaloneConfiguration(Marshaller marshaller, File configfile,
      String slimConfigServiceName, HashMap<String, ServiceConfiguration> serviceConfigMap,
      DeploymentMode deploymentMode) throws Exception {
    try {
      String serviceconfigString = FileUtils.readFileToString(configfile, StandardCharsets.UTF_8);
      DescriptorType configDescriptorType = DescriptorType.fromExtension(configfile.getName());
      SlimImplStructure slimImplConfig =
          marshaller.unmarshall(serviceconfigString, SlimImplStructure.class, configDescriptorType);
      ServiceConfiguration serviceConfiguration =
          populateServiceConfiguration(slimImplConfig, slimConfigServiceName, deploymentMode);
      serviceConfigMap.put(slimConfigServiceName, serviceConfiguration);
    } catch (Exception e) {
      throw e;
    }
  }


  private static void populateClubbedConfiguration(Marshaller marshaller, File configfile,
      String implConfigServiceName, HashMap<String, ServiceConfiguration> serviceConfigMap,
      DeploymentMode deploymentMode) throws Exception {
    try {
      String serviceconfigString = FileUtils.readFileToString(configfile, StandardCharsets.UTF_8);
      DescriptorType configDescriptorType = DescriptorType.fromExtension(configfile.getName());
      SlimImplStructure slimImplConfig =
          marshaller.unmarshall(serviceconfigString, SlimImplStructure.class, configDescriptorType);
      ServiceConfiguration serviceConfiguration =
          populateServiceConfiguration(slimImplConfig, implConfigServiceName, deploymentMode);
      serviceConfigMap.put(implConfigServiceName, serviceConfiguration);
    } catch (Exception e) {
      throw e;
    }
  }

  private static void setDeployementMode(String serviceDeploymentMode,
      ServiceConfiguration serviceConfiguration) throws Exception {

    try {
      if (serviceDeploymentMode.equals(DeploymentMode.STANDALONE.name())) {
        serviceConfiguration.setMode(DeploymentMode.STANDALONE);
      } else if (serviceDeploymentMode.equals(DeploymentMode.CLUBBED.name())) {
        serviceConfiguration.setMode(DeploymentMode.CLUBBED);
      } else {
        throw new ServiceException("Invalid Deployment Mode :" + serviceDeploymentMode);
      }
    } catch (Exception e) {
      throw e;
    }
  }


  private static void initializeServices(Map<String, ServiceConfiguration> services,
      Injector appInjector, ServiceConfiguration entryPointConfig, String currentProfile,
      String profileRoot) {
    ClassPathAnalyser classPathAnalyser =
        new ClassPathAnalyser(entryPointConfig.getPackageToScan());
    Collection<Class<? extends ServiceInitializer>> initializers =
        classPathAnalyser.subTypesOf(ServiceInitializer.class);
    DeploymentMode deploymentMode = entryPointConfig.getMode();
    String currentService = entryPointConfig.getServiceName();
    String baseConfigPath = profileRoot + currentProfile;
    try {
      if (DeploymentMode.STANDALONE.equals(deploymentMode)) {
        initializeService(currentService, entryPointConfig, appInjector, initializers,
            baseConfigPath);
      } else {
        for (String serviceName : services.keySet()) {
          if (serviceName.contentEquals(currentService)) {
            initializeService(serviceName, entryPointConfig, appInjector, initializers,
                baseConfigPath);
          } else {
            ServiceConfiguration config = services.get(serviceName);
            initializeService(serviceName, config, appInjector, initializers, baseConfigPath);
          }
        }
      }
    } catch (Exception e) {
      throw e;
    }

  }

  private static void initializeService(String serviceName, ServiceConfiguration config,
      Injector appInjector, Collection<Class<? extends ServiceInitializer>> initializers,
      String baseConfigPath) {
    baseConfigPath = baseConfigPath.endsWith("/") ? baseConfigPath : baseConfigPath + "/";
    File ymlConfig = new File(baseConfigPath + serviceName + "/");
    if (ymlConfig.exists()) {
      if (ymlConfig.isDirectory()) {
        for (File ymlFile : ymlConfig.listFiles()) {
          appInjector.getInstance(ConfigurationLoader.class).loadConfigurationsFromFile(serviceName,
              ymlFile);
        }
      } else {
        System.out.println("Warning : Configuration for " + serviceName
            + " does not exist on path -> " + ymlConfig.getPath());
      }
    }
    Class<? extends Annotation> serviceAnnotation =
        (Class<? extends Annotation>) config.getAnnotationClass();
    for (Class<? extends ServiceInitializer> initializer : initializers) {
      if (initializer.isAnnotationPresent(serviceAnnotation)) {
        appInjector.getInstance(initializer).initialize(serviceName, config, serviceAnnotation);
        initializers.remove(initializer);
        break;
      }
    }
  }


  public Injector getInjector() {
    return injector;
  }

  public static void setInjector(Injector injector) {
    AddressBookImplInjectorProvider.injector = injector;
  }

  public String getConfigDirPath() {
    return configDirPath;
  }

  public void setConfigDirPath(String configDirPath) {
    this.configDirPath = configDirPath;
  }


}
