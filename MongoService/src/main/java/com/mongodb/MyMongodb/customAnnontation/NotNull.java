package com.mongodb.MyMongodb.customAnnontation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
public @interface NotNull {
}
