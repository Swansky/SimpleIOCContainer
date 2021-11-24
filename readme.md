# **Simple IOC Container**
- Work with @Service annotation
- Possible to add custom annotation with configuration



## **How to add custom annotations**
Create Annotation with Retention policy runtime.
```java
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface CustomAnnotation {
    }
 ```

create config and add to this Annotation class.
```java
   public static void main(String[] args) {
        try {
            SwansIOCConfig swansIOCConfig = new SwansIOCConfig();
            swansIOCConfig.addCustomAnnotation(CustomAnnotation.class);

            SwansIOC swansIOC = SwansIOC.InitIOC(RadioRythm.class,swansIOCConfig);
            swansIOC.startIOC();
            
            Set<ServiceDetails> serviceWithAnnotation = getServiceWithAnnotation(CustomAnnotation.class);
            System.out.println("classes with customAnnotation : "+serviceWithAnnotation);
        } catch (InstanceCreationException e) {
            e.printStackTrace();
        }
    }
   ```


Api [SwansApi](https://github.com/Swansky/SwansAPI)

Used in [RadioRythm](https://github.com/Swansky/RadioRythm)
