package com.hdbsoft.annotation.repeatable;

import java.lang.annotation.Repeatable;

@Repeatable(Container.class)
@interface Product {
    String value();
}
