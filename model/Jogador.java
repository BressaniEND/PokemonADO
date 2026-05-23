package JogoPokemon.model;

/*
    ==========================================================
    CLASSE: Jogador

    RESPONSABILIDADE:
    Representa o jogador do jogo.

    Controla:
    - Pokémon principal
    - Quantidade de poções
    - Uso de poções

    Cada jogador possui:
    - 1 Pokémon
    - 5 poções iniciais
    ==========================================================
*/

public class Jogador {

    // Pokémon do jogador
    private Pokemon pokemon;

    // Quantidade de poções
    private int pocoes;

    /*
        ======================================================
        CONSTRUTOR
        ======================================================
    */
    public Jogador(Pokemon pokemon) {

        this.pokemon = pokemon;

        // Jogador começa com 5 poções
        this.pocoes = 5;
    }

    /*
        ======================================================
        USA POÇÃO

        Recupera:
        +50 HP
        ======================================================
    */
    public void usarPocao() {

        /*
            Verifica se possui poções
        */
        if (pocoes <= 0) {
            System.out.println("Você não possui poções.");

            return;
        }

        /*
            Usa poção
        */
        pocoes--;

        /*
            Cura Pokémon
        */
        pokemon.curar(50);

        System.out.println(pokemon.getNome() + " recuperou 50 HP!");

        System.out.println("Poções restantes: " + pocoes);
    }

    /*
        ======================================================
        GETTERS E SETTERS
        ======================================================
    */

    public Pokemon getPokemon() {

        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {

        this.pokemon = pokemon;
    }

    public int getPocoes() {

        return pocoes;
    }

    public void setPocoes(int pocoes) {

        this.pocoes = pocoes;
    }
}