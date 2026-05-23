package JogoPokemon.API;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PokemonAPI {

    private static final String URL = "https://pokeapi.co/api/v2/";

    private final HttpClient client;

    public PokemonAPI() {

        this.client = HttpClient.newHttpClient();
    }

    public String get(String endpoint) throws Exception {

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL + endpoint)).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {

            throw new RuntimeException(
                    "Erro na requisição."
            );
        }

        return response.body();
    }

    /*
        Busca Pokémon pelo nome
    */
    public String buscarPokemon(String nome) throws Exception {

        return get("pokemon/" + nome);
    }

    /*
        Busca Pokémon por ID
    */
    public String buscarPokemonPorId(int id) throws Exception {

        return get("pokemon/" + id);
    }

    /*
        Busca movimento
    */
    public String buscarMovimento(String nome) throws Exception {

        return get("move/" + nome);
    }

    /*
        Busca evolução
    */
    public String buscarEvolucao(int id) throws Exception {

        return get("evolution-chain/" + id);
    }

    /*
        Busca species do Pokémon
    */
    public String buscarSpecies(int id) throws Exception {

        return get("pokemon-species/" + id
        );
    }
}