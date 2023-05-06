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

        for(int i = 0;i < 40;i++){
            while (true){
                int v = (int)(Math.random() * 1000);
                if(list2.stream().noneMatch(s -> s == v)){
                    list2.add(v);
                    break;
                }
            }
        }

        System.out.println(list2);

        // TODO: 2023/4/2 创建map用于保存每两个元素的相似度，每两个元素的相似度用100以内的随机数
        match(list1,list2);
    }

    static <T,C> void match(List<T> list1, List<C> list2){

        List<Map<T,List<ValueSimilarity<C>>>> allSimilarity = new ArrayList<>();
        HashMap<T,List<MatchElement<T>>> map = new HashMap<>();
        int eachSize = 2;
        for(int c = 0;c < eachSize;c++){
            List<C> l = new ArrayList<>();
            for(int d = c;d < list2.size();d += eachSize){
                l.add(list2.get(d));
            }
            getResult(list1,l,map,allSimilarity);
        }
        List<Map.Entry<T,List<MatchElement<T>>>> list = new LinkedList<>(map.entrySet());

        list = list.stream().sorted((v1,v2) -> {

            List<MatchElement<T>> value1 = v1.getValue();
            double max1 = 0;
            HashSet<Object> diff1 = new HashSet<>();
            for (int i = 0; i < value1.size(); i++) {
                if(max1 < value1.get(i).getValue().get(0).getSimilarity()){
                    max1 = value1.get(i).getValue().get(0).getSimilarity();
                }
                diff1.add(value1.get(i).getValue().get(0).getValue());
            }


            List<MatchElement<T>> value2 = v2.getValue();
            double max2 = 0;
            HashSet<Object> diff2 = new HashSet<>();
            for (int i = 0; i < value2.size(); i++) {
                if(max2 < value2.get(i).getValue().get(0).getSimilarity()){
                    max2 = value2.get(i).getValue().get(0).getSimilarity();
                }
                diff2.add(value2.get(i).getValue().get(0).getValue());
            }

            if((max1 - (diff1.size() - 1) * eachSize * 100) == (max2 - (diff2.size() - 1) * eachSize * 100)){
                return 0;
            }else if((max1 - (diff1.size() - 1) * eachSize * 100) > (max2 - (diff2.size() - 1) * eachSize * 100)){
                return -1;
            }else{
                return 1;
            }

        }).collect(Collectors.toList());

        list.forEach(System.out::println);

        for(int i = 0;i < list.size() - 1;i++){
            List<MatchElement<T>> value = list.get(i).getValue();
            int max = 0;
            for(int j = 0;j < value.size();j++){
                MatchElement<T> element = value.get(j);
                //查找key匹配的最相似且，没有加入过matchSet的value
                if(value.get(max).getValue().get(0).getSimilarity() < element.getValue().get(0).getSimilarity()){
                    max = j;
                }
            }

            List<C> valueForKey = new ArrayList<>();
            for(int j = max % eachSize;j < list2.size();j += eachSize){
                if(list2.get(j).equals(value.get(max).getValue().get(0).getValue())){
                    for(int k = j - j % eachSize;k < j - j % eachSize + eachSize;k++){
                        valueForKey.add(list2.get(k));
                    }
                    break;
                }
            }
            System.out.println(valueForKey);

            for(int k = 0;k < valueForKey.size();k++){
                for(int j = i + 1;j < list.size();j++){
                    if(list.get(j).getValue().get(k).getValue().get(0).getValue().equals(valueForKey.get(k))){
                        int nowSimilarities = 0;
                        List<ValueSimilarity<C>> valueSimilarities = allSimilarity.get(k).get(list.get(i).getKey());
                        for(int l = 0;l < valueSimilarities.size();l++){
                            if(valueSimilarities.get(l).getValue().equals(valueForKey.get(k))){
                                nowSimilarities = valueSimilarities.get(l).getSimilarity();
                                break;
                            }
                        }
                        int anotherSimilarities = 0;
                        List<ValueSimilarity<C>> anotherValueSimilarities = allSimilarity.get(k).get(list.get(j).getKey());
                        for(int l = 0;l < anotherValueSimilarities.size();l++){
                            if(anotherValueSimilarities.get(l).getValue().equals(list.get(i).getValue().get(k).getValue().get(0).getValue())){
                                anotherSimilarities = anotherValueSimilarities.get(l).getSimilarity();
                                break;
                            }
                        }

                        list.get(j).getValue().get(k).getValue().get(0).setValue(list.get(i).getValue().get(k).getValue().get(0).getValue());
                        list.get(j).getValue().get(k).getValue().get(0).setSimilarity(anotherSimilarities);

                        list.get(i).getValue().get(k).getValue().get(0).setValue(valueForKey.get(k));
                        list.get(i).getValue().get(k).getValue().get(0).setSimilarity(nowSimilarities);

                        break;
                    }
                }
            }
        }

        list.forEach(System.out::println);
    }

    static <T,C> void getResult(List<T> list1, List<C> list2, HashMap<T,List<MatchElement<T>>> map,List<Map<T,List<ValueSimilarity<C>>>> allSimilarity){

        Map<T,List<ValueSimilarity<C>>> similarity = new HashMap<>();
        for(int i = 0;i < list1.size();i++){
            for(int j = 0;j < list2.size();j++){
                ValueSimilarity<C> valueSimilarity = new ValueSimilarity<>();
                valueSimilarity.setSimilarity((int)(Math.random() * 101));
                valueSimilarity.setValue(list2.get(j));
                if(!similarity.containsKey(list1.get(i))){
                    List<ValueSimilarity<C>> data = new ArrayList<>();
                    data.add(valueSimilarity);
                    similarity.put(list1.get(i),data);
                }else{
                    similarity.get(list1.get(i)).add(valueSimilarity);
                }
            }
        }

        allSimilarity.add(similarity);

        // TODO: 2023/4/5 每一个 ValueSimilarity按照相似度排序
        for(Map.Entry<T,List<ValueSimilarity<C>>> entry : similarity.entrySet()){
            entry.setValue(entry.getValue().stream().sorted(Comparator.comparing(ValueSimilarity<C>::getSimilarity).reversed()).collect(Collectors.toList()));
        }

        // TODO: 2023/4/2 一对多匹配 list2不能重复
        Set<T> isMatchChar = new HashSet<>();
        Set<C> isMatch = new HashSet<>();
        List<MatchElement<T>> list3 = new ArrayList<>(10);
        for(int i = 0;i < list1.size();i++){
            ValueSimilarity<C> maxSimilarityElement = new ValueSimilarity<>();
            int maxSimilarity = 0;
            T maxSimilarityChar = null;
            for(Map.Entry<T,List<ValueSimilarity<C>>> entry : similarity.entrySet()){
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

            MatchElement<T> element = new MatchElement<>();
            element.setKey(maxSimilarityChar);
            element.setValue(new ArrayList<>());
            element.getValue().add(maxSimilarityElement);
            list3.add(element);
        }

        // TODO: 2023/4/2 输出匹配结果，并附上每对的相似度
        for(int i = 0;i < list3.size();i++){
            List<MatchElement<T>> matchElements = map.getOrDefault(list3.get(i).getKey(),new ArrayList<>());
            matchElements.add(list3.get(i));
            map.put(list3.get(i).getKey(),matchElements);
        }
    }
}