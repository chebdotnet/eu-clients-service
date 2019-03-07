package com.eu.client.registration.service.country

import com.eu.client.registration.domain.Country
import com.eu.client.registration.domain.CountryRepository
import com.eu.client.registration.restcountries.CountryBean
import com.eu.client.registration.restcountries.RestCountriesApi
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

import static java.lang.String.format
import static java.util.Optional.empty

class CountryServiceSpec extends Specification {

    @Shared
    static final String MOCK_COUNTRY_CODE = 'lv'

    static final Long MOCK_COUNTRY_POPULATION = 1961600

    static final BigDecimal MOCK_COUNTRY_AREA = 64559

    List MOCK_COUNTRY_BORDERS = List.of("BLR", "EST", "LTU", "RUS")

    RestCountriesApi restCountriesApi = Mock(RestCountriesApi)
    CountryRepository repository = Mock(CountryRepository)

    CountryBean firstCountryBean = Mock(CountryBean)
    CountryBean secondCountryBean = Mock(CountryBean)
    CountryBean thirdCountryBean = Mock(CountryBean)

    CountryBean countryBean = Mock(CountryBean)
    Country country = Mock(Country)

    @Subject
    CountryService service = new CountryService(restCountriesApi, repository)

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


    void "should get country when the country already exists in db and not capture country from 'restcountries' service"() {
        when:
            service.captureCountry(MOCK_COUNTRY_CODE)
        then:
            1 * repository.findByCode(MOCK_COUNTRY_CODE) >> Optional.of(country)
            0 * restCountriesApi.fetchCountryByCountryCode(MOCK_COUNTRY_CODE)
            0 * repository.save(_ as Country)
    }

    void "should capture country from 'restcountries' service and save it in db"() {
        given:
            countryBean.population >> MOCK_COUNTRY_POPULATION
            countryBean.area >> MOCK_COUNTRY_AREA
            countryBean.borders >> MOCK_COUNTRY_BORDERS
        when:
            service.captureCountry(MOCK_COUNTRY_CODE)
        then:
            1 * repository.findByCode(MOCK_COUNTRY_CODE) >> empty()
            1 * restCountriesApi.fetchCountryByCountryCode(MOCK_COUNTRY_CODE) >> countryBean
            1 * repository.save(_ as Country)
    }

}
