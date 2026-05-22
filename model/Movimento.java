package JogoPokemon.model;

/*
    ==========================================================
    CLASSE: Movimento

    RESPONSABILIDADE:
    Representa um movimento/ataque Pokémon.

    Cada movimento possui:
    - Nome
    - Tipo
    - Dano
    - Precisão
    - PP atual
    - PP máximo

    Essas informações são utilizadas
    durante o sistema de batalha.
    ==========================================================
*/

public class Movimento {

    // Nome do golpe
    private String nome;

    // Tipo do golpe
    private String tipo;

    // Dano/poder do golpe
    private int dano;

    // Precisão do golpe
    private int precisao;

    // PP atual
    private int ppAtual;

    // PP máximo
    private int ppMax;

    /*
        ======================================================
        CONSTRUTOR
        ======================================================
    */
    public Movimento(String nome,
                     String tipo,
                     int dano,
                     int precisao,
                     int ppMax) {

        this.nome = nome;

        this.tipo = tipo;

        this.dano = dano;

        this.precisao = precisao;

        this.ppMax = ppMax;

        // Começa com PP cheio
        this.ppAtual = ppMax;
    }

    /*
        ======================================================
        USA PP DO MOVIMENTO

        Retorna:
        true  -> conseguiu usar
        false -> sem PP
        ======================================================
    */
    public boolean usarPP() {

        /*
            Verifica PP
        */
        if (ppAtual <= 0) {

            return false;
        }

        /*
            Gasta 1 PP
        */
        ppAtual--;

        return true;
    }

    /*
        ======================================================
        RESTAURA PP TOTAL
        ======================================================
    */
    public void restaurarPP() {

        this.ppAtual = ppMax;
    }

    /*
        ======================================================
        GETTERS E SETTERS
        ======================================================
    */

    public String getNome() {

        return nome;
    }

    public void setNome(String nome) {

        this.nome = nome;
    }

    public String getTipo() {

        return tipo;
    }

    public void setTipo(String tipo) {

        this.tipo = tipo;
    }

    public int getDano() {

        return dano;
    }

    public void setDano(int dano) {

        this.dano = dano;
    }

    public int getPrecisao() {

        return precisao;
    }

    public void setPrecisao(int precisao) {

        this.precisao = precisao;
    }

    public int getPpAtual() {

        return ppAtual;
    }

    public void setPpAtual(int ppAtual) {

        this.ppAtual = ppAtual;
    }

    public int getPpMax() {

        return ppMax;
    }

    public void setPpMax(int ppMax) {

        this.ppMax = ppMax;
    }
}