package JogoPokemon.repository;

import JogoPokemon.model.Jogador;
import JogoPokemon.model.Movimento;
import JogoPokemon.model.Pokemon;

import java.sql.*;

/*
    ==========================================================
    CLASSE: PokemonRepository

    RESPONSABILIDADE:
    Responsável pela persistência de dados
    utilizando banco H2 + JDBC.

    Implementa:
    - Criação das tabelas
    - Save do jogador
    - Load do jogador
    - Conexão com banco
    - Fechamento da conexão

    O banco é salvo localmente
    através do arquivo:

    pokemon_db.mv.db
    ==========================================================
*/

public class PokemonRepository {


        //URL banco H2

    private static final String URL =
            "jdbc:h2:./pokemon_db";


        //Usuário padrão H2

    private static final String USER =
            "sa";

        //Senha padrão H2

    private static final String PASSWORD =
            "";

    // Conexão JDBC
    private Connection connection;

    /*
        ======================================================
        CONSTRUTOR
        ======================================================
    */
    public PokemonRepository() throws SQLException {

        connection = DriverManager.getConnection(
                        URL,
                        USER,
                        PASSWORD
                );

        System.out.println("Banco conectado com sucesso!");

        criarTabelas();
    }

    /*
        ======================================================
        CRIA TABELAS
        ======================================================
    */
    private void criarTabelas() {

        try (Statement st = connection.createStatement()) {

            /*
                ==============================================
                TABELA SAVE PLAYER
                ==============================================
            */
            st.execute("""
                    CREATE TABLE IF NOT EXISTS jogador (
                    
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        
                        nome_pokemon VARCHAR(100),
                        
                        level INT,
                        
                        hp INT,
                        
                        hp_max INT,
                        
                        ataque INT,
                        
                        defesa INT,
                        
                        xp INT,
                        
                        pocoes INT
                    )
                    """);

            /*
                ==============================================
                TABELA MOVIMENTOS
                ==============================================
            */
            st.execute("""
                    CREATE TABLE IF NOT EXISTS movimentos (
                    
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        
                        pokemon_nome VARCHAR(100),
                        
                        nome VARCHAR(100),
                        
                        tipo VARCHAR(50),
                        
                        dano INT,
                        
                        precisao INT,
                        
                        pp_atual INT,
                        
                        pp_max INT
                    )
                    """);

            /*
                ==============================================
                TABELA TIPOS
                ==============================================
            */
            st.execute("""
                    CREATE TABLE IF NOT EXISTS tipos (
                    
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        
                        pokemon_nome VARCHAR(100),
                        
                        tipo VARCHAR(50)
                    )
                    """);

            System.out.println(
                    "Tabelas criadas com sucesso!"
            );

        }

        catch (SQLException e) {

            System.out.println(
                    "Erro ao criar tabelas."
            );

            System.out.println(
                    e.getMessage()
            );
        }
    }

    /*
        ======================================================
        SAVE DO JOGO
        ======================================================
    */
    public void salvar(Jogador jogador) {

        try {

            /*
                Limpa save anterior
            */
            try (Statement st =
                         connection.createStatement()) {

                st.execute("DELETE FROM jogador");

                st.execute("DELETE FROM movimentos");

                st.execute("DELETE FROM tipos");
            }

            Pokemon p =
                    jogador.getPokemon();

            /*
                ==============================================
                SALVA JOGADOR
                ==============================================
            */
            try (PreparedStatement pstmt =
                         connection.prepareStatement("""
                                 INSERT INTO jogador
                                 (
                                 nome_pokemon,
                                 level,
                                 hp,
                                 hp_max,
                                 ataque,
                                 defesa,
                                 xp,
                                 pocoes
                                 )
                                 VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                                 """)) {

                pstmt.setString(
                        1,
                        p.getNome()
                );

                pstmt.setInt(
                        2,
                        p.getLevel()
                );

                pstmt.setInt(
                        3,
                        p.getHp()
                );

                pstmt.setInt(
                        4,
                        p.getHpMax()
                );

                pstmt.setInt(
                        5,
                        p.getAtaque()
                );

                pstmt.setInt(
                        6,
                        p.getDefesa()
                );

                pstmt.setInt(
                        7,
                        p.getXp()
                );

                pstmt.setInt(
                        8,
                        jogador.getPocoes()
                );

                pstmt.executeUpdate();
            }

            /*
                ==============================================
                SALVA MOVIMENTOS
                ==============================================
            */
            for (Movimento m
                    : p.getMovimentos()) {

                try (PreparedStatement pstmt =
                             connection.prepareStatement("""
                                     INSERT INTO movimentos
                                     (
                                     pokemon_nome,
                                     nome,
                                     tipo,
                                     dano,
                                     precisao,
                                     pp_atual,
                                     pp_max
                                     )
                                     VALUES (?, ?, ?, ?, ?, ?, ?)
                                     """)) {

                    pstmt.setString(
                            1,
                            p.getNome()
                    );

                    pstmt.setString(
                            2,
                            m.getNome()
                    );

                    pstmt.setString(
                            3,
                            m.getTipo()
                    );

                    pstmt.setInt(
                            4,
                            m.getDano()
                    );

                    pstmt.setInt(
                            5,
                            m.getPrecisao()
                    );

                    pstmt.setInt(
                            6,
                            m.getPpAtual()
                    );

                    pstmt.setInt(
                            7,
                            m.getPpMax()
                    );

                    pstmt.executeUpdate();
                }
            }

            /*
                ==============================================
                SALVA TIPOS
                ==============================================
            */
            for (String tipo : p.getTipos()) {

                try (PreparedStatement pstmt =
                             connection.prepareStatement("""
                                     INSERT INTO tipos
                                     (
                                     pokemon_nome,
                                     tipo
                                     )
                                     VALUES (?, ?)
                                     """)) {

                    pstmt.setString(1, p.getNome());

                    pstmt.setString(2, tipo);

                    pstmt.executeUpdate();
                }
            }

            System.out.println("Jogo salvo com sucesso!");
        }

        catch (SQLException e) {

            System.out.println(
                    "Erro ao salvar jogo."
            );

            System.out.println(e.getMessage()
            );
        }
    }

    /*
        ======================================================
        LOAD DO JOGO
        ======================================================
    */
    public Jogador carregar() {

        try {

            /*
                Busca save
            */
            try (Statement st = connection.createStatement()) {

                ResultSet rs = st.executeQuery(
                                "SELECT * FROM jogador LIMIT 1"
                        );

                /*
                    Sem save
                */
                if (!rs.next()) {

                    return null;
                }

                /*
                    Cria Pokémon
                */
                Pokemon pokemon = new Pokemon(
                                rs.getString(
                                        "nome_pokemon"
                                ),

                                rs.getInt(
                                        "level"
                                ),

                                rs.getInt(
                                        "hp_max"
                                ),

                                rs.getInt(
                                        "ataque"
                                ),

                                rs.getInt(
                                        "defesa"
                                )
                        );

                /*
                    Recupera HP e XP
                */
                pokemon.setHp(rs.getInt("hp"));

                pokemon.setXp(rs.getInt("xp"));

                /*
                    ==========================================
                    CARREGA MOVIMENTOS
                    ==========================================
                */
                try (PreparedStatement pstmt =
                             connection.prepareStatement("""
                                     SELECT * FROM movimentos
                                     WHERE pokemon_nome = ?
                                     """)) {

                    pstmt.setString(1, pokemon.getNome());

                    ResultSet movs = pstmt.executeQuery();

                    while (movs.next()) {

                        Movimento movimento = new Movimento(

                                        movs.getString("nome"),

                                        movs.getString("tipo"),

                                        movs.getInt("dano"),

                                        movs.getInt("precisao"),

                                        movs.getInt("pp_max")
                        );

                        movimento.setPpAtual(movs.getInt("pp_atual"));

                        pokemon.adicionarMovimento(movimento);
                    }
                }

                /*
                    ==========================================
                    CARREGA TIPOS
                    ==========================================
                */
                try (PreparedStatement pstmt = connection.prepareStatement("""
                                     SELECT * FROM tipos
                                     WHERE pokemon_nome = ?
                                     """)) {

                    pstmt.setString(1, pokemon.getNome());

                    ResultSet tipos = pstmt.executeQuery();

                    while (tipos.next()) {

                        pokemon.adicionarTipo(tipos.getString("tipo"));
                    }
                }

                /*
                    Cria jogador
                */
                Jogador jogador = new Jogador(pokemon);

                jogador.setPocoes(rs.getInt("pocoes"));

                System.out.println("Save carregado com sucesso!");

                return jogador;
            }
        }

        catch (SQLException e) {
            System.out.println("Erro ao carregar save.");

            System.out.println(e.getMessage());

            return null;
        }
    }

    /*
        ======================================================
        FECHA CONEXÃO
        ======================================================
    */
    public void close()
            throws SQLException {

        connection.close();
    }
}