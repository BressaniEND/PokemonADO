package JogoPokemon.dto;

import java.util.List;

/*
    ==========================================================
    CLASSE: PokemonResponse

    RESPONSABILIDADE:
    Representa o JSON retornado pela PokéAPI
    no endpoint:

    /pokemon/{id}
    /pokemon/{nome}

    Essa classe é utilizada pelo Gson para
    converter automaticamente o JSON da API
    em objetos Java.

    Ela contém:
    - Nome
    - Stats
    - Tipos
    - Movimentos

    Exemplo:
    Pikachu
    Charizard
    Blastoise
    ==========================================================
*/


public class PokemonResponse {

    /*
        Nome do Pokémon
    */
    public String name;

    /*
        Lista de stats
    */
    public List<StatSlot> stats;

    /*
        Lista de tipos
    */
    public List<TypeSlot> types;

    /*
        Lista de movimentos
    */
    public List<MoveSlot> moves;

    /*
        ======================================================
        CLASSE INTERNA: StatSlot

        Representa uma stat do Pokémon.

        Exemplo:
        hp
        attack
        defense
        ======================================================
    */
    public static class StatSlot {

        /*
            Valor da stat
        */
        public int base_stat;

        /*
            Informações da stat
        */
        public Stat stat;
    }

    /*
        ======================================================
        CLASSE INTERNA: Stat

        Nome da stat.
        ======================================================
    */
    public static class Stat {

        /*
            Nome da stat
        */
        public String name;
    }

    /*
        ======================================================
        CLASSE INTERNA: TypeSlot

        Representa um tipo do Pokémon.
        ======================================================
    */
    public static class TypeSlot {

        /*
            Informações do tipo
        */
        public Type type;
    }

    /*
        ======================================================
        CLASSE INTERNA: Type

        Nome do tipo.

        Exemplos:
        fire
        water
        grass
        electric
        ======================================================
    */
    public static class Type {

        /*
            Nome do tipo
        */
        public String name;
    }

    /*
        ======================================================
        CLASSE INTERNA: MoveSlot

        Representa um movimento aprendido
        pelo Pokémon.
        ======================================================
    */
    public static class MoveSlot {

        /*
            Informações do movimento
        */
        public List<VersionGroupDetails> version_group_details;
        public Move move;

    }

    /*
        ======================================================
        CLASSE INTERNA: Move

        Nome do movimento.
        ======================================================
    */
    public static class Move {

        /*
            Nome do golpe
        */
        public String name;
    }

    /*
    ======================================================
    LEVEL DO MOVIMENTO
    ======================================================
*/
    public static class VersionGroupDetails {

        public int level_learned_at;

        public MoveLearnMethod move_learn_method;
    }

    /*
        ======================================================
        MÉTODO DE APRENDIZADO
        ======================================================
    */
    public static class MoveLearnMethod {

        public String name;
    }
}