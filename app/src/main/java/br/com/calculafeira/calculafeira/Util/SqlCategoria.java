package br.com.calculafeira.calculafeira.Util;

import java.util.ArrayList;
import java.util.Arrays;
import br.com.calculafeira.calculafeira.DAO.CategoriaDAO;

/**
 * Created by DPGE on 23/06/2017.
 */

public class SqlCategoria {
    private static final ArrayList<String> CODIGO_SQL = new ArrayList<String>(Arrays.asList(
            "INSERT INTO " + CategoriaDAO.TABELA + " (nomeCategoria) VALUES ('" + CategoriaDAO.ALIMENTO + "');",
            "INSERT INTO " + CategoriaDAO.TABELA + " (nomeCategoria) VALUES ('" + CategoriaDAO.BEBIDA + "');",
            "INSERT INTO " + CategoriaDAO.TABELA + " (nomeCategoria) VALUES ('" + CategoriaDAO.LIMPEZA + "');",
            "INSERT INTO " + CategoriaDAO.TABELA + " (nomeCategoria) VALUES ('" + CategoriaDAO.HIGIENE + "');"
        )
    );

}
