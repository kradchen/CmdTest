package com.company;

import java.util.*;

public class SimpleCollectionTest {
    static void collectionsTest(){
        List<String> aryList = new ArrayList<>(Country.names(20));
        List<String> linList =  new LinkedList<>(Country.names(20));
        System.out.printf("ArrayList Printing...");
        System.out.printf(aryList.toString());
        System.out.println();
        Collections.sort(aryList,Collections.reverseOrder());
        System.out.printf("Sorted ArrayList Printing...");
        System.out.printf(aryList.toString());
        System.out.println();
        System.out.printf("LinkList Printing...");
        System.out.printf(linList.toString());
        System.out.println();
        Collections.shuffle(linList);
        System.out.printf("Shuffle LinkList Printing...");
        System.out.printf(linList.toString());
        System.out.println();
        Map<String,String> conMap = Country.map;
        System.out.printf("Map Printing...");
        System.out.printf(conMap.toString());
        System.out.println();
        Map<String,String> newMap = new HashMap<>();
        for(String name: conMap.keySet())
        {
            if(name.startsWith("A")){
                newMap.put(name,conMap.get(name));
            }
        }
        System.out.printf("Selected Map Printing...");
        System.out.printf(newMap.toString());
        System.out.println();


        List<String> txtList = new LinkedList<>();
        TextFileGenerator tfg = new TextFileGenerator();
        while (tfg.hasNext()) {
            txtList.add(tfg.next());
        }
        System.out.printf("Text file content Printing...");
        System.out.printf(txtList.toString());
        System.out.println();
    }
    static void HashTest()
    {
        String[] keyVals = {"A1","B2","C3","D4","F5","G6","H7","I8","H9","O10"};
        String[] vals ={"AAA","BB","CCC","DDDD","F5","GSix","H7","IEight","HNine","-10"};
        HashMap<String, String> hashMap = new HashMap<>();
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>(10,10,true);
        for (int i = 0; i < keyVals.length; i++ )
        {
            hashMap.put(keyVals[i], vals[i]);
            linkedHashMap.put(keyVals[i], vals[i]);
        }
        System.out.println(hashMap);
        System.out.println(linkedHashMap);
        System.out.println("Print Key Set!");
        System.out.println(linkedHashMap);
        hashMap = linkedHashMap;
        linkedHashMap.get("D4");
        System.out.println("Print Key Set after get D4!");
        System.out.println(hashMap);
        System.out.println(linkedHashMap);
    }
}
