package com.mobiquity.packer.utils;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.models.Item;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility class to read file data and process it such that all the items associated to a package weight is mapped in map
 */
public class PackageWeightAndItemsEnricher {

  public static final int MAX_ITEM_WEIGHT = 100;
  public static final int MAX_ITEM_COST = 100;
  private static final int MAX_PACKAGE_WEIGHT = 100;
  private static final int MAX_ITEMS_AMOUNT = 15;

  /**
   * Helper method to process the file data
   * and initialize it in Map with key as weight of package
   * and value as associated Items.
   *
   * @param filePath - absolute path of the file with the package data that need to be processed
   * @return packageWeightAndRelatedItems - Map with key as weight of package and value as associated Items
   */
  public Map<String, List<Item>> initializeItems(String filePath) {
    Map<String, List<Item>> packageWeightAndRelatedItems = new LinkedHashMap<>();
    try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
      stream.forEach(s -> extractPackageWeightAndItems(packageWeightAndRelatedItems, s));
    } catch (IOException ioException) {
      throw new APIException("IO exception Incorrect File Path");
    }
    return packageWeightAndRelatedItems;
  }

  /**
   * Helper method to process the String
   * representing the row in provided file
   * and enrich it to Map with key as weight of package
   * and value as associated Items
   *
   * @param packageWeightAndRelatedItems - Map with key as weight of package and value as associated Items
   * @param s                            - String representing the row of file with Item and package weight details.
   */
  private void extractPackageWeightAndItems(Map<String, List<Item>> packageWeightAndRelatedItems,
      String s) {
    String[] packageWeightWithRelatedItems = s.split(":");
    String weight = packageWeightWithRelatedItems[0];
    validatePackageWeight(weight);
    String itemsRow = packageWeightWithRelatedItems[1];
    List<String> itemList = Arrays.asList(itemsRow.trim().split(" "));
    validateItemsCount(itemList.size());

    List<Item> items = getItems(itemList);

    //Sort the Items based on the max cost and less weight if cost is same
    List<Item> sortedItems = items.stream().sorted().collect(Collectors.toList());
    packageWeightAndRelatedItems.put(weight, sortedItems);
  }

  /**
   * Get Items from the current line of File data
   *
   * @param itemList - list of String each representing the item object data
   * @return items - List if Items populated from the current line of file data
   */
  private List<Item> getItems(List<String> itemList) {
    List<Item> items = new ArrayList<>();
    itemList.stream().map(item -> item.replaceAll("\\(", "").replaceAll("\\)", ""))
        .forEach(item -> {
          String[] itemAttributes = item.split(",");
          validateItemWeight(parseDouble(itemAttributes[1]));
          validateItemCost(parseInt(itemAttributes[2].substring(1)));
          items.add(new Item(parseInt(itemAttributes[0]),
              parseDouble(itemAttributes[1]),
              parseInt(itemAttributes[2].substring(1))));
        });
    return items;
  }


  private void validatePackageWeight(String weight) throws APIException {
    if (parseInt(weight.trim()) > MAX_PACKAGE_WEIGHT) {
      throw new APIException("Package Weight Beyond Max Limit");
    }
  }

  private void validateItemsCount(int itemCount) throws APIException {
    if (itemCount > MAX_ITEMS_AMOUNT) {
      throw new APIException("Count of Items Beyond Max Limit");
    }
  }

  private void validateItemWeight(Double itemWeight) throws APIException {
    if (itemWeight > MAX_ITEM_WEIGHT) {
      throw new APIException("Item Weight Beyond Max Limit");
    }
  }

  private void validateItemCost(int itemCost) throws APIException {
    if (itemCost > MAX_ITEM_COST) {
      throw new APIException("Item Cost Beyond Max Limit");
    }
  }

}
