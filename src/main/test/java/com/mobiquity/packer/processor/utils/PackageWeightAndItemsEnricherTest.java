package com.mobiquity.packer.processor.utils;

import static java.lang.Thread.currentThread;
import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.models.Item;
import com.mobiquity.packer.utils.PackageWeightAndItemsEnricher;
import java.io.File;
import java.util.List;
import java.util.Map;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PackageWeightAndItemsEnricherTest {

  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();
  @InjectMocks
  private PackageWeightAndItemsEnricher underTest;

  @Test
  public void initializeItems_Happy() throws APIException {
    String filePath = getFilePathFromResources("example_input");
    Map<String, List<Item>> packageWeightAndRelatedItems = underTest
        .initializeItems(filePath);
    assertNotNull("The Input from file result in Map and is not null",
        packageWeightAndRelatedItems);
    assertEquals("Verify the Size of Map", 4,packageWeightAndRelatedItems.size());
  }

  @Test
  public void initializeItems_FilePathIncorrectIOException() throws APIException {
    exceptionRule.expect(APIException.class);
    exceptionRule.expectMessage("IO exception Incorrect File Path");

    underTest.initializeItems("example_input");
  }

  @Test
  public void initializeItems_PackageWeightBeyondMaxLimit_Error() throws APIException {
    exceptionRule.expect(APIException.class);
    exceptionRule.expectMessage("Package Weight Beyond Max Limit");

    String filePath = getFilePathFromResources("example_input_package_weight_max_limit");
    underTest.initializeItems(filePath);
  }

  @Test
  public void initializeItems_CountOfItemsBeyondMaxLimit_Error() throws APIException {
    exceptionRule.expect(APIException.class);
    exceptionRule.expectMessage("Count of Items Beyond Max Limit");

    String filePath = getFilePathFromResources("example_input_items_count_max_limit");
    underTest.initializeItems(filePath);
  }

  @Test
  public void initializeItems_ItemWeightBeyondMaxLimit_Error() throws APIException {
    exceptionRule.expect(APIException.class);
    exceptionRule.expectMessage("Item Weight Beyond Max Limit");

    String filePath = getFilePathFromResources("example_input_item_weight_max_limit");
    underTest.initializeItems(filePath);
  }

  @Test
  public void initializeItems_ItemCostBeyondMaxLimit_Error() throws APIException {
    exceptionRule.expect(APIException.class);
    exceptionRule.expectMessage("Item Cost Beyond Max Limit");

    String filePath = getFilePathFromResources("example_input_item_cost_max_limit");
    underTest.initializeItems(filePath);
  }

  /**
   * Helper method to read the file path from resources for unit test purpose
   *
   * @param fileName - name of the file with the package data that need to be processed for unit testing
   * @return filePath - absolute path of the file
   */
  String getFilePathFromResources(String fileName) throws APIException {
    ClassLoader classloader = currentThread().getContextClassLoader();
    return new File(requireNonNull(classloader.getResource(fileName)).getFile()).getAbsolutePath();
  }
}
