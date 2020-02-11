package com.hdbsoft.annotation.repeatable;

import java.lang.annotation.Annotation;
import java.util.Arrays;

@Product("ramen")
@Product("noodle")
public class RepeatableTest {

    public static void main(String[] args) {
        Annotation[] annotations = RepeatableTest.class.getAnnotations();
        for(Annotation anno : annotations) {
            if(anno instanceof Container) {
                Container container = (Container) anno;
            }
            System.out.println(anno);
        }


//        Container container = RepeatableTest.class.getAnnotation(Container.class);
//        System.out.println(container);
//        for(Product product : container.value()) {
//            System.out.println(product.value());
//        }
    }

}
