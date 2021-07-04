package com.mobiquity.packer;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.processor.PackageProcessor;
import com.mobiquity.packer.processor.PackageProcessorTemplate;

/**
 * Service Invoker responsible to invoke the package processor
 */
public class Packer {

  private static final PackageProcessorTemplate packageProcessor = new PackageProcessor();

  private Packer() {
  }

  public static String pack(String filePath) throws APIException {
    return packageProcessor.pack(filePath);
  }


}
