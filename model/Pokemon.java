package JogoPokemon.model;

import java.util.ArrayList;
import java.util.List;

/*
    ==========================================================
    CLASSE: Pokemon

    RESPONSABILIDADE:
    Representa um Pokémon dentro do jogo.

    Controla:
    - Nome
    - Level
    - HP
    - Ataque
    - Defesa
    - XP
    - Tipos
    - Movimentos
    - Evolução
    - Level up

    Esta é uma das principais entidades
    do sistema.
    ==========================================================
*/

public class Pokemon {

    // Nome do Pokémon
    private String nome;

    // Level do Pokémon
    private int level;

    // Vida atual
    private int hp;

    // Vida máxima
    private int hpMax;

    // Ataque
    private int ataque;

    // Defesa
    private int defesa;

    // Experiência
    private int xp;

    // Tipos do Pokémon
    private List<String> tipos;

    // Lista de movimentos
    private List<Movimento> movimentos;

    /*
        ======================================================
        CONSTRUTOR
        ======================================================
    */
    public Pokemon(String nome, int level, int hpMax, int ataque, int defesa) {
        this.nome = nome;
        this.level = level;
        this.hpMax = hpMax;

        // Pokémon começa com HP cheio
        this.hp = hpMax;
        this.ataque = ataque;
        this.defesa = defesa;
        this.xp = 0;

        // Inicializa listas
        this.tipos = new ArrayList<>();
        this.movimentos = new ArrayList<>();
    }

    /*
        ======================================================
        RECEBE DANO
        ======================================================
    */
    public void receberDano(int dano) {
        hp -= dano;

        /*
            Evita HP negativo
        */
        if (hp < 0) {
            hp = 0;
        }
    }

    /*
        ======================================================
        CURA POKÉMON
        ======================================================
    */
    public void curar(int valor) {
        hp += valor;

        /*
            Não ultrapassa HP máximo
        */
        if (hp > hpMax) {
            hp = hpMax;
        }
    }

    /*
        ======================================================
        VERIFICA DERROTA
        ======================================================
    */
    public boolean estaDerrotado() {
        return hp <= 0;
    }

    /*
    ======================================================
    GANHA XP

    A cada 100 XP:
    - sobe 1 nível
    ======================================================
*/
    public void ganharXp(int valor) {

    /*
        Adiciona XP
    */
        xp += valor;

        System.out.println(nome + " ganhou " + valor + " XP!");

        System.out.println("XP Atual: " + xp + "/100");

    /*
        Verifica level up
    */
        while (xp >= 100) {

        /*
            Remove 100 XP
        */
            xp -= 100;

        /*
            Sobe nível
        */
            subirLevel();
        }
    }

    /*
        ======================================================
        LEVEL UP
        ======================================================
    */
    private void subirLevel() {

    /*
        Sobe nível
    */
        level++;

    /*
        HP aumenta 1.5%
    */
        hpMax += (int)(hpMax * 0.2);

    /*
        Garante aumento mínimo
    */
        if (hpMax <= hp) {
            hpMax += 2;
        }

    /*
        Ataque e defesa aumentam
    */
        ataque += 2;

        defesa += 2;

    /*
        Cura total ao subir
    */

        System.out.println("\n=================================");

        System.out.println("LEVEL UP!");

        System.out.println(nome + " agora está no nível " + level + "!");

        System.out.println("HP Máximo: " + hpMax);

        System.out.println("Ataque: " + ataque);

        System.out.println("Defesa: " + defesa);

        System.out.println(
                "================================="
        );
    }

    /*
        ======================================================
        ADICIONA MOVIMENTO
        ======================================================
    */
    public void adicionarMovimento(Movimento movimento) {

        movimentos.add(movimento);
    }

    /*
        ======================================================
        ADICIONA TIPO
        ======================================================
    */
    public void adicionarTipo(String tipo) {
        tipos.add(tipo);
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getHpMax() {
        return hpMax;
    }

    public void setHpMax(int hpMax) {

        this.hpMax = hpMax;
    }

    public int getAtaque() {

        return ataque;
    }

    public void setAtaque(int ataque) {

        this.ataque = ataque;
    }

    public int getDefesa() {

        return defesa;
    }

    public void setDefesa(int defesa) {

        this.defesa = defesa;
    }

    public int getXp() {

        return xp;
    }

    public void setXp(int xp) {

        this.xp = xp;
    }

    public List<String> getTipos() {

        return tipos;
    }

    public void setTipos(List<String> tipos) {

        this.tipos = tipos;
    }

    public List<Movimento> getMovimentos() {

        return movimentos;
    }

    public void setMovimentos(List<Movimento> movimentos) {

        this.movimentos = movimentos;
    }
}