package JogoPokemon.Application;

import JogoPokemon.model.Batalha;
import JogoPokemon.model.Jogador;
import JogoPokemon.model.Pokemon;
import JogoPokemon.repository.PokemonRepository;
import JogoPokemon.service.PokemonService;

import java.util.Scanner;

/*
    ==========================================================
    CLASSE: Main

    RESPONSABILIDADE:
    Classe principal do jogo.

    Controla:
    - Início do sistema
    - Escolha do Pokémon inicial
    - Loop principal
    - Sistema de batalhas
    - Save e Load
    - Progressão do jogador

    Esta é a porta de entrada do programa.
    ==========================================================
*/

public class Main {

    public static void main(String[] args)
            throws Exception {

        // Scanner para leitura do teclado
        Scanner scanner =
                new Scanner(System.in);

        // Service responsável pela PokéAPI
        PokemonService service =
                new PokemonService();

        // Banco de dados
        PokemonRepository repository =
                new PokemonRepository();

        /*
            ==================================================
            MENU INICIAL
            ==================================================
        */

        System.out.println("=================================");
        System.out.println("         POKEMON GAME");
        System.out.println("=================================");

        System.out.println("1 - Novo Jogo");
        System.out.println("2 - Carregar Jogo");

        int opcao =
                scanner.nextInt();

        scanner.nextLine();

        Jogador jogador;

        /*
            ==================================================
            LOAD
            ==================================================
        */
        if (opcao == 2) {

            jogador =
                    repository.carregar();

            // Caso não exista save
            if (jogador == null) {

                System.out.println(
                        "Nenhum save encontrado."
                );

                return;
            }

            System.out.println(
                    "Jogo carregado com sucesso!"
            );
        }

        /*
            ==================================================
            NOVO JOGO
            ==================================================
        */
        else {

            /*
                Sorteia 3 Pokémons
                base form aleatórios
            */
            Pokemon[] iniciais =
                    service.gerarIniciais();

            System.out.println("\nEscolha seu Pokémon:\n");

            /*
                Mostra os 3 Pokémons
            */
            for (int i = 0;
                 i < iniciais.length;
                 i++) {

                Pokemon p =
                        iniciais[i];

                System.out.println(
                        (i + 1)
                                + " - "
                                + p.getNome()
                );

                System.out.println(
                        "HP: "
                                + p.getHpMax()
                );

                System.out.println(
                        "ATK: "
                                + p.getAtaque()
                );

                System.out.println(
                        "DEF: "
                                + p.getDefesa()
                );

                System.out.println("-------------------");
            }

            /*
                Jogador escolhe
            */
            int escolha =
                    scanner.nextInt();

            scanner.nextLine();

            Pokemon pokemonEscolhido =
                    iniciais[escolha - 1];

            /*
                Cria jogador
            */
            jogador =
                    new Jogador(
                            pokemonEscolhido
                    );

            System.out.println(
                    "\nVocê escolheu "
                            + pokemonEscolhido.getNome()
                            + "!"
            );
        }

        /*
            ==================================================
            LOOP PRINCIPAL DO JOGO
            ==================================================
        */
        while (true) {

            System.out.println("\n========================");
            System.out.println("1 - Procurar batalha");
            System.out.println("2 - Salvar jogo");
            System.out.println("3 - Sair");
            System.out.println("========================");

            int menu =
                    scanner.nextInt();

            scanner.nextLine();

            /*
                ==============================================
                BATALHA
                ==============================================
            */
            if (menu == 1) {

                /*
                    Gera inimigo aleatório
                    próximo do level do jogador
                */
                Pokemon inimigo =
                        service.gerarInimigo(
                                jogador
                                        .getPokemon()
                                        .getLevel()
                        );

                System.out.println(
                        "\nUm "
                                + inimigo.getNome()
                                + " apareceu!"
                );

                /*
                    Inicia batalha
                */
                Batalha batalha =
                        new Batalha(
                                jogador,
                                inimigo
                        );

                batalha.iniciar();

                /*
                    Verifica derrota
                */
                if (jogador
                        .getPokemon()
                        .estaDerrotado()) {

                    System.out.println(
                            "\nSeu Pokémon foi derrotado."
                    );

                    break;
                }
            }

            /*
                ==============================================
                SAVE
                ==============================================
            */
            else if (menu == 2) {

                repository.salvar(jogador);

                System.out.println(
                        "Jogo salvo com sucesso!"
                );
            }

            /*
                ==============================================
                SAIR
                ==============================================
            */
            else if (menu == 3) {

                System.out.println(
                        "Encerrando jogo..."
                );

                break;
            }

            /*
                ==============================================
                OPÇÃO INVÁLIDA
                ==============================================
            */
            else {

                System.out.println(
                        "Opção inválida."
                );
            }
        }

        /*
            Fecha recursos
        */
        repository.close();

        scanner.close();
    }
}