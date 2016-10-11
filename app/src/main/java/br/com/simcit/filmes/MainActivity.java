package br.com.simcit.filmes;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import br.com.simcit.filmes.dao.FilmeDao;
import br.com.simcit.filmes.model.Filme;

public class MainActivity extends AppCompatActivity {

    private ListView listViewFilmes;
    private ArrayList<Filme> filmes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Obtendo os componentes que criamos no main_activity.xml
        listViewFilmes = (ListView) findViewById(R.id.lv_filmes);
        //Aqui definimos a cação que será realizada ao clicar no floatActionButton
        //Tutorial de como usar: https://guides.codepath.com/android/floating-action-buttons
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itNovoFilme = new Intent(getBaseContext(), GerenciarFilmeActivity.class);
                startActivity(itNovoFilme);
            }
        });

        //Definimos um Listener, que irá ficar escutando o clique na lista, para que possamos enviar
        // o filme para a activityGerenciar onde iremos fazer sua edição
        listViewFilmes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Pegamos o filme que na lista de filmes está na posição recebida pelo parametro position
                Filme filme = filmes.get(position);
                //Criamos uma intent com a intenção de ir para a activity Gerenciar Filme
                Intent itAlterarFilme = new Intent(getBaseContext(), GerenciarFilmeActivity.class);
                //Inserimos o filme dentro da activity para podermos recuperar-lo posteriormente
                itAlterarFilme.putExtra("filme", filme);
                startActivity(itAlterarFilme);
            }
        });

        //Definimos que a listView irá utilizar Menu de Contexto
        // (menu que é exibido ao pressionar um item da lista por alguns segundos)
        registerForContextMenu(listViewFilmes);
    }

    //Definimos título e um item ao nosso menu de Contexto
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Opções");
        menu.add("Excluir");
    }

    //Metodo que será executado quando um item do menu de contexto for selecionado
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //Se o item que foi selecionado conter o titulo do item que definomos
        // então iremos realizar a ação de excluir.
        //Obs.: Poderia ser o id o menu, caso fosse utilizado um menu xml.

        if (item.getTitle() == "Excluir") {
            //Criamos o obejeto dao, para realizar as operações no banco de dados
            FilmeDao dao = new FilmeDao(getBaseContext());
            //Pegamos as informações do menu atraves do item selecionado, utilziando o AdapterView  e getMenuInfo
            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            //Obtemos o item da posição selecionada e fazermos um cast para o tipo filme
            Filme filme = (Filme) listViewFilmes.getItemAtPosition(menuInfo.position);
            //Depois de obtermos o filme, solicitamos ao objeto dao, que exclua o filme selecionado
            dao.excluir(filme);
            //Após realizar a ação nós atualizamos a lista de filmes
            atualizarLista();
            return true;
        }

        return false;
    }

    @Override
    protected void onResume() {
        //Atualizamos a lista de filmes , dentro do metódo onResume para que se realizamos a
        // operação de inserir ao retornar a activity a lista seja atualizada.
        //Detalhe importante que falamos sobre o ciclo de vida da activity.
        // Dica: É uma boa prática atualizar dados da interface dentro do metodo onResume()
        atualizarLista();
        super.onResume();
    }

    /***Método responsável por obter os dados do banco de dados e preencher a lista;
     * Para aprender mais sobre ListView: https://developer.android.com/guide/topics/ui/layout/listview.html */
    private void atualizarLista() {
        //Criamos um objeto dao e solicitamos que ele nos devolva um ArrayList com todos os
        // filmes que estão no banco de dados
        FilmeDao dao = new FilmeDao(getBaseContext());
        filmes = dao.getFilmes();

        //Criamos um adaptador que será resposável por exibir os dados de cada filme que esta no ArrayList
        ArrayAdapter<Filme> adapter = new ArrayAdapter<>(
                getBaseContext(), android.R.layout.simple_list_item_1, filmes);

        //Agora setamos o adapter na nossa lista
        listViewFilmes.setAdapter(adapter);
    }
}
