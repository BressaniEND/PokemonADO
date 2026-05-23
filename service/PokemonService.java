package JogoPokemon.service;

import JogoPokemon.API.PokemonAPI;
import JogoPokemon.dto.EvolutionResponse;
import JogoPokemon.dto.MoveResponse;
import JogoPokemon.dto.PokemonResponse;
import JogoPokemon.model.Movimento;
import JogoPokemon.model.Pokemon;

import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/*
    ==========================================================
    CLASSE: PokemonService

    RESPONSABILIDADE:
    Classe responsável pelas regras relacionadas
    à PokéAPI.

    Implementa:
    - Busca Pokémon
    - Busca movimentos
    - Sorteio de iniciais
    - Sorteio de inimigos
    - Evolução automática
    - Novos movimentos
    - Conversão JSON -> Objetos Java

    Esta classe funciona como camada
    de serviço do sistema.
    ==========================================================
*/

public class PokemonService {

    // Classe da API
    private final PokemonAPI api;

    // Gson para converter JSON
    private final Gson gson;

    // Random para sorteios
    private final Random random;

    /*
        ======================================================
        CONSTRUTOR
        ======================================================
    */
    public PokemonService() {

        this.api =
                new PokemonAPI();

        this.gson =
                new Gson();

        this.random =
                new Random();
    }

    /*
        ======================================================
        VERIFICA SE O POKÉMON É BASE FORM

        Um Pokémon base:
        - Não evolui de ninguém
        ======================================================
    */
    private boolean ehBaseForm(int id) {

        try {

            String json =
                    api.buscarSpecies(id);

            var obj =
                    gson.fromJson(
                            json,
                            com.google.gson.JsonObject.class
                    );

            return obj
                    .get("evolves_from_species")
                    .isJsonNull();
        }

        catch (Exception e) {

            return false;
        }
    }

    /*
        ======================================================
        BUSCA POKÉMON PELA API
        ======================================================
    */
    public Pokemon buscarPokemon(String id) {

        try {

            /*
                Busca JSON Pokémon
            */
            String json =
                    api.buscarPokemon(id);

            /*
                Converte JSON
            */
            PokemonResponse response =
                    gson.fromJson(
                            json,
                            PokemonResponse.class
                    );

            /*
                Stats básicas
            */
            int hp = 50;
            int ataque = 10;
            int defesa = 10;

            /*
                Lê stats
            */
            for (PokemonResponse.StatSlot stat
                    : response.stats) {

                switch (stat.stat.name) {

                    case "hp" ->
                            hp = stat.base_stat;

                    case "attack" ->
                            ataque = stat.base_stat;

                    case "defense" ->
                            defesa = stat.base_stat;
                }
            }

            /*
                Cria Pokémon
            */
            Pokemon pokemon =
                    new Pokemon(
                            response.name,
                            5,
                            hp,
                            ataque,
                            defesa
                    );

            /*
                Adiciona tipos
            */
            for (PokemonResponse.TypeSlot tipo
                    : response.types) {

                pokemon.adicionarTipo(
                        tipo.type.name
                );
            }

            /*
                Adiciona até 4 movimentos
            */
            /*
    ======================================================
    ADICIONA APENAS MOVIMENTOS
    APRENDIDOS POR LEVEL

    Ignora:
    - TM
    - tutor
    - egg
    ======================================================
*/
            int quantidade = 0;

            for (PokemonResponse.MoveSlot moveSlot
                    : response.moves) {

    /*
        Limite 4 movimentos
    */
                if (quantidade >= 4) {
                    break;
                }

    /*
        Verifica versões
    */
                for (PokemonResponse.VersionGroupDetails details
                        : moveSlot.version_group_details) {

        /*
            Aprende por level
        */
                    boolean aprendePorLevel =
                            details.move_learn_method.name
                                    .equals("level-up");

        /*
            Não pode level 0
        */
                    boolean levelValido =
                            details.level_learned_at > 0;

                    if (aprendePorLevel
                            && levelValido
                            && details.level_learned_at
                            <= pokemon.getLevel()) {

                        Movimento movimento =
                                buscarMovimento(
                                        moveSlot.move.name
                                );

                        if (movimento != null) {

                            pokemon.adicionarMovimento(
                                    movimento
                            );

                            quantidade++;
                        }

                        break;
                    }
                }
            }

            return pokemon;
        }

        catch (Exception e) {

            System.out.println(
                    "Erro ao buscar Pokémon."
            );

            return null;
        }
    }

    /*
        ======================================================
        BUSCA MOVIMENTO PELA API
        ======================================================
    */
    public Movimento buscarMovimento(
            String nomeMovimento) {

        try {

            String json = api.buscarMovimento(nomeMovimento);

            MoveResponse response = gson.fromJson(json, MoveResponse.class);

            /*
                Alguns golpes possuem power null
            */
            int dano = response.power == 0 ? 40 : response.power;

            /*
                Alguns golpes possuem accuracy null
            */
            int precisao = response.accuracy == 0 ? 100 : response.accuracy;

            return new Movimento(
                    response.name,
                    response.type.name,
                    dano,
                    precisao,
                    response.pp
            );
        }

        catch (Exception e) {

            return null;
        }
    }

    /*
        ======================================================
        SORTEIA 3 POKÉMONS BASE FORM
        ENTRE OS 150 PRIMEIROS
        ======================================================
    */
    public Pokemon[] gerarIniciais() {

        Pokemon[] escolha = new Pokemon[3];

        Set<Integer> usados = new HashSet<>();

        int i = 0;

        while (i < 3) {

            /*
                Sorteia ID entre 1 e 150
            */
            int id = random.nextInt(150) + 1;

            /*
                Evita repetidos
            */
            if (usados.contains(id)) {
                continue;
            }

            /*
                Verifica base form
            */
            if (!ehBaseForm(id)) {
                continue;
            }

            usados.add(id);

            escolha[i] = buscarPokemon(String.valueOf(id));

            i++;
        }

        return escolha;
    }

    /*
    ======================================================
    GERA INIMIGO ALEATÓRIO

    O inimigo terá:
    - level parecido com o jogador
    - atributos escalando por nível
    ======================================================
*/
    public Pokemon gerarInimigo(
            int levelJogador) {

    /*
        Sorteia Pokémon
    */
        int id = random.nextInt(150) + 1;
        Pokemon inimigo = buscarPokemon(String.valueOf(id));

    /*
        Pequena variação de level
    */
        int variacao = random.nextInt(3) - 1;

        int novoLevel = Math.max(1, levelJogador + variacao);

    /*
        Calcula diferença
        do level base 5
    */
        int diferenca = novoLevel - 5;

    /*
        Atualiza level
    */
        inimigo.setLevel(novoLevel);
    /*
        Escala stats

        HP aumenta 1.5%
        por nível.
    */
        for (int i = 0; i < diferenca; i++) {
            inimigo.setHpMax(inimigo.getHpMax() + (int)(inimigo.getHpMax() * 0.015));

            inimigo.setAtaque(inimigo.getAtaque() + 2);

            inimigo.setDefesa(inimigo.getDefesa() + 2);
        }

    /*
        Recupera HP total
    */
        inimigo.setHp(inimigo.getHpMax());

        return inimigo;
    }

    /*
        ======================================================
        EVOLUÇÃO AUTOMÁTICA

        Busca evolução diretamente
        da PokéAPI.
        ======================================================
    */
    public void evoluirPokemon(
            Pokemon pokemon, int evolutionId) {

        try {

            String json = api.buscarEvolucao(evolutionId);

            EvolutionResponse response = gson.fromJson(json, EvolutionResponse.class);

            /*
                Verifica próxima evolução
            */
            if (response.chain.evolves_to.isEmpty()) {

                return;
            }

            /*
                Nome evolução
            */
            String evolucao = response.chain
                            .evolves_to
                            .get(0)
                            .species
                            .name;

            /*
                Atualiza nome
            */
            pokemon.setNome(
                    evolucao
            );

            /*
                Buff stats
            */
            pokemon.setHpMax(pokemon.getHpMax() + 30);

            pokemon.setAtaque(pokemon.getAtaque() + 15);

            pokemon.setDefesa(pokemon.getDefesa() + 15);

            System.out.println("\n=================================");

            System.out.println("Seu Pokémon evoluiu para " + evolucao + "!");

            System.out.println("=================================");
        }

        catch (Exception e) {

            System.out.println("Erro ao evoluir Pokémon.");
        }
    }
}