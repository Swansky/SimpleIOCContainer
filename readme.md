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
            run(SwansIOC.class,swansIOCConfig);
            Set<ServiceDetails> serviceWithAnnotation = getServiceWithAnnotation(CustomAnnotation.class);
            System.out.println("classes with customAnnotation : "+serviceWithAnnotation);
        } catch (InstanceCreationException e) {
            e.printStackTrace();
        }
    }
   ```
