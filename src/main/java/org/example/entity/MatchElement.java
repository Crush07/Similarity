package org.example.entity;

import lombok.Data;

import java.util.List;

@Data
public class MatchElement<T> {

    T key;

    List<ValueSimilarity> value;
}
