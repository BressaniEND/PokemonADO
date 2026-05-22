package JogoPokemon.dto;

/*
    ==========================================================
    CLASSE: MoveResponse

    RESPONSABILIDADE:
    Representa o JSON retornado pela PokéAPI
    no endpoint:

    /move/{nome}

    Essa classe é utilizada pelo Gson para
    converter automaticamente o JSON da API
    em objetos Java.

    Ela contém:
    - Nome do movimento
    - PP
    - Precisão
    - Poder
    - Tipo

    Exemplo:
    ember
    water-gun
    vine-whip
    ==========================================================
*/

public class MoveResponse {

    /*
        Nome do movimento
    */
    public String name;

    /*
        Quantidade máxima de PP
    */
    public int pp;

    /*
        Precisão do movimento
    */
    public int accuracy;

    /*
        Poder/dano do movimento
    */
    public int power;

    /*
        Tipo do movimento
    */
    public Type type;

    /*
        ======================================================
        CLASSE INTERNA: Type

        Representa o tipo do golpe.

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
}