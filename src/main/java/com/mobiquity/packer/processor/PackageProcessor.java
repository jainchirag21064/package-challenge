package com.mobiquity.packer.processor;

import static java.lang.Double.parseDouble;

import com.mobiquity.packer.models.Item;
import com.mobiquity.packer.utils.PackageWeightAndItemsEnricher;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Processor class with the logic of processing the Items from the provided file.
 */
public class PackageProcessor extends PackageProcessorTemplate {


  private final PackageWeightAndItemsEnricher packageWeightAndItemsEnricher = new PackageWeightAndItemsEnricher();

  @Override
  Map<String, List<Item>> readFileData(String filePath) {
    return packageWeightAndItemsEnricher.initializeItems(filePath);
  }

  @Override
  Map<String, List<Item>> processSortedItems(Map<String, List<Item>> packageWeightAndRelatedItems) {

    Map<String, List<Item>> finalPackage = new LinkedHashMap<>();
    packageWeightAndRelatedItems.forEach((k, v) -> {
      //For each packageWeight we will evaluate the items to be selected with optimum cost
      List<Item> finalPackageList = new ArrayList<>();
      evaluateWhatToPick(parseDouble(k), v, finalPackageList, v.size());
      finalPackage.put(k, finalPackageList);
    });
    return finalPackage;

  }

  /**
   * This is a recursive helper method where we recursively evaluate
   * what will be the cost we will acquire in case the given item is picked
   *
   * @param capacity      - Initially it is the capacity of the package but as the method is called recursively the value is reduced by the amount of items which we already added
   * @param sortedItems   - collection of packageWeight and its associated list of items sorted by max cost
   * @param finalPackage- collection of packageWeight and its associated list of items which we need to pick to acquire the optimum cost
   * @param currentIndex- represent index of items which is under process
   * @return cost - max cost when a given items is either picked or excluded
   */
  private int evaluateWhatToPick(Double capacity, List<Item> sortedItems, List<Item> finalPackage,
      int currentIndex) {
    //We cant add if the capacity itself is zero or we are out of items
    if (capacity == 0 || currentIndex == 0) {
      return 0;
    }

    //if the item weight is greater then capacity then we can not add it in package
    // skip the item and try adding the next item in list.
    if (sortedItems.get(currentIndex - 1).getWeight() > capacity) {
      return evaluateWhatToPick(capacity, sortedItems, finalPackage, currentIndex - 1);
    } else {
      List<Item> incItem = new ArrayList<>();
      List<Item> excItem = new ArrayList<>();
      int incCost = sortedItems.get(currentIndex - 1).getCost() + evaluateWhatToPick(
          capacity - sortedItems.get(currentIndex - 1).getWeight(), sortedItems, incItem,
          currentIndex - 1);
      int excCost = evaluateWhatToPick(capacity, sortedItems, excItem, currentIndex - 1);
      if (incCost > excCost) {
        finalPackage.addAll(incItem);
        finalPackage.add(sortedItems.get(currentIndex - 1));
        return incCost;
      } else {
        finalPackage.addAll(excItem);
        return excCost;
      }
    }
  }

  String result(Map<String, List<Item>> finalPackage) {
    var indexList = new StringBuilder();
    finalPackage.forEach((packageWeight, listOfItems) -> {
      if (listOfItems.isEmpty()) {
        indexList.append("-\n");
      } else {
        listOfItems.forEach(value -> indexList.append(value.getIndex()).append(","));
        indexList.deleteCharAt(indexList.length() - 1);
        indexList.append("\n");
      }
    });
    return indexList.toString();
  }

}
