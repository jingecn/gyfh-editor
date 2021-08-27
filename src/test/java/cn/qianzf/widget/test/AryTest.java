package cn.qianzf.widget.test;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

public class AryTest {

    public static void main(String[] args) {
        ConcurrentHashMap m=new ConcurrentHashMap();

        Set<Long> s=new TreeSet<>();
        for(int i=0;i<2000000;i++){
            s.add(i+13000000000L);
        }
        System.out.println(s);
    }
}
