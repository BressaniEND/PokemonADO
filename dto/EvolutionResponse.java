package JogoPokemon.dto;

import java.util.List;

/*
    ==========================================================
    CLASSE: EvolutionResponse

    RESPONSABILIDADE:
    Representa o JSON retornado pela PokéAPI
    no endpoint:

    /evolution-chain/{id}

    Essa classe é utilizada pelo Gson para
    converter automaticamente o JSON da API
    em objetos Java.

    Ela contém:
    - Cadeia evolutiva
    - Próximas evoluções
    - Nome das evoluções

    Exemplo:
    Bulbasaur -> Ivysaur -> Venusaur
    ==========================================================
*/

public class EvolutionResponse {

    /*
        Cadeia principal de evolução
    */
    public Chain chain;

    /*
        ======================================================
        CLASSE INTERNA: Chain

        Representa um estágio da evolução.
        ======================================================
    */
    public static class Chain {

        /*
            Espécie atual
        */
        public Species species;

        /*
            Próximas evoluções
        */
        public List<Chain> evolves_to;
    }

    /*
        ======================================================
        CLASSE INTERNA: Species

        Representa o nome da espécie.
        ======================================================
    */
    public static class Species {
        /*
            Nome do Pokémon
        */
        public String name;
    }
}