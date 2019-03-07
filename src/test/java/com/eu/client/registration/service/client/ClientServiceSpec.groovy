package com.eu.client.registration.service.client

import com.eu.client.registration.domain.Client
import com.eu.client.registration.domain.Country
import com.eu.client.registration.domain.ClientRepository
import com.eu.client.registration.domain.CountryRepository
import com.eu.client.registration.restcountries.CountryBean
import com.eu.client.registration.restcountries.RestCountriesApi
import com.eu.client.registration.service.client.converters.ToClient
import com.eu.client.registration.service.client.converters.ToClientDto
import com.eu.client.registration.service.client.validators.ClientRegisterValidator
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

import static java.util.Optional.of

class ClientServiceSpec extends Specification {

    @Shared
    static final String MOCK_CLIENT_NAME = 'John'

    @Shared
    static final String MOCK_CLIENT_SURNAME = 'Doe'

    @Shared
    static final String MOCK_COUNTRY_CODE = 'lv'

    @Shared
    static final String MOCK_CLIENT_EMAIL = 'john.doe@test.com'

    @Shared
    static final String MOCK_CLIENT_PASSWORD = '12345678'

    @Shared
    static final Long MOCK_COUNTRY_POPULATION = 1961600

    @Shared
    static final BigDecimal MOCK_COUNTRY_AREA = 64559

    @Shared
    List MOCK_COUNTRY_BORDERS = List.of("BLR", "EST", "LTU", "RUS")

    ClientBean bean = new ClientBean()

    ToClient toClient = Mock(ToClient)

    ToClientDto toClientDto = Mock(ToClientDto)

    ClientRegisterValidator firstValidator = Mock(ClientRegisterValidator)

    ClientRegisterValidator secondValidator = Mock(ClientRegisterValidator)

    List<ClientRegisterValidator> validators = List.of(firstValidator, secondValidator)

    ClientRepository repository = Mock(ClientRepository)

    CountryRepository countryRepository = Mock(CountryRepository);

    RestCountriesApi restCountriesApi = Mock(RestCountriesApi)

    @Subject
    ClientService service = new ClientService(toClient, toClientDto, repository, countryRepository, validators, restCountriesApi)

    Client client = Stub(Client)

    Country country = Mock(Country)

    CountryBean countryBean = Mock(CountryBean)

    void setup() {
        with(bean) {
            setName(MOCK_CLIENT_NAME)
            setSurname(MOCK_CLIENT_SURNAME)
            setCountryCode(MOCK_COUNTRY_CODE)
            setEmail(MOCK_CLIENT_EMAIL)
            setPassword(MOCK_CLIENT_PASSWORD)
        }
    }

    void "should register client when country already exists in db"() {
        when:
            service.register(bean)
        then:
            1 * firstValidator.validate(bean)
            1 * secondValidator.validate(bean)
            1 * countryRepository.findByCode(MOCK_COUNTRY_CODE) >> Optional.of(country)
            0 * restCountriesApi.fetchCountryByCountryCode(MOCK_COUNTRY_CODE)
            1 * toClient.convert(bean) >> client
            1 * repository.save(client)
    }

    void "should register client when country does not exist in db yet"() {
        given:
            countryBean.population >> MOCK_COUNTRY_POPULATION
            countryBean.area >> MOCK_COUNTRY_AREA
            countryBean.borders >> MOCK_COUNTRY_BORDERS
        when:
            service.register(bean)
        then:
            1 * firstValidator.validate(bean)
            1 * secondValidator.validate(bean)
            1 * countryRepository.findByCode(MOCK_COUNTRY_CODE) >> Optional.empty()
            1 * restCountriesApi.fetchCountryByCountryCode(MOCK_COUNTRY_CODE) >> countryBean
            1 * countryRepository.save(_ as Country)
            1 * toClient.convert(bean) >> client
            1 * repository.save(client)
    }

    void "should get user basic info"() {
        given:
            ClientDto clientDto = Mock(ClientDto){
                getName() >> MOCK_CLIENT_NAME
                getSurname() >> MOCK_CLIENT_SURNAME
                getCountry() >> MOCK_COUNTRY_CODE
                getEmail() >> MOCK_CLIENT_EMAIL
            }
            setSecurityContext()
        when:
            ClientDto returnedClientDto = service.getUserBasicInfo()
        then:
            1 * repository.findByEmail(MOCK_CLIENT_EMAIL) >> of(client)
            1 * toClientDto.convert(client) >> clientDto
            with (returnedClientDto) {
                assert name == MOCK_CLIENT_NAME
                assert surname == MOCK_CLIENT_SURNAME
                assert country == MOCK_COUNTRY_CODE
                assert email == MOCK_CLIENT_EMAIL
            }
    }

    void "should get client country info"() {
        given:
            setSecurityContext()
            client.getCountry() >> Mock(Country){
                getPopulation() >> MOCK_COUNTRY_POPULATION
                getArea() >> MOCK_COUNTRY_AREA
                getBorderingCountries() >> 'BLR;EST;LTU;RUS'

            }
        when:
            CountryDto returnedCountryDto = service.getClientCountryInfo()
        then:
            1 * repository.findByEmail(MOCK_CLIENT_EMAIL) >> of(client)
            with (returnedCountryDto) {
                assert population == MOCK_COUNTRY_POPULATION
                assert area == MOCK_COUNTRY_AREA
                assert borderingCountries == MOCK_COUNTRY_BORDERS
            }
    }

    void setSecurityContext() {
        Authentication authentication = Mock(Authentication)
        authentication.getName() >> MOCK_CLIENT_EMAIL
        SecurityContext securityContext = Mock(SecurityContext)
        securityContext.getAuthentication() >> authentication
        SecurityContextHolder.setContext(securityContext)
    }
}
