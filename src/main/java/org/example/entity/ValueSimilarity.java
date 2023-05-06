package org.example.entity;

import lombok.Data;

@Data
public class ValueSimilarity<T> {

    T value;

    Integer similarity;

}