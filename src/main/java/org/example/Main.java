package org.example;

import org.example.entity.MatchElement;
import org.example.entity.ValueSimilarity;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        // TODO: 2023/4/2 创建两个ArrayList，并给两个ArrayList赋值随机字母
        List<Character> list1 = new ArrayList<>(20);
        List<Integer> list2 = new ArrayList<>(10);

        for(int i = 0;i < 20;i++){
            while(true){
                char v = (char)('A'+(int)(Math.random() * 26));
                if(list1.stream().noneMatch(s -> s == v)) {
                    list1.add(v);
                    break;
                }
            }
        }

        for(int i = 0;i < 10;i++){
            while (true){
                int v = (int)(Math.random() * 100);
                if(list2.stream().noneMatch(s -> s == v)){
                    list2.add(v);
                    break;
                }
            }
        }

        // TODO: 2023/4/2 创建map用于保存每两个元素的相似度，每两个元素的相似度用100以内的随机数
        Map<Character,List<ValueSimilarity>> similarity = new HashMap<>();
        for(int i = 0;i < list1.size();i++){
            for(int j = 0;j < list2.size();j++){
                ValueSimilarity valueSimilarity = new ValueSimilarity();
                valueSimilarity.setSimilarity((int)(Math.random() * 101));
                valueSimilarity.setValue(list2.get(j));
                if(!similarity.containsKey(list1.get(i))){
                    List<ValueSimilarity> data = new ArrayList<>();
                    data.add(valueSimilarity);
                    similarity.put(list1.get(i),data);
                }else{
                    similarity.get(list1.get(i)).add(valueSimilarity);
                }
            }
        }

        // TODO: 2023/4/2 数组相似度map
        for(Map.Entry<Character,List<ValueSimilarity>> entry : similarity.entrySet()){
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }

        // TODO: 2023/4/5 每一个 ValueSimilarity按照相似度排序
        for(Map.Entry<Character,List<ValueSimilarity>> entry : similarity.entrySet()){
            entry.setValue(entry.getValue().stream().sorted(Comparator.comparing(ValueSimilarity::getSimilarity).reversed()).collect(Collectors.toList()));
        }

        // TODO: 2023/4/2 一对多匹配 list2不能重复
        Set<Character> isMatchChar = new HashSet<>();
        Set<Integer> isMatch = new HashSet<>();
        List<MatchElement<Character>> list3 = new ArrayList<>(10);
        for(int i = 0;i < list1.size();i++){
            ValueSimilarity maxSimilarityElement = new ValueSimilarity();
            int maxSimilarity = 0;
            char maxSimilarityChar = '0';
            for(Map.Entry<Character,List<ValueSimilarity>> entry : similarity.entrySet()){
                if(isMatchChar.contains(entry.getKey())){
                    continue;
                }
                for(int j = 0;j < entry.getValue().size();j++){
                    if(!isMatch.contains(entry.getValue().get(j).getValue())){
                        if(maxSimilarity < entry.getValue().get(j).getSimilarity()){
                            maxSimilarityElement = entry.getValue().get(j);
                            maxSimilarity = entry.getValue().get(j).getSimilarity();
                            maxSimilarityChar = entry.getKey();
                            break;
                        }
                    }
                }
            }
            isMatchChar.add(maxSimilarityChar);
            isMatch.add(maxSimilarityElement.getValue());

            MatchElement<Character> element = new MatchElement<>();
            element.setKey(maxSimilarityChar);
            element.setValue(new ArrayList<>());
            element.getValue().add(maxSimilarityElement);
            list3.add(element);
        }

        // TODO: 2023/4/2 输出匹配结果，并附上每对的相似度
        for(int i = 0;i < list3.size();i++){
            System.out.println(list3.get(i));
        }
    }
}