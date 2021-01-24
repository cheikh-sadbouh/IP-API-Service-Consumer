package com.roche.interview.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roche.interview.exception.ApiRequestException;
import com.roche.interview.validation.UserInputValidator;
import com.roche.interview.validation.ValidationResult;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static com.roche.interview.validation.UserInputValidator.hasNotEmptyValues;
import static com.roche.interview.validation.UserInputValidator.hasValidIps;

@Service
public class CountryIpLocationFinderService implements IpLocationFinderService {
    private static final Logger log = LoggerFactory.getLogger(CountryIpLocationFinderService.class);
    private static final String url = "http://ip-api.com/json/";
    @Autowired
    RestTemplate restTemplate;

    @Override
    public Set<String> getNorthernCountries(List<String> countryList) {

        Set<String> northernCountries = new TreeSet<>();
        ValidationResult validationResult = UserInputValidator
                .hasAppropriateSize()
                .and(hasNotEmptyValues())
                .and(hasValidIps())
                .apply(Optional.of(countryList));

        if (validationResult.equals(ValidationResult.VALID_INPUT)) {
            northernCountries = countryList.stream()
                    .filter(this::isNorthernCountry)
                    .map(this::findCountryNameByIp)
                    .collect(Collectors.toCollection(TreeSet::new));
        }
        if (northernCountries.isEmpty()) northernCountries.add("No country found");

        return northernCountries;
    }

    @Override
    public Boolean isNorthernCountry(String countryIp) {
        return (getIpInfo(countryIp).getFloat("lat") > 0);
    }

    @Override
    public String findCountryNameByIp(String countryIp) {
        return getIpInfo(countryIp).getString("country");

    }

    @Override
    public JSONObject getIpInfo(String countryIp) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(
                    new ObjectMapper().
                            writeValueAsString(restTemplate.getForObject(url + countryIp, Object.class)));

        } catch (JsonProcessingException e) {
            log.error("getIpInfo({}): {}", countryIp, e.getMessage());
            throw new ApiRequestException(e.getMessage());
        }
        return jsonObject;
    }
}
