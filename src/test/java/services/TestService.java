package services;

import fr.swansky.ioccontainer.annotations.Autowired;
import fr.swansky.ioccontainer.annotations.Service;

@Service
public class TestService {
    @Autowired
    public TestService() {
        System.out.println("test");
    }

}
