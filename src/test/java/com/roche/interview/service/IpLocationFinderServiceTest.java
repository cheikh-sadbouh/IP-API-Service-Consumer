package com.roche.interview.service;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IpLocationFinderServiceTest {
    @SpyBean
    CountryIpLocationFinderService countryIpLocationFinderService;

    @Test
    public void Should_Return_Country_Name_List_When_Country_Is_From_Northern_Hemisphere() {
        assertThat(countryIpLocationFinderService.getNorthernCountries(Arrays.asList("41.221.221.3"))).contains("Mauritania");
    }

    @Test
    public void Should_Return_No_Country_Found_When_Country_Is_Not_From_Northern_Hemisphere() {
        assertThat(countryIpLocationFinderService.getNorthernCountries(Arrays.asList("119.18.2.212"))).contains("No country found");
    }

    @Test
    public void Should_Return_True_When_Country_Is_From_Northern_Hemisphere() {
        assertThat(countryIpLocationFinderService.isNorthernCountry("41.221.221.3")).isTrue();
    }

    @Test
    public void Should_Return_False_When_Country_Is_Not_From_Northern_Hemisphere() {
        assertThat(countryIpLocationFinderService.isNorthernCountry("119.18.2.212")).isFalse();
    }

    @Test
    public void Should_Return_Country_Name_When_Country_IP_Is_Provided() {
        assertThat(countryIpLocationFinderService.findCountryNameByIp("119.18.2.212")).isEqualTo("Australia");
    }

    @Test
    public void Should_Return_IP_info_When_Country_IP_Is_Provided() {
        assertThat(countryIpLocationFinderService.getIpInfo("119.18.2.212")).isNotNull().extracting(JSONObject::toString).toString().contains("Australia");
    }

}
