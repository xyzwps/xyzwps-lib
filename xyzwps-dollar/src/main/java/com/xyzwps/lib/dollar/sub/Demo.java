package com.xyzwps.lib.dollar.sub;

import java.util.List;

public class Demo {


    public static void main(String[] args) {
        var list = List.of(1, 2, 3, 4);
        var observable = new Observable<Integer>((observer) -> {
            list.forEach(observer::next);
            observer.complete();
            observer.next(1);
            return () -> System.out.println("Clear");
        });

        var subscription = observable.subscribe(new Observer<>() {
            @Override
            public void next(Integer value) {
                System.out.println(value);
            }

            @Override
            public void error(Exception ex) {
                System.out.println("Error");
            }

            @Override
            public void complete() {
                System.out.println("Done");
            }
        });

        subscription.unsubscribe();


    }
}
