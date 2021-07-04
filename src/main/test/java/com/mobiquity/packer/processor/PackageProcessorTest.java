package com.mobiquity.packer.processor;

import static java.lang.Thread.currentThread;
import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mobiquity.exception.APIException;
import java.io.File;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Test class for PackageProcessor.java where all the magic happens
 * 1. Get the reference to file
 * 2. Process the data in the file and initialize it to Map of Items
 */
@RunWith(MockitoJUnitRunner.class)
public class PackageProcessorTest {

  @InjectMocks
  private PackageProcessor packageProcessor;

  @Test
  public void test_pack_happy() throws APIException {
    String expected = "4\n"
        + "-\n"
        + "2,7\n"
        + "8,9\n";
    String filePath = getFilePathFromResources();
    String items = packageProcessor.pack(filePath);

    assertNotNull("The result of items are not null ", items);
    assertEquals("Items that can be included in package ", expected, items);
  }

  /**
   * Helper method to read the file path from resources for unit test purpose
   *
   * @return filePath - absolute path of the file
   */
  String getFilePathFromResources() throws APIException {
    ClassLoader classloader = currentThread().getContextClassLoader();
    return new File(requireNonNull(classloader.getResource("example_input")).getFile())
        .getAbsolutePath();
  }
}
