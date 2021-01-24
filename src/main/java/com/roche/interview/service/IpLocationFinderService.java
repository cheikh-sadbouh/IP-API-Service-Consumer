package com.roche.interview.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONObject;

import java.util.List;
import java.util.Set;

public interface IpLocationFinderService {
    Set<String> getNorthernCountries(List<String> countryList);

    Boolean isNorthernCountry(String countryIp);

    String findCountryNameByIp(String countryIp);

    JSONObject getIpInfo(String countryIp) throws JsonProcessingException;

}
