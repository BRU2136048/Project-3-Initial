package com.example.streams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Demonstrates Stream API operations, defensive validation,
 * and custom exception handling.
 */
public class GentlyDownTheStream {

    protected List<String> fruits;
    protected List<String> veggies;
    protected List<Integer> integerValues;

    public GentlyDownTheStream() {
        this.fruits = new ArrayList<>(Arrays.asList(
                "Apple", "Orange", "Banana", "Pear", "Peach", "Tomato"
        ));

        this.veggies = new ArrayList<>(Arrays.asList(
                "Corn", "Potato", "Carrot", "Pea", "Tomato"
        ));

        this.integerValues = new Random()
                .ints(1000, 0, 1001)
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<String> sortedFruits() {
        validateStringCollection(fruits, "fruits");

        return fruits.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<String> sortedFruitsException() {
        validateStringCollection(fruits, "fruits");

        return fruits.stream()
                .filter(fruit -> fruit != null && !"Apple".equals(fruit))
                .sorted()
                .collect(Collectors.toList());
    }

    public List<String> sortedFruitsFirstTwo() {
        validateStringCollection(fruits, "fruits");

        return fruits.stream()
                .sorted()
                .limit(2)
                .collect(Collectors.toList());
    }

    public String commaSeparatedListOfFruits() {
        validateStringCollection(fruits, "fruits");

        return fruits.stream()
                .sorted()
                .collect(Collectors.joining(", "));
    }

    public List<String> reverseSortedVeggies() {
        validateStringCollection(veggies, "veggies");

        return veggies.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    public List<String> reverseSortedVeggiesInUpperCase() {
        validateStringCollection(veggies, "veggies");

        return veggies.stream()
                .sorted(Comparator.reverseOrder())
                .map(String::toUpperCase)
                .collect(Collectors.toList());
    }

    public List<Integer> topTen() {
        validateIntegerCollection(integerValues, "integerValues");

        return integerValues.stream()
                .sorted(Comparator.reverseOrder())
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Integer> topTenUnique() {
        validateIntegerCollection(integerValues, "integerValues");

        return integerValues.stream()
                .distinct()
                .sorted(Comparator.reverseOrder())
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Integer> topTenUniqueOdd() {
        validateIntegerCollection(integerValues, "integerValues");

        List<Integer> result = integerValues.stream()
                .filter(value -> value % 2 != 0)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .limit(10)
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            throw new NoSuchElementException("No odd values found in integerValues.");
        }

        return result;
    }

    public double average() {
        if (integerValues == null) {
            throw new IllegalArgumentException("integerValues collection cannot be null.");
        }

        return integerValues.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElseThrow(() ->
                        new InvalidDataException("Cannot compute average of an empty collection."));
    }

    protected void validateStringCollection(List<String> collection, String name) {
        if (collection == null) {
            throw new IllegalArgumentException(name + " collection cannot be null.");
        }
        if (collection.isEmpty()) {
            throw new InvalidDataException(name + " collection cannot be empty.");
        }
        if (collection.stream().anyMatch(item -> item == null || item.isBlank())) {
            throw new InvalidDataException(name + " collection contains null or blank values.");
        }
    }

    protected void validateIntegerCollection(List<Integer> collection, String name) {
        if (collection == null) {
            throw new IllegalArgumentException(name + " collection cannot be null.");
        }
        if (collection.isEmpty()) {
            throw new InvalidDataException(name + " collection cannot be empty.");
        }
        if (collection.stream().anyMatch(item -> item == null)) {
            throw new InvalidDataException(name + " collection contains null values.");
        }
    }
}