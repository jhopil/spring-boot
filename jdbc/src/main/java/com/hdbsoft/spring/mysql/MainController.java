package com.hdbsoft.spring.mysql;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping(path="/city")
public class MainController {

    @Resource //@Autowired
    private UserRepository userRepository;

    @GetMapping(path="/add")
    public @ResponseBody
    String addCity(@RequestParam String name,
                   @RequestParam String countryCode) {
        City city = new City();
        city.name = name;
        city.countryCode = countryCode;
        city.district = name;
        city.population = 123456;
        userRepository.save(city);
        return "saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<City> getAllCities() {
        return userRepository.findAll();
    }
}
