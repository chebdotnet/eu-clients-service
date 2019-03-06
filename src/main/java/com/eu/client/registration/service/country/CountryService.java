package com.eu.client.registration.service.country;

import com.eu.client.registration.restcountries.CountryBean;
import com.eu.client.registration.restcountries.RestCountriesApi;
import lombok.RequiredArgsConstructor;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm.SingleSourcePaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.eu.client.registration.service.country.CountriesPathNotFoundException.MESSAGE_TEMPLATE;
import static java.lang.String.format;
import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final RestCountriesApi restCountriesApi;

    public ShortestPathDto findTheShortestWay(String startCountry, String finishCountry) {
        try {
            Graph<String, DefaultEdge> directedGraph = buildGraph();
            DijkstraShortestPath<String, DefaultEdge> dijkstraAlg = new DijkstraShortestPath<>(directedGraph);
            SingleSourcePaths<String, DefaultEdge> iPaths = dijkstraAlg.getPaths(startCountry);
            GraphPath graphPath = iPaths.getPath(finishCountry);
            if (isNull(graphPath)) {
                throw new CountriesPathNotFoundException(format(MESSAGE_TEMPLATE, startCountry, finishCountry));
            }
            List<DefaultEdge> edgeList = graphPath.getEdgeList();
            return ShortestPathDto.builder().path(edgeList.toString()).build();
        } catch (IllegalArgumentException e) {
            throw new CountryIncorrectInputException("Seems the input data is invalid. Please re-check it!");
        }
    }

    private Graph buildGraph() {
        List<CountryBean> countries = restCountriesApi.fetchAllCountries();

        Graph<String, DefaultEdge> directedGraph =
                new DefaultDirectedGraph<>(DefaultEdge.class);

        countries.forEach(country -> directedGraph.addVertex(country.getAlpha3Code()));
        countries.forEach(country -> country.getBorders().forEach(targetVertexCountry -> directedGraph.addEdge(country.getAlpha3Code(), targetVertexCountry)));

        return directedGraph;
    }

}
