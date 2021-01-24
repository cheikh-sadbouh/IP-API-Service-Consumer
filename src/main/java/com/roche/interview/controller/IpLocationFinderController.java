package com.roche.interview.controller;


import com.roche.interview.exception.ApiRequestException;
import com.roche.interview.service.IpLocationFinderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api/v1")
public class IpLocationFinderController {
    private static final Logger log = LoggerFactory.getLogger(IpLocationFinderController.class);

    @Autowired
    IpLocationFinderService ipLocationFinderService;

    @GetMapping(value = "/northern-countries", headers = "Accept=application/json")
    public ResponseEntity<Set<String>> getNorthernCountriesNames(@RequestParam("ip") final List<String> ipAddressList) {
        try {

            return new ResponseEntity<>(ipLocationFinderService.getNorthernCountries(ipAddressList), HttpStatus.OK);

        } catch (Exception e) {
            log.error("getNorthernCountriesNames({}): {}", ipAddressList, e.getMessage());
            throw new ApiRequestException(e.getMessage());
        }


    }
}
