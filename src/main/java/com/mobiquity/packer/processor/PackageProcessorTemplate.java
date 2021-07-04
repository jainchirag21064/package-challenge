package com.mobiquity.packer.processor;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.models.Item;
import java.util.List;
import java.util.Map;

/**
 * Base Class which act as Template that defines a sequence of steps a.k.a algorithm to solve the packaging problem and
 * allows the sub class to override the steps without changing the sequence of steps.
 * Also referred as Behavioral Pattern : Template Method
 */
public abstract class PackageProcessorTemplate {

  /**
   * Method defining the Sequence of steps that is required to solve the given problem
   * - Get the File and Process the data in the file and initialize it to Map of Items
   * -
   *
   * @param filePath - name of the file with the package data that need to be processed
   * @return items index in comma separated string
   * @throws APIException - All exception is wrapped to this Exception class
   */
  public final String pack(String filePath) throws APIException {
    //Step 1 read file data and process it such that all the items associated to a package weight is mapped in map
    Map<String, List<Item>> packageWeightAndRelatedItems = readFileData(filePath);

    //Step 2 Process the sorted Map to get the result of most valued item in package
    Map<String, List<Item>> finalPackage = processSortedItems(packageWeightAndRelatedItems);

    //Step 3 Get the Comma separated Items index number.
    return result(finalPackage);
  }

  /**
   * The Subclass need to implement this method
   * so different client are not restricted
   * to use the file in strict format.
   *
   * @param filePath - absolute path of the file with the package data that need to be processed
   * @return packageWeightAndRelatedItems - Map with key as weight of package and value as associated Items
   */
  abstract Map<String, List<Item>> readFileData(String filePath);

  /**
   * Definition of processing method
   * The actual logic of processing the items goes here.
   * Subclass need to override the below method to choose
   * how they want to implement the solution based on different constraints
   *
   * @param packageWeightAndRelatedItems - Map of package weight and its associated List of Items
   * @return finalPackage - Map of package weight and the list of items which can be added to the package
   */
  abstract Map<String, List<Item>> processSortedItems(
      Map<String, List<Item>> packageWeightAndRelatedItems);

  /**
   * Definition of result method
   * Subclass need to implement the logic of how to get the results
   * e.g Get the Items Index or get the max profit for the solution
   *
   * @param finalPackage - Map of package weight and the list of items which can be added to the package
   * @return result in string
   */
  abstract String result(Map<String, List<Item>> finalPackage);
}
