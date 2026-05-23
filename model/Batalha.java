package JogoPokemon.model;

import java.util.Random;
import java.util.Scanner;

/*
    ==========================================================
    CLASSE: Batalha

    RESPONSABILIDADE:
    Controla todo o sistema de batalha do jogo.

    Implementa:
    - Turnos
    - Ataques
    - Precisão
    - PP
    - Fraquezas elementais
    - Uso de poções
    - Fuga
    - Atualização de status
    - Vitória e derrota

    Esta classe é uma das principais regras
    do sistema.
    ==========================================================
*/

public class Batalha {

    // Jogador da batalha
    private final Jogador jogador;

    // Pokémon inimigo
    private final Pokemon inimigo;

    // Scanner do teclado
    private final Scanner scanner;

    // Random para precisão
    private final Random random;

    // Máximo de tentativas de fuga
    private int tentativasFuga = 4;

    /*
        Construtor
    */
    public Batalha(Jogador jogador, Pokemon inimigo) {

        this.jogador = jogador;

        this.inimigo = inimigo;

        this.scanner = new Scanner(System.in);

        this.random = new Random();
    }

    /*
        ======================================================
        INICIA A BATALHA
        ======================================================
    */
    public void iniciar() {

        System.out.println("\n========================");
        System.out.println("BATALHA INICIADA");
        System.out.println("========================");

        /*
            Loop principal da batalha
        */
        while (true) {
            Pokemon pokemonJogador = jogador.getPokemon();

            /*
                ==============================================
                MOSTRA STATUS
                ==============================================
            */
            mostrarStatus();

            /*
                ==============================================
                MENU
                ==============================================
            */
            System.out.println("\n1 - Atacar");
            System.out.println("2 - Usar Poção");
            System.out.println("3 - Fugir");

            int escolha = scanner.nextInt();

            /*
                ==============================================
                ATACAR
                ==============================================
            */
            if (escolha == 1) {

                /*
                    Mostra movimentos
                */
                for (int i = 0; i < pokemonJogador.getMovimentos().size(); i++) {

                    Movimento m = pokemonJogador.getMovimentos().get(i);

                    System.out.println(
                            (i + 1)
                                    + " - "
                                    + m.getNome()
                                    + " | PP: "
                                    + m.getPpAtual()
                                    + "/"
                                    + m.getPpMax()
                    );
                }

                /*
                    Escolha movimento
                */
                int movEscolha = scanner.nextInt();

                Movimento movimento = pokemonJogador.getMovimentos().get(movEscolha - 1);

                /*
                    Jogador ataca
                */
                atacar(pokemonJogador, inimigo, movimento);
            }

            /*
                ==============================================
                USAR POÇÃO
                ==============================================
            */
            else if (escolha == 2) {jogador.usarPocao();
            }

            /*
    ==============================================
    FUGIR
    ==============================================
*/
            else if (escolha == 3) {

    /*
        Verifica tentativas restantes
    */
                if (tentativasFuga <= 0) {

                    System.out.println(
                            "Você não pode mais fugir!"
                    );

                    continue;
                }

    /*
        Gasta tentativa
    */
                tentativasFuga--;

    /*
        Chance de fuga
    */
                int chance =
                        random.nextInt(100) + 1;

    /*
        70% de chance
    */
                if (chance <= 70) {

                    System.out.println(
                            "Você fugiu da batalha!"
                    );

                    System.out.println(
                            "Tentativas restantes: "
                                    + tentativasFuga
                    );

                    return;
                }

    /*
        Falhou ao fugir
    */
                System.out.println(
                        "Falhou ao fugir!"
                );

                System.out.println(
                        "Tentativas restantes: "
                                + tentativasFuga
                );
            }

            /*
                ==============================================
                VERIFICA DERROTA INIMIGO
                ==============================================
            */
            if (inimigo.estaDerrotado()) {
                System.out.println("\n" + inimigo.getNome() + " foi derrotado!");

                /*
                    Dá XP
                */
                pokemonJogador.ganharXp(50);

                return;
            }

            /*
                ==============================================
                TURNO INIMIGO
                ==============================================
            */
            Movimento movimentoInimigo = inimigo.getMovimentos().get(random.nextInt(inimigo.getMovimentos().size()));

            atacar(inimigo, pokemonJogador, movimentoInimigo);

            /*
                ==============================================
                VERIFICA DERROTA JOGADOR
                ==============================================
            */
            if (pokemonJogador.estaDerrotado()) {
                System.out.println("\nSeu Pokémon foi derrotado!");
                return;
            }
        }
    }

    /*
        ======================================================
        MOSTRA STATUS DOS POKÉMONS
        ======================================================
    */
    private void mostrarStatus() {

        Pokemon jogadorPokemon = jogador.getPokemon();

        System.out.println("\n========================");

        System.out.println(jogadorPokemon.getNome() + " | LV " + jogadorPokemon.getLevel());

        System.out.println("HP: " + jogadorPokemon.getHp() + "/" + jogadorPokemon.getHpMax());

        System.out.println("------------------------");

        System.out.println(inimigo.getNome() + " | LV " + inimigo.getLevel());

        System.out.println("HP: " + inimigo.getHp() + "/" + inimigo.getHpMax());

        System.out.println("========================");
    }

    /*
        ======================================================
        REALIZA ATAQUE

        Considera:
        - PP
        - Precisão
        - Fraquezas
        - Resistências
        ======================================================
    */
    private void atacar(Pokemon atacante, Pokemon defensor, Movimento movimento) {

        /*
            Verifica PP
        */
        if (!movimento.usarPP()) {
            System.out.println("Sem PP.");

            return;
        }

        /*
            Precisão
        */
        int chance = random.nextInt(100) + 1;

        /*
            Errou ataque
        */
        if (chance > movimento.getPrecisao()) {
            System.out.println(atacante.getNome() + " errou o ataque!");
            return;
        }

        /*
            Cálculo dano base
        */
        int dano = movimento.getDano() + atacante.getAtaque()/2 - defensor.getDefesa()/3;
        System.out.println("Movimento:" + movimento.getDano());
        System.out.println("Ataque: "+ atacante.getAtaque());
        System.out.println("Defesa: " + defensor.getDefesa());
        System.out.println("Dano sem modificador: " + dano);

        /*
            Dano mínimo
        */
        if (dano < 1) {
            dano = 1;
        }

        /*
            Multiplicador elemental
        */
        double multiplicador =
                calcularFraqueza(
                        movimento.getTipo(),
                        defensor
                );

        dano =
                (int)(dano * multiplicador);
        System.out.println("Modificador: " +  multiplicador);
        System.out.println("Dano final: " +  dano);


        /*
            Aplica dano
        */
        defensor.receberDano(dano);

        /*
            Mensagens
        */
        System.out.println(atacante.getNome() + " usou " + movimento.getNome());

        if (multiplicador > 1) {
            System.out.println("É super efetivo!");
        }

        else if (multiplicador < 1) {
            System.out.println("Não foi muito efetivo...");
        }

        System.out.println("Causou " + dano + " de dano!");
    }

    /*
        ======================================================
        SISTEMA DE FRAQUEZAS E VANTAGENS
        ======================================================
    */
    private double calcularFraqueza(String tipoAtaque, Pokemon defensor) {

        for (String tipoDefensor : defensor.getTipos()) {

            /*
                FIRE > GRASS
            */
            if (tipoAtaque.equalsIgnoreCase("fire") && tipoDefensor.equalsIgnoreCase("grass")) {
                return 2.0;
            }

            /*
                WATER > FIRE
            */
            if (tipoAtaque.equalsIgnoreCase("water") && tipoDefensor.equalsIgnoreCase("fire")) {
                return 2.0;
            }

            /*
                GRASS > WATER
            */
            if (tipoAtaque.equalsIgnoreCase("grass") && tipoDefensor.equalsIgnoreCase("water")) {
                return 2.0;
            }

            /*
                GRASS < FIRE
            */
            if (tipoAtaque.equalsIgnoreCase("grass") && tipoDefensor.equalsIgnoreCase("fire")) {
                return 0.5;
            }

            /*
                FIRE < WATER
            */
            if (tipoAtaque.equalsIgnoreCase("fire") && tipoDefensor.equalsIgnoreCase("water")) {
                return 0.5;
            }

            /*
                WATER < GRASS
            */
            if (tipoAtaque.equalsIgnoreCase("water") && tipoDefensor.equalsIgnoreCase("grass")) {
                return 0.5;
            }
        }

        /*
            Dano neutro
        */
        return 1.0;
    }
}