package com.eu.client.registration.service.country

import com.eu.client.registration.restcountries.CountryBean
import com.eu.client.registration.restcountries.RestCountriesApi
import spock.lang.Specification
import spock.lang.Subject

import static java.lang.String.format

class CountryServiceSpec extends Specification {

    RestCountriesApi restCountriesApi = Mock(RestCountriesApi)

    CountryBean firstCountryBean = Mock(CountryBean)
    CountryBean secondCountryBean = Mock(CountryBean)
    CountryBean thirdCountryBean = Mock(CountryBean)


    @Subject
    CountryService service = new CountryService(restCountriesApi)

    void "should find the shortest way between countries"() {
        given:
            firstCountryBean.getAlpha3Code() >> 'A'
            firstCountryBean.getBorders() >> List.of('B')

            secondCountryBean.getAlpha3Code() >> 'B'
            secondCountryBean.getBorders() >> List.of('A','C')

            thirdCountryBean.getAlpha3Code() >> 'C'
            thirdCountryBean.getBorders() >> List.of('B')

            List<CountryBean> countries = List.of(firstCountryBean, secondCountryBean, thirdCountryBean)
        when:
            ShortestPathDto dto = service.findTheShortestWay('A','C')
        then:
            1 * restCountriesApi.fetchAllCountries() >> countries
            assert dto.path == '[(A : B), (B : C)]'
    }


    void "should not find a way and throw CountriesPathNotFoundException ex"() {
        given:
            firstCountryBean.getAlpha3Code() >> 'A'
            firstCountryBean.getBorders() >> List.of()

            secondCountryBean.getAlpha3Code() >> 'B'
            secondCountryBean.getBorders() >> List.of()

            List<CountryBean> countries = List.of(firstCountryBean, secondCountryBean)
        when:
            service.findTheShortestWay('A','B')
        then:
            1 * restCountriesApi.fetchAllCountries() >> countries
            CountriesPathNotFoundException notFoundEx = thrown(CountriesPathNotFoundException)
            notFoundEx.message == format(CountriesPathNotFoundException.MESSAGE_TEMPLATE, 'A', 'B')
    }

    void "should not find a way and throw CountryIncorrectInputException ex"() {
        given:
            firstCountryBean.getAlpha3Code() >> 'A'
            firstCountryBean.getBorders() >> List.of()

            secondCountryBean.getAlpha3Code() >> 'B'
            secondCountryBean.getBorders() >> List.of()

            List<CountryBean> countries = List.of(firstCountryBean, secondCountryBean)
        when:
            service.findTheShortestWay('Z','B')
        then:
            1 * restCountriesApi.fetchAllCountries() >> countries
            CountryIncorrectInputException notFoundEx = thrown(CountryIncorrectInputException)
    }

}
