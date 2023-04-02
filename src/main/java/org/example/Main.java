package org.example;

import org.example.entity.MatchElement;
import org.example.entity.ValueSimilarity;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        // TODO: 2023/4/2 创建两个ArrayList，并给两个ArrayList赋值随机字母
        List<Character> list1 = new ArrayList<>(10);
        List<Integer> list2 = new ArrayList<>(10);
        for(int i = 0;i < 10;i++){
            list1.add((char)('A'+(int)(Math.random() * 26)));
            list2.add((int)(Math.random() * 100));
        }

        // TODO: 2023/4/2 创建map用于保存每两个元素的相似度，每两个元素的相似度用100以内的随机数
        Map<String,Integer> similarity = new HashMap<>();
        for(int i = 0;i < list1.size();i++){
            for(int j = 0;j < list2.size();j++){
                if(!similarity.containsKey(""+list1.get(i) + list2.get(j))){
                    similarity.put(""+list1.get(i) + list2.get(j),(int)(Math.random() * 101));
                }
            }
        }

        // TODO: 2023/4/2 数组相似度map
        for(Map.Entry<String,Integer> entry : similarity.entrySet()){
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }

        // TODO: 2023/4/2 规定list1中的一个元素最多匹配n个list2中的元素
        int maxMatchCount = 2;

        // TODO: 2023/4/2 一对多匹配 list2不能重复
        List<MatchElement> list3 = new ArrayList<>(10);
        Map<Integer,Integer> isMatchMap = new HashMap<>();
        for(int i = 0;i < list1.size();i++) {
            MatchElement<Character> element = new MatchElement<>();
            element.setValue(new ArrayList<>());
            element.setKey(list1.get(i));
            list3.add(element);
            for (Map.Entry<String, Integer> entry : similarity.entrySet()) {
                if(entry.getKey().charAt(0) == list1.get(i)){
                    if(element.getValue().size() == 0){
                        int match = Integer.parseInt(entry.getKey().substring(1));
                        ValueSimilarity valueSimilarity = new ValueSimilarity();
                        valueSimilarity.setValue(match);
                        valueSimilarity.setSimilarity(entry.getValue());
                        if(!isMatchMap.containsKey(match)){
                            element.getValue().add(valueSimilarity);
                            isMatchMap.put(match, 1);
                        }else if(isMatchMap.get(match) < 1){
                            element.getValue().add(valueSimilarity);
                            isMatchMap.put(match, isMatchMap.get(match) + 1);
                        }
                    }else if(entry.getValue() > element.getValue().get(element.getValue().size() - 1).getSimilarity()){
                        int match = Integer.parseInt(entry.getKey().substring(1));
                        ValueSimilarity valueSimilarity = new ValueSimilarity();
                        valueSimilarity.setValue(match);
                        valueSimilarity.setSimilarity(entry.getValue());
                        if(!isMatchMap.containsKey(match)){
                            element.getValue().add(valueSimilarity);
                            isMatchMap.put(match, 1);
                        }else if(isMatchMap.get(match) < 1){
                            element.getValue().add(valueSimilarity);
                            isMatchMap.put(match, isMatchMap.get(match) + 1);
                        }
                        List<ValueSimilarity> collect = element.getValue().stream().sorted(Comparator.comparing(ValueSimilarity::getSimilarity).reversed()).collect(Collectors.toList());
                        if(collect.size() > maxMatchCount){
                            isMatchMap.put(collect.get(collect.size() - 1).getValue(),Math.min(isMatchMap.get(collect.get(collect.size() - 1).getValue()) - 1,0));
                            collect = collect.subList(0,collect.size()-1);
                        }
                        element.setValue(collect);
                    }
                }
            }
        }

        // TODO: 2023/4/2 输出匹配结果，并附上每对的相似度
        for(int i = 0;i < list3.size();i++){
            System.out.println(list3.get(i));
        }
    }
}